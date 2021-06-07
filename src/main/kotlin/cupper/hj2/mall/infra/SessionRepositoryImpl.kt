package cupper.hj2.mall.infra

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import cupper.hj2.mall.models.entity.Session
import cupper.hj2.mall.models.repositories.SessionRepository
import cupper.hj2.mall.settings.SessionConfig
import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import java.util.*

@Repository
class SessionRepositoryImpl(private val sessionConfig: SessionConfig) : SessionRepository {
    val client = DynamoDbClient.create()

    override fun get(encodedValue: String): Session? {
        val request = GetItemRequest.builder()
            .key(mapOf<String, AttributeValue>(
                "sessionId" to AttributeValue.builder().s(encodedValue).build()
            ))
            .tableName(sessionConfig.tableName)
            .build()

        val item = client.getItem(request)

        return if (item == null) {
            return null
        } else {
            Session(encodedValue)
        }
    }

    override fun put(session: Session) {
        val item = mapOf<String, AttributeValue>(
            "sessionId" to AttributeValue.builder().s(session.encodedValue).build(),
            "ttl" to AttributeValue.builder().n("${System.currentTimeMillis() / 1000L + sessionConfig.expirationTime}").build()
        )

        val request = PutItemRequest.builder()
            .tableName(sessionConfig.tableName)
            .item(item)
            .build()

        client.putItem(request)
    }

    override fun newSession(loginId: String): Session {
        val now = System.currentTimeMillis()

        val algorithm = Algorithm.HMAC256(sessionConfig.secret)

        val token = JWT.create()
            .withIssuer(sessionConfig.issuer)
            .withSubject(loginId)
            .withExpiresAt(Date(now + sessionConfig.expirationTime))
            .withIssuedAt(Date(now))
            .sign(algorithm)

        val session = Session(token)
        put(session)

        return session
    }
}
