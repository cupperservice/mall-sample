package cupper.hj2.mall.models.services

import cupper.hj2.mall.models.entity.Tweet
import cupper.hj2.mall.models.entity.User
import cupper.hj2.mall.models.repositories.HashTagRepository
import cupper.hj2.mall.models.repositories.TweetRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TweetService(
    private val tweetRepository: TweetRepository,
    private val hashTagRepository: HashTagRepository) {

    fun get(id: Int): Tweet? {
        return tweetRepository.get(id)
    }

    @Transactional
    fun create(tweet: Tweet): Tweet {
        val tweet = tweetRepository.create(tweet)
        val hashTags = hashTagRepository.create(tweet, tweet.hashTags())

        return tweet.copy(hashTags = hashTags)
    }

    fun getMyOwnTweets(user: User): List<Tweet> {
        return tweetRepository.getMyOwnTweets(user.id)
    }

    fun fincTweetsByHashTag(tag: String): List<Tweet> {
        return tweetRepository.findTweetsByHashTag(tag)
    }
}
