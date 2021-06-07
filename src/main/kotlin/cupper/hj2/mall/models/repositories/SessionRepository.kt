package cupper.hj2.mall.models.repositories

import cupper.hj2.mall.models.entity.Session

interface SessionRepository {
    fun newSession(loginId: String): Session
    fun get(encodedValue: String): Session?
    fun put(session: Session)
}
