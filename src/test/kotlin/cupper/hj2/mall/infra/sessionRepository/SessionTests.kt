package cupper.hj2.mall.infra.sessionRepository

import cupper.hj2.mall.infra.SessionRepositoryImpl
import cupper.hj2.mall.models.repositories.SessionRepository
import cupper.hj2.mall.settings.SessionConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class SessionTests() {
    var sessionRepository: SessionRepository? = null

    class Config: SessionConfig() {
        override val issuer = "https://hogehoge.com"
        override val tableName = "cupper-Session"
        override val expirationTime = 3600
    }

    @BeforeEach
    fun before() {
        sessionRepository = SessionRepositoryImpl(Config())
    }

    @Test
    fun newSession() {
        sessionRepository!!.newSession("sample1@example.com")
    }
}
