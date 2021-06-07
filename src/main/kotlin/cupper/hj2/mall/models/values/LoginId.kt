package cupper.hj2.mall.models.values

data class LoginId(val login_id: String) {
    fun value(): String {
        return login_id
    }
}