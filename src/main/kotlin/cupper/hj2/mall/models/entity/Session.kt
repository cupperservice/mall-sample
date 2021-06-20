package cupper.hj2.mall.models.entity

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import cupper.hj2.mall.settings.SessionConfig
import java.util.*

data class Session(
    val user: User,
    val config: SessionConfig,
    val encodedValue: String) {
}
