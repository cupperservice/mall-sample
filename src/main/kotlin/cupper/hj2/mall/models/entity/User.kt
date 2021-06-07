package cupper.hj2.mall.models.entity

import cupper.hj2.mall.models.values.LoginId
import cupper.hj2.mall.models.values.Password
import cupper.hj2.mall.models.values.UserId
import cupper.hj2.mall.models.values.UserName

data class User(val id: Int, val loginId: String, val password: String, val name: String)
