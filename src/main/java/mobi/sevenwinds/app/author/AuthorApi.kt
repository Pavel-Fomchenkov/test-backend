package mobi.sevenwinds.app.author

import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.path.normal.post
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route

fun NormalOpenAPIRoute.author() {
    route("/author") {
        val authorService = AuthorService()

        route("").get<AuthorRequest, List<AuthorRecord>>(info("Получить данные об авторах")) { param ->
            respond(authorService.getAuthors(param))
        }

        route("/add").post<Unit, AuthorRecord, AuthorName>(info("Добавить автора")) { param, body ->
            respond(authorService.addAuthor(body))
        }
    }
}

data class AuthorName(
    val name: String
)

data class AuthorRecord(
    val id: Int,
    val name: String,
    val entryDate: String
)

data class AuthorRequest(
    @QueryParam("id автора") val id: Int?,
    @QueryParam("Имя или часть имени") val name: String?,
    @QueryParam("Дата создания записи автора в формате \"dd.MM.yyyy\"") val entryDate: String?,
    @QueryParam("Лимит пагинации") val limit: Int?,
    @QueryParam("Смещение пагинации") val offset: Int?
)