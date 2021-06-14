package cupper.hj2.mall.models

import cupper.hj2.mall.models.entity.Session
import cupper.hj2.mall.models.entity.User
import cupper.hj2.mall.models.values.Password
import cupper.hj2.mall.models.repositories.SessionRepository

class UserModel(
    private val user: User,
    private val sessionRepository: SessionRepository) {

    fun checkPassword(password: Password): Boolean {
        return password.hashedValue() == user.password
    }
}
