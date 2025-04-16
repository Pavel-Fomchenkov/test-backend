package mobi.sevenwinds.app.author

import com.papsign.ktor.openapigen.exceptions.OpenAPIRequiredFieldException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class AuthorService {
    private val message: String = "Некорректные параметры пагинации в запросе"
    suspend fun addAuthor(body: AuthorName): AuthorRecord = withContext(Dispatchers.IO) {
        transaction {
            val entity = AuthorEntity.new {
                this.name = body.name
                this.entryDate = DateTime.now()
            }
            return@transaction entity.toResponse()
        }
    }

    suspend fun getAuthors(param: AuthorRequest): List<AuthorRecord> = withContext(Dispatchers.IO) {
        transaction {
            val query = if (param.id != null) {
                AuthorTable
                    .select {
                        AuthorTable.id eq param.id
                    }
            } else if (param.name != null && param.entryDate != null) {
                if (param.limit == null || param.offset == null) throw OpenAPIRequiredFieldException(message)
                val formatter = DateTimeFormat.forPattern("dd.MM.yyyy")
                val dateTime = formatter.parseDateTime(param.entryDate)
                val startDate = dateTime.withTimeAtStartOfDay()
                val endDate = startDate.plusDays(1)

                AuthorTable
                    .select {
                        AuthorTable.name.like("%${param.name}%") and AuthorTable.entryDate.between(startDate, endDate)
                    }
            } else if (param.name != null) {
                if (param.limit == null || param.offset == null) throw OpenAPIRequiredFieldException(message)
                AuthorTable
                    .select {
                        AuthorTable.name.like("%${param.name}%")
                    }
            } else if (param.entryDate != null) {
                if (param.limit == null || param.offset == null) throw OpenAPIRequiredFieldException(message)
                val formatter = DateTimeFormat.forPattern("dd.MM.yyyy")
                val dateTime = formatter.parseDateTime(param.entryDate)
                val startDate = dateTime.withTimeAtStartOfDay()
                val endDate = startDate.plusDays(1)

                AuthorTable
                    .select {
                        AuthorTable.entryDate.between(startDate, endDate)
                    }
            } else {
                return@transaction emptyList()
            }

            val effectiveOffset = param.offset ?: 0
            val effectiveLimit = param.limit ?: Int.MAX_VALUE
            return@transaction AuthorEntity.wrapRows(query)
                .toList()
                .map { it.toResponse() }
                .drop(effectiveOffset)
                .take(effectiveLimit)
        }
    }
}