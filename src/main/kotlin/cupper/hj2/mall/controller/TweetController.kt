package cupper.hj2.mall.controller

import cupper.hj2.mall.models.entity.Session
import cupper.hj2.mall.models.entity.Tweet
import cupper.hj2.mall.models.services.TweetService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException
import javax.servlet.ServletRequest

@RestController
@RequestMapping(value = ["/v1/tweet"])
class TweetController(
    private val tweetService: TweetService
) {
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    private class HttpStatus401: RuntimeException()

    @PostMapping(value = ["/"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(
        @RequestBody request: CreateRequest,
        @RequestAttribute("cupper.mall.session") session: Session
    ): CreateResponse {
        val newTweet = tweetService.create(Tweet(0, 1, request.content, hashTags = listOf()))

        return CreateResponse(newTweet.id, newTweet.hashTags().map { CreteResponseHashTag(it.id, it.tag) })
    }

    @GetMapping(value = ["/"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getMyOwnTweets(request: ServletRequest): GetMyOwnTweetsResponse {
        val session = request.getAttribute("cupper.mall.session")
        if (session is Session) {
            return GetMyOwnTweetsResponse(tweetService.getMyOwnTweets(session.user).map {
                GetMyOwnTweetsResponseTweet(
                    id = it.id,
                    content = it.content,
                    hashTags = it.hashTags?.map {
                        GetMyOwnTweetsResponseHashTag(
                            id = it.id,
                            tag = it.tag
                        )
                    }
                )
            })
        } else {
            throw HttpStatus401()
        }
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
    val hashTags: List<GetMyOwnTweetsResponseHashTag>?
)

data class GetMyOwnTweetsResponseHashTag(
    val id: Int,
    val tag: String
)
