package mobi.sevenwinds.app.Author

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import java.time.LocalDateTime

object AuthorTable : IntIdTable("author") {
    val name = text("name")
    val entryDate = datetime("entry_date")
}

class AuthorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AuthorEntity>(AuthorTable)

    var name by AuthorTable.name
    var entryDate by AuthorTable.entryDate

    fun toResponse(): AuthorRecord {
        return AuthorRecord(name, LocalDateTime.now())
    }
}