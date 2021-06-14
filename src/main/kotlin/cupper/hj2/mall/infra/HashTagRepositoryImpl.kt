package cupper.hj2.mall.infra

import cupper.hj2.mall.models.entity.HashTag
import cupper.hj2.mall.models.entity.Tweet
import cupper.hj2.mall.models.repositories.HashTagRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class HashTagRepositoryImpl(private val db: JdbcTemplate) : HashTagRepository {
    override fun create(tweet: Tweet, hashTags: List<HashTag>): List<HashTag> {
        return hashTags.map {
            db.update(
                "INSERT INTO hash_tag (tag, tweet_id) VALUES (?,?)",
                it.tag, tweet.id
            )

            val id = db.queryForObject("SELECT LAST_INSERT_ID()", Int::class.java)
            it.copy(id = id!!)
        }
    }
}
