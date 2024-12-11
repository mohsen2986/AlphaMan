package example.com

import example.com.db.DatabaseFactory
import example.com.plugins.*
import example.com.repository.RefreshTokenRepository
import example.com.repository.UserRepository
import example.com.service.JWTService
import example.com.service.UserService
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()
    val userRepository = UserRepository()
    val refreshTokenRepository = RefreshTokenRepository()

    val jwtService = JWTService(userRepository)
    val userService = UserService(userRepository , jwtService , refreshTokenRepository)

    configureSecurity(userService , jwtService)
    configureSerialization()
    configureRouting(userRepository , userService , jwtService)

}
