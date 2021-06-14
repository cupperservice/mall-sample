package cupper.hj2.mall.controller

import cupper.hj2.mall.models.entity.Session
import cupper.hj2.mall.models.entity.Tweet
import cupper.hj2.mall.models.services.TweetService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/v1/tweet"])
class TweetController(
    private val tweetService: TweetService
) {
    @PostMapping(value = ["/"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(
        @RequestBody request: CreateRequest,
        @RequestAttribute("cupper.mall.session") session: Session
    ): CreateResponse {
        val newTweet = tweetService.create(Tweet(0, 1, request.content, hashTags = listOf()))

        val res = CreateResponse(newTweet.id, newTweet.hashTags().map { CreteResponseHashTag(it.id, it.tag) })
        return res
    }

    @GetMapping(value = ["/"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getMyOwnTweets(): GetMyOwnTweetsResponse {
        TODO()
    }

    @PostMapping(value = ["/find_by_hash_tag"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findTweetsByHashTag(): List<Tweet> {
        TODO()
    }
}

/**
 * Create Response
 */
data class CreateRequest(
    val content: String
)

data class CreateResponse(
    val id: Int,
    val hashTags: List<CreteResponseHashTag>
)

data class CreteResponseHashTag(
    val id: Int,
    val tag: String
)

/**
 * Get Response
 */
data class GetMyOwnTweetsResponse(
    val tweets: List<GetMyOwnTweetsResponseTweet>
)

data class GetMyOwnTweetsResponseTweet(
    val id: Int,
    val content: String,
    val hashTags: List<CreteResponseHashTag>
)

data class GetMyOwnTweetsResponseHashTag(
    val id: Int,
    val tag: String
)
