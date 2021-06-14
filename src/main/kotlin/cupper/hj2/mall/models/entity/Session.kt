package cupper.hj2.mall.models.entity

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import cupper.hj2.mall.settings.SessionConfig
import java.util.*

data class Session(
    val loginId: String,
    val userName: String,
    val config: SessionConfig) {

    val encodedValue: String by lazy {
        val now = System.currentTimeMillis()

        val algorithm = Algorithm.HMAC256(config.secret)

        JWT.create()
            .withIssuer(config.issuer)
            .withSubject(loginId)
            .withExpiresAt(Date(now + config.expirationTime))
            .withIssuedAt(Date(now))
            .sign(algorithm)
    }
}
