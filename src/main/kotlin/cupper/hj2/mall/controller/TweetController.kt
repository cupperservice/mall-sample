package cupper.hj2.mall.controller

import cupper.hj2.mall.models.entity.Session
import cupper.hj2.mall.models.entity.Tweet
import cupper.hj2.mall.models.services.TweetService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException
import javax.servlet.ServletRequest

@RestController
@RequestMapping(value = ["/v1/tweet"])
@Api(tags = ["Tweet"], description = "Tweet endpoints.")
class TweetController(
    private val tweetService: TweetService
) {
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    private class HttpStatus401: RuntimeException()

    @ApiOperation(
        "発話API",
        notes = "tweetする。"
    )
    @ApiResponse(code = 200, message = "Tweet成功")
    @PostMapping(value = ["/"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(
        @RequestBody request: CreateRequest,
        @RequestAttribute("cupper.mall.session") session: Session
    ): CreateResponse {
        val newTweet = tweetService.create(Tweet(0, 1, request.content, hashTags = listOf()))

        return CreateResponse(newTweet.id, newTweet.hashTags().map { CreteResponseHashTag(it.id, it.tag) })
    }

    @ApiOperation(
        "Tweet取得API",
        notes = "自分が発話したTweetを取得する"
    )
    @ApiResponse(code = 200, message = "成功")
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

    @ApiOperation(
        "Tweet検索API",
        notes = "ハッシュタグでTweetを検索する"
    )
    @ApiResponse(code = 200, message = "成功")
    @PostMapping(value = ["/find_by_hash_tag"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findTweetsByHashTag(
        @RequestBody
        request: FindTweetsByHashTagRequest
    ): FindTweetsByHashTagResponse {
        return FindTweetsByHashTagResponse(
            tweetService.findTweetsByHashTag(request.tag).map {
                FindTweetsByHashTagResponseTweet(
                    id = it.id,
                    ownerId = it.ownerId,
                    content = it.content,
                    hashTags = it.hashTags?.map {
                        FindTweetsByHashTagResponseHashTag(
                            id = it.id,
                            tag = it.tag
                        )
                    }
                )
            }
        )
    }
}

/**
 * Create Response
 */
data class CreateRequest(
    @ApiModelProperty(value = "Tweet", example = "静岡駅 Now! #shizuoka", required = true)
    val content: String
)

data class CreateResponse(
    @ApiModelProperty(value = "Tweet ID", example = "123", required = true)
    val id: Int,
    val hashTags: List<CreteResponseHashTag>
)

data class CreteResponseHashTag(
    @ApiModelProperty(value = "Hash tag ID", example = "123", required = true)
    val id: Int,
    @ApiModelProperty(value = "Hash tag", example = "#shizuoka", required = true)
    val tag: String
)

/**
 * Get Response
 */
data class GetMyOwnTweetsResponse(
    val tweets: List<GetMyOwnTweetsResponseTweet>
)

data class GetMyOwnTweetsResponseTweet(
    @ApiModelProperty(value = "Tweet ID", example = "123", required = true)
    val id: Int,
    @ApiModelProperty(value = "Tweet", example = "静岡駅 Now #shizuoka", required = true)
    val content: String,
    val hashTags: List<GetMyOwnTweetsResponseHashTag>?
)

data class GetMyOwnTweetsResponseHashTag(
    @ApiModelProperty(value = "Hash tag ID", example = "123", required = true)
    val id: Int,
    @ApiModelProperty(value = "Hash tag", example = "#shizuoka", required = true)
    val tag: String
)

/**
 * Find tweets by hash tag
 */
data class FindTweetsByHashTagRequest(
    @ApiModelProperty(value = "Hash tag", example = "shizuoka", required = true)
    val tag: String
)

data class FindTweetsByHashTagResponse(
    val tweets: List<FindTweetsByHashTagResponseTweet>
)

data class FindTweetsByHashTagResponseTweet(
    @ApiModelProperty(value = "Tweet ID", example = "123", required = true)
    val id: Int,
    @ApiModelProperty(value = "User ID", example = "123", required = true)
    val ownerId: Int,
    @ApiModelProperty(value = "Tweet", example = "静岡駅 Now #shizuoka", required = true)
    val content: String,
    val hashTags: List<FindTweetsByHashTagResponseHashTag>?
)

data class FindTweetsByHashTagResponseHashTag(
    @ApiModelProperty(value = "Hash tag ID", example = "123", required = true)
    val id: Int,
    @ApiModelProperty(value = "Hash tag", example = "#shizuoka", required = true)
    val tag: String
)
