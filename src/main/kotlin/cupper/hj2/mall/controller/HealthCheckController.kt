package cupper.hj2.mall.controller

import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/health"])
class HealthCheckController {

    @ApiOperation(
        value = "ヘルスチェックAPI",
        notes = "ヘルスチェックの結果を返す"
    )
    @ApiResponse(code = 200, message = "ヘルスチェック成功")
    @GetMapping(value = ["/check"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun check(): CheckResponse {
        return CheckResponse(result = "Ok")
    }
}

data class CheckResponse(
    @ApiModelProperty(value = "The result of health check", example = "Ok", required = true)
    val result: String
)
