package example.com.plugins

import example.com.service.JWTService
import example.com.service.UserService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity(
    userService: UserService ,
    jwtService: JWTService ,
) {


    authentication {
//        bearer("auth-bearer") {
//            authenticate { tokenCredential ->
//                val principal = verifyJwtToken(tokenCredential.token)
//                principal
//            }
//        }
        jwt {
            realm = jwtService.jwtRealm

            verifier(jwtService.jwtVerifier)

            validate { credential ->
                jwtService.jwtValidator(credential)
            }
        }
      }


}
