package example.com.plugins

import example.com.routing.response.FlashCardResponse
import example.com.service.UserService
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(userService: UserService) {
    routing {
        get("/") {
            call.respondText("Hello World! ${userService.getAllUsers()}")
        }

        get("/flash-cards") {
            val tempResponse = listOf(
                FlashCardResponse("test one" , "test one description") ,
                FlashCardResponse("test two" , "test two description") ,
            )

            call.respond(
                message = tempResponse
            )
        }

        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
