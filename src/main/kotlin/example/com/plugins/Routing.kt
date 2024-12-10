package example.com.plugins

import example.com.JWTService
import example.com.LoginRequest
import example.com.LoginResponse
import example.com.routing.response.FlashCardResponse
import example.com.service.UserService
import io.ktor.client.request.HttpRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.http.content.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userService: UserService,
    jwtService: JWTService
) {
    routing {
        get("/") {
            call.respondText("Hello World! ${userService.getAllUsers()}")
        }

        get("/flash-cards") {
            val tempResponse = listOf(
                FlashCardResponse("test one", "test one description"),
                FlashCardResponse("test two", "test two description"),
            )

            call.respond(
                message = tempResponse
            )
        }

        post("/login") {
            val loginRequest = call.receive<LoginRequest>()
            val token = jwtService.createJwtToken(loginRequest)

            token?.let {
                call.respond(LoginResponse(it))
            } ?: call.respond(HttpStatusCode.Unauthorized)
        }

        authenticate{
            get("me") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal?.payload?.getClaim("username")?.asString()

                val foundUser = userService.getAllUsers().firstOrNull { it.name == username }

                foundUser?.let {
                    call.respond(foundUser)
                } ?: call.respond(HttpStatusCode.Unauthorized)
            }
        }

        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
