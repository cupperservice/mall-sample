package cupper.hj2.mall.models.repositories

import cupper.hj2.mall.models.entity.HashTag
import cupper.hj2.mall.models.entity.Tweet

interface HashTagRepository {
    fun create(tweet: Tweet, hashTags: List<HashTag>): List<HashTag>
}
