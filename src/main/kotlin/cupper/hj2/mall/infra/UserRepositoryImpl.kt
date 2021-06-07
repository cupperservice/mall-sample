package cupper.hj2.mall.infra

import cupper.hj2.mall.models.entity.User
import cupper.hj2.mall.models.repositories.UserRepository
import cupper.hj2.mall.models.values.LoginId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.JdbcTemplate

@Repository
class UserRepositoryImpl(private val db: JdbcTemplate): UserRepository {
    override fun get(id: Int): User? {
        return db.queryForObject(
            """SELECT id, login_id, password, name
                FROM users
                WHERE id = ?
            """.trimIndent(), DataClassRowMapper<User>(), id)
    }

    override fun getByLoginId(loginId: LoginId): User? {
        return db.queryForObject(
            """SELECT id, login_id, password, name
                FROM users
                WHERE login_id = ?
            """.trimIndent(), DataClassRowMapper(User::class.java), loginId.value())
    }
}
