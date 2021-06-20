package cupper.hj2.mall.infra

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import cupper.hj2.mall.models.entity.Session
import cupper.hj2.mall.models.entity.User
import cupper.hj2.mall.models.repositories.SessionRepository
import cupper.hj2.mall.settings.SessionConfig
import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest
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

        return if (item.hasItem()) {
            Session(
                User(
                    id = item.item().get("userId")!!.s().toInt(),
                    loginId = item.item().get("loginId")!!.s(),
                    password = null,
                    name = item.item().get("userName")!!.s()),
                sessionConfig, encodedValue)
        } else {
            return null
        }
    }

    override fun put(session: Session): Session {
        val item = mapOf<String, AttributeValue>(
            "sessionId" to AttributeValue.builder().s(session.encodedValue).build(),
            "userId" to AttributeValue.builder().s(session.user.id.toString()).build(),
            "loginId" to AttributeValue.builder().s(session.user.loginId).build(),
            "userName" to AttributeValue.builder().s(session.user.name).build(),
            "ttl" to AttributeValue.builder().n("${System.currentTimeMillis() / 1000L + sessionConfig.expirationTime}").build()
        )

        val request = PutItemRequest.builder()
            .tableName(sessionConfig.tableName)
            .item(item)
            .build()

        client.putItem(request)

        return session
    }

    override fun newSession(user: User): Session {
        val now = System.currentTimeMillis()

        val algorithm = Algorithm.HMAC256(sessionConfig.secret)

        val encodedValue = JWT.create()
            .withIssuer(sessionConfig.issuer)
            .withSubject(user.loginId)
            .withExpiresAt(Date(now + sessionConfig.expirationTime))
            .withIssuedAt(Date(now))
            .sign(algorithm)

        return Session(user, sessionConfig, encodedValue)
    }

    override fun delete(session: Session) {
        val item = mapOf<String, AttributeValue>(
            "sessionId" to AttributeValue.builder().s(session.encodedValue).build()
        )

        val request = DeleteItemRequest.builder()
            .tableName(sessionConfig.tableName)
            .key(item)
            .build()

        client.deleteItem(request)
    }
}
