package example.com

import example.com.db.DatabaseFactory
import example.com.plugins.*
import example.com.service.UserService
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()
    val userService = UserService()
    val jwtService = JWTService(userService)

    configureSecurity(userService , jwtService)
    configureSerialization()
    configureRouting(userService , jwtService)

}
