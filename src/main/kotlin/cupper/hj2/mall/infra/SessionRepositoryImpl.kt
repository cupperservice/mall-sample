package cupper.hj2.mall.infra

import cupper.hj2.mall.models.entity.Session
import cupper.hj2.mall.models.entity.User
import cupper.hj2.mall.models.repositories.SessionRepository
import cupper.hj2.mall.settings.SessionConfig
import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest

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
            Session(loginId = item.item().get("loginId")!!.s(),
                userName = item.item().get("userName")!!.s(),
                sessionConfig)
        } else {
            return null
        }
    }

    override fun put(session: Session): Session {
        val item = mapOf<String, AttributeValue>(
            "sessionId" to AttributeValue.builder().s(session.encodedValue).build(),
            "loginId" to AttributeValue.builder().s(session.loginId).build(),
            "userName" to AttributeValue.builder().s(session.userName).build(),
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
        return Session(loginId = user.loginId, userName = user.name, sessionConfig)
    }
}
