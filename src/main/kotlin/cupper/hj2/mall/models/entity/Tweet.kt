package cupper.hj2.mall.models.entity

data class Tweet(
    val id: Int,
    val ownerId: Int,
    val content: String,
    val hashTags: List<HashTag>?) {

    fun hashTags(): List<HashTag> {
        return parse(content)
    }

    companion object {
        private fun parse(content: String): List<HashTag> {
            return content.split(" ").fold(listOf<HashTag>()) { l, w ->
                if (w.startsWith("#")) {
                    l + HashTag(0, w)
                } else {
                    l
                }
            }
        }
    }
}
