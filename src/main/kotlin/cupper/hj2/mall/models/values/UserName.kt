package cupper.hj2.mall.models.values

data class UserName(val name: String) {
    fun value(): String {
        return name
    }
}