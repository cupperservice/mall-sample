package cupper.hj2.mall.settings

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SessionConfig {
    @Value("\${session.issuer}")
    val issuer = ""

    @Value("\${session.expiration_time}")
    val expirationTime = 0

    @Value("\${session.secret}")
    val secret = "secret"

    @Value("\${session.table_name}")
    val tableName = ""
}
