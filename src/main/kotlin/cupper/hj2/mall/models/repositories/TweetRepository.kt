package cupper.hj2.mall.models.repositories

import cupper.hj2.mall.models.entity.Tweet

interface TweetRepository {
    fun get(id: Int): Tweet?
    fun create(tweet: Tweet): Tweet
    fun getMyOwnTweets(userId: Int): List<Tweet>
}
