package mobi.sevenwinds.app.budget

import mobi.sevenwinds.app.author.AuthorTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.select
import javax.annotation.Nullable

object BudgetTable : IntIdTable("budget") {
    val year = integer("year")
    val month = integer("month")
    val amount = integer("amount")
    val type = enumerationByName("type", 100, BudgetType::class)
    @Nullable val authorId = integer("author_id").nullable()
}

class BudgetEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BudgetEntity>(BudgetTable)

    var year by BudgetTable.year
    var month by BudgetTable.month
    var amount by BudgetTable.amount
    var type by BudgetTable.type
    @Nullable var authorId by BudgetTable.authorId

    fun toResponse(): BudgetRecord {
        return BudgetRecord(year, month, amount, type, authorId)
    }

    fun toStatsResponse(): BudgetStatsRecord {
        if (authorId == null) {
            return BudgetStatsRecord(year, month, amount, type, authorId, null, null)
        }

        val query = AuthorTable
            .select { AuthorTable.id eq authorId }
            .firstOrNull()
        val name: String? = query?.get(AuthorTable.name)
        val entryDate: String? = query?.get(AuthorTable.entryDate)?.toString("dd.MM.yyyy HH:mm:ss")

        return BudgetStatsRecord(year, month, amount, type, authorId, name, entryDate)
    }
}