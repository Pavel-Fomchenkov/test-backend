package mobi.sevenwinds.app.author

import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.path.normal.post
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route

fun NormalOpenAPIRoute.author() {
    route("/author") {
        route("/test").get<Unit, HelloResponse>(info("Получить тестовый ответ")) {
            respond(HelloResponse("Привет"))
        }

        route("/add").post<Unit, AuthorRecord, AuthorName>(info("Добавить автора")) { param, body ->
            val authorService = AuthorService()
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

class HelloResponse(
    val hello: String
)