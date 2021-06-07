package cupper.hj2.mall.models.repositories

import cupper.hj2.mall.models.entity.User
import cupper.hj2.mall.models.values.LoginId

interface UserRepository {
    fun get(id: Int): User?
    fun getByLoginId(loginId: LoginId): User?
}
