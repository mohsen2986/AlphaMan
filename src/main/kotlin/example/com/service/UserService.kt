package example.com.service

import com.auth0.jwt.interfaces.DecodedJWT
import example.com.LoginRequest
import example.com.model.User
import example.com.model.Users
import example.com.repository.RefreshTokenRepository
import example.com.repository.UserRepository
import example.com.routing.response.AuthResponse

class  UserService(
    private val userRepository: UserRepository,
    private val jwtService: JWTService,
    private val refreshTokenRepository: RefreshTokenRepository
)  {


    suspend fun authenticate(loginRequest: LoginRequest): AuthResponse? {
        val username = loginRequest.username
        val foundUser: User? = userRepository.findByUsername(username)

        val password = "1234"

        return if (foundUser != null && loginRequest.password == password) {
            val accessToken = jwtService.createAccessToken(username)
            val refreshToken = jwtService.createRefreshToken(username)

            refreshTokenRepository.save(refreshToken, username)

            AuthResponse(
                accessToken = accessToken,
                refreshToken = refreshToken,
            )
        } else
            null
    }

    suspend fun refreshToken(token: String): String? {
        val decodedRefreshToken = verifyRefreshToken(token)
        val persistedUsername = refreshTokenRepository.findUsernameByToken(token)

        return if (decodedRefreshToken != null && persistedUsername != null) {
            val foundUser: User? = userRepository.findByUsername(persistedUsername)
            val usernameFromRefreshToken: String? = decodedRefreshToken.getClaim("username").asString()

            if (foundUser != null && usernameFromRefreshToken == foundUser.name)
                jwtService.createAccessToken(persistedUsername)
            else
                null
        } else
            null
    }

    private fun verifyRefreshToken(token: String): DecodedJWT? {
        val decodedJwt: DecodedJWT? = getDecodedJwt(token)

        return decodedJwt?.let {
            val audienceMatches = jwtService.audienceMatches(it.audience.first())

            if (audienceMatches)
                decodedJwt
            else
                null
        }
    }

    private fun getDecodedJwt(token: String): DecodedJWT? =
        try {
            jwtService.jwtVerifier.verify(token)
        } catch (ex: Exception) {
            null
        }
}