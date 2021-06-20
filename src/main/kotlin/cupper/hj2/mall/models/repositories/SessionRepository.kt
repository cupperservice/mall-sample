package cupper.hj2.mall.models.repositories

import cupper.hj2.mall.models.entity.Session
import cupper.hj2.mall.models.entity.User

interface SessionRepository {
    fun newSession(user: User): Session
    fun get(encodedValue: String): Session?
    fun put(session: Session): Session
    fun delete(session: Session): Unit
}
