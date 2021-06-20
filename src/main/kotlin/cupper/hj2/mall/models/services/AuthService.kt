package cupper.hj2.mall.models.services

import cupper.hj2.mall.models.UserModel
import cupper.hj2.mall.models.entity.Session
import cupper.hj2.mall.models.repositories.SessionRepository
import cupper.hj2.mall.models.repositories.UserRepository
import cupper.hj2.mall.models.values.LoginId
import cupper.hj2.mall.models.values.Password
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository) {

    fun login(loginId: LoginId, password: Password): Session? {
        val user = userRepository.getByLoginId(loginId)
        return if (user == null) {
            null
        } else  {
            val userModel = UserModel(user, sessionRepository)
            if (userModel.checkPassword(password)) {
                val newSession = sessionRepository.newSession(user)
                sessionRepository.put(newSession)
            } else {
                null
            }
        }
    }

    fun logout(session: Session) {
        sessionRepository.delete(session)
    }
}
