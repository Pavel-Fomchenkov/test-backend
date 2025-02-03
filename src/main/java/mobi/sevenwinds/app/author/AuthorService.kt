package mobi.sevenwinds.app.author

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class AuthorService {
    suspend fun addAuthor(body: AuthorName): AuthorRecord = withContext(Dispatchers.IO) {
        transaction {
            val entity = AuthorEntity.new {
                this.name = body.name
                this.entryDate = DateTime.now()
            }
            return@transaction entity.toResponse()
        }
    }
}