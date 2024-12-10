package example.com.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import example.com.JWTService
import example.com.LoginRequest
import example.com.service.UserService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import sun.security.util.KeyUtil.validate
import java.util.*

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
