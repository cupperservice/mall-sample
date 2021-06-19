package cupper.hj2.mall.controller

import cupper.hj2.mall.models.services.LoginService
import cupper.hj2.mall.models.values.LoginId
import cupper.hj2.mall.models.values.Password
import io.swagger.annotations.Api
import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException

@Validated
@RestController
@RequestMapping(value = ["/v1/users"])
@Api(tags = ["User"], description = "User authentication endpoint.")
class UserController(
    private val loginService: LoginService
) {
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    private class HttpStatus404: RuntimeException()

    @ApiOperation(
        "ユーザー認証API",
        notes = "ログインID、パスワードで認証する。"
    )
    @ApiResponse(code = 200, message = "トークン")
    @PostMapping(value = ["/login"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun login(
        @RequestBody
        request: LoginRequest
    ): LoginResponse {
        val session = loginService.login(LoginId(request.login_id), Password(request.password))
        return if (session == null) {
            throw HttpStatus404()
        } else {
            LoginResponse(session.encodedValue)
        }
    }

    @PostMapping(value = ["/logout"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun logout(): String {
        TODO()
    }
}

data class LoginRequest(
    @ApiModelProperty(value = "ユーザーID", example = "sample1@example.com", required = true)
    val login_id: String,
    @ApiModelProperty(value = "パスワード", example = "password00", required = true)
    val password: String
)

data class LoginResponse(
    @ApiModelProperty(value = "トークン", example = "", required = true)
    val token: String
)
