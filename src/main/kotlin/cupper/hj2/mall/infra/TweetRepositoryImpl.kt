package cupper.hj2.mall.infra

import cupper.hj2.mall.models.entity.HashTag
import cupper.hj2.mall.models.entity.Tweet
import cupper.hj2.mall.models.repositories.TweetRepository
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class TweetRepositoryImpl(private val db: JdbcTemplate) : TweetRepository {
    override fun get(id: Int): Tweet? {
        return db.queryForObject(
            """SELECT id, owner_id, content
               FROM tweet
               WHERE id = ?
            """.trimIndent(), DataClassRowMapper<Tweet>(), id)
    }

    override fun create(tweet: Tweet): Tweet {
        db.update(
            "INSERT INTO tweet (owner_id,content) VALUES(?,?)",
            tweet.ownerId, tweet.content)

        val id = db.queryForObject("SELECT LAST_INSERT_ID()", Int::class.java)
        return tweet.copy(id = id!!)
    }

    override fun getMyOwnTweets(userId: Int): List<Tweet> {
        return db.queryForList("""
            |SELECT tweet.id as tw_id, tweet.content as tw_content, hash_tag.id as tag_id, hash_tag.tag as tag
            |FROM tweet, hash_tag
            |WHERE tweet.owner_id = ?
            |  AND hash_tag.tweet_id = tweet.id
        """.trimMargin(), userId).fold(listOf<Tweet>()) { l, rs ->
            if (l.isEmpty()) {
                l + Tweet(
                    id = rs.get("tw_id").toString().toInt(),
                    content = rs.get("tw_content").toString(),
                    hashTags = listOf(HashTag(rs.get("tag_id").toString().toInt(), rs.get("tag").toString())),
                    ownerId = userId
                )
            } else {
                val tweet = l.last()
                if (tweet.id == rs.get("tw_id").toString().toInt()) {
                    val tweet = l.last()
                    val hashTags = tweet.hashTags
                    l.subList(0, l.size - 1) + tweet.copy(hashTags = hashTags!! + HashTag(id = rs.get("tag_id").toString().toInt(), tag = rs.get("tag").toString()))
                } else {
                    l + Tweet(
                        id = rs.get("tw_id").toString().toInt(),
                        content = rs.get("tw_content").toString(),
                        hashTags = listOf(HashTag(rs.get("tag_id").toString().toInt(), rs.get("tag").toString())),
                        ownerId = userId
                    )
                }
            }
        }
    }
}

data class GetOwnTweetRow(
    val tw_id: Int,
    val tw_content: String,
    val tag_id: Int,
    val tag: String
)
