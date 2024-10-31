package example.com.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import sun.security.util.KeyUtil.validate
import java.util.*

fun Application.configureSecurity() {
    // Please read the jwt property from the config file if you are using EngineMain
    val jwtAudience = "jwt-audience"
    val jwtDomain = "https://jwt-provider-domain/"
    val jwtRealm = "ktor sample app"
    val jwtSecret = "secret"

    @Serializable
    data class LoginRequest(val username: String, val password: String)

    @kotlinx.serialization.Serializable
    data class LoginResponse(val token: String)








    fun verifyJwtToken(token: String): UserIdPrincipal? {
        return try {
            val algorithm = Algorithm.HMAC256(jwtSecret)
            val verifier = JWT.require(algorithm)
                .withIssuer("yourdomain.com")
                .withAudience("ktor-audience")
                .build()

            val decodedJWT = verifier.verify(token)
            val username = decodedJWT.getClaim("username").asString()
            if (username != null) UserIdPrincipal(username) else null
        } catch (e: Exception) {
            null
        }
    }

    authentication {
        bearer("auth-bearer") {
            authenticate { tokenCredential ->
                val principal = verifyJwtToken(tokenCredential.token)
                principal
            }
        }
//        jwt {
//            realm = jwtRealm
//            verifier(
//                JWT
//                    .require(Algorithm.HMAC256(jwtSecret))
//                    .withAudience(jwtAudience)
//                    .withIssuer(jwtDomain)
//                    .build()
//            )
//            validate { credential ->
//                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
//            }
//        }
    }


}
