package mobi.sevenwinds.app.Author

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object AuthorTable : IntIdTable("author") {
    val name = text("name")
    val entryDate = datetime("entrydate")
}

class AuthorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AuthorEntity>(AuthorTable)

    var name by AuthorTable.name
    var entryDate by AuthorTable.entryDate

    fun toResponse(): AuthorRecord {
        return AuthorRecord(name, entryDate.toString("dd.MM.yyyy HH:mm:ss"))
    }
}