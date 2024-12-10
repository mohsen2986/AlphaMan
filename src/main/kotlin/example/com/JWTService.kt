package example.com

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import example.com.service.UserService
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.Date

class JWTService(
    val userService: UserService
) {

    // Please read the jwt property from the config file if you are using EngineMain
    val jwtAudience = "jwt-audience"
    val jwtDomain = "https://jwt-provider-domain/"
    val jwtRealm = "ktor sample app"
    val jwtSecret = "secret"





    val jwtVerifier: JWTVerifier =
        JWT
            .require (Algorithm.HMAC256(jwtSecret))
            .withAudience(jwtAudience)
            .withIssuer(jwtDomain )
            .build()




    suspend fun createJwtToken(loginRequest: LoginRequest): String?{
        val user = userService.getAllUsers().firstOrNull { it.name == loginRequest.username }
        val password = "1234"

        return if (user != null && loginRequest.password == password){
            JWT
                .create()
                .withAudience(jwtAudience)
                .withIssuer(jwtDomain )
                .withClaim("username" , user.name)
                .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000 ))
                .sign(Algorithm.HMAC256(jwtSecret))
        }else null
    }

    fun extractUserName(credential: JWTCredential): String?  =
        credential.payload.getClaim("username").asString()

    fun audienceMatchs(credential: JWTCredential): Boolean =
        credential.payload.audience.contains(jwtAudience )

    suspend fun jwtValidator(credential: JWTCredential): JWTPrincipal?{
        val username = extractUserName(credential)
        val foundUser = userService.getAllUsers().firstOrNull { it.name == username }

        return foundUser?.let {
            if(audienceMatchs(credential)){
                JWTPrincipal(payload = credential.payload)
            }else null
        }
    }

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
}