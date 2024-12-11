package example.com.plugins

import example.com.service.JWTService
import example.com.LoginRequest
import example.com.LoginResponse
import example.com.repository.UserRepository
import example.com.routing.response.FlashCardResponse
import example.com.routing.response.authRoute
import example.com.service.UserService
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
    userRepository: UserRepository,
    userService: UserService,
    jwtService: JWTService
) {
    routing {
        get("/") {
            call.respondText("Hello World! ")
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

//        post("/login") {
//            val loginRequest = call.receive<LoginRequest>()
//            val token = jwtService.createJwtToken(loginRequest)
//
//            token?.let {
//                call.respond(LoginResponse(it))
//            } ?: call.respond(HttpStatusCode.Unauthorized)
//        }

        authenticate{
            get("me") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal?.payload?.getClaim("username")?.asString() ?:
                    return@get call.respond(HttpStatusCode.Unauthorized)

                val foundUser = userRepository.findByUsername(username)

                foundUser?.let {
                    call.respond(foundUser)
                } ?: call.respond(HttpStatusCode.Unauthorized)
            }
        }

        route("/api/auth") {
            authRoute(userService)
        }

        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
