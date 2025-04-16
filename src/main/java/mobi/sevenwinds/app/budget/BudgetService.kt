package mobi.sevenwinds.app.budget

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobi.sevenwinds.app.author.AuthorTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object BudgetService {
    suspend fun addRecord(body: BudgetRecord): BudgetRecord = withContext(Dispatchers.IO) {
        transaction {
            val authorExists = AuthorTable.select { AuthorTable.id eq body.authorId }.count() > 0
            val entity = BudgetEntity.new {
                this.year = body.year
                this.month = body.month
                this.amount = body.amount
                this.type = body.type
                this.authorId = if (authorExists) body.authorId else null
            }

            return@transaction entity.toResponse()
        }
    }

    suspend fun getYearStats(param: BudgetYearParam): BudgetYearStatsResponse = withContext(Dispatchers.IO) {
        transaction {
            val query = if (param.name != null) {
                val authorIds = AuthorTable
                    .select { AuthorTable.name.like("%${param.name}%") }
                    .map { it[AuthorTable.id].value }

                BudgetTable
                    .select { (BudgetTable.authorId inList authorIds) and (BudgetTable.year eq param.year) }
                    .limit(param.limit, param.offset)
            } else {
                BudgetTable
                    .select { BudgetTable.year eq param.year }
                    .limit(param.limit, param.offset)
            }

            val total = query.count()
            val data = BudgetEntity.wrapRows(query)
                .map { it.toStatsResponse() }
                .sortedByDescending { it.amount }
                .sortedBy { it.month }

            val sumByType = data.groupBy { it.type.name }.mapValues { it.value.sumOf { v -> v.amount } }

            return@transaction BudgetYearStatsResponse(
                total = total,
                totalByType = sumByType,
                items = data
            )
        }
    }
}