package example.com.routing.response;

import example.com.LoginRequest
import example.com.routing.request.RefreshTokenRequest
import example.com.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.authRoute(userService: UserService) {

  post {
    val loginRequest = call.receive<LoginRequest>()

    val authResponse: AuthResponse? = userService.authenticate(loginRequest)

    authResponse?.let {
      call.respond(authResponse)
    } ?: call.respond(
      message = HttpStatusCode.Unauthorized
    )
  }

  post("/refresh") {
    val request = call.receive<RefreshTokenRequest>()

    val newAccessToken = userService.refreshToken(token = request.token)

    newAccessToken?.let {
      call.respond(
        RefreshTokenResponse(it)
      )
    } ?: call.respond(
      message = HttpStatusCode.Unauthorized
    )
  }

}