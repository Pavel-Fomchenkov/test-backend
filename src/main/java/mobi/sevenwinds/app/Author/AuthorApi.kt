package mobi.sevenwinds.app.Author

import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.path.normal.post
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import java.time.LocalDateTime

fun NormalOpenAPIRoute.author() {
    route("/author") {
        route("/test").get<Unit, AuthorResponse>(info("Получить тестовый ответ")) {
            respond(AuthorResponse("Привет"))
        }
        route("/add").post<Unit, AuthorRecord, AuthorRecord>(info("Добавить автора")) {
                param, body -> respond(body)
        }
    }
}

data class AuthorRecord(
    val name: String,
    val entryDate: LocalDateTime
)

class AuthorResponse(
    val hello: String
)