package cupper.hj2.mall.controller

import cupper.hj2.mall.models.entity.Session
import cupper.hj2.mall.models.services.AuthService
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
import javax.servlet.ServletRequest

@Validated
@RestController
@RequestMapping(value = ["/v1/users"])
@Api(tags = ["User"], description = "User authentication endpoint.")
class AuthController(
    private val authService: AuthService
) {
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    private class HttpStatus404: RuntimeException()

    @ApiOperation(
        "ログイン",
        notes = "ログインID、パスワードで認証する。"
    )
    @ApiResponse(code = 200, message = "トークン")
    @PostMapping(value = ["/login"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun login(
        @RequestBody
        request: LoginRequest
    ): LoginResponse {
        val session = authService.login(LoginId(request.login_id), Password(request.password))
        return if (session == null) {
            throw HttpStatus404()
        } else {
            LoginResponse(session.encodedValue)
        }
    }

    @ApiOperation(
        "ログアウト",
        notes = "ログアウトする"
    )
    @ApiResponse(code = 200, message = "成功")
    @PostMapping(value = ["/logout"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun logout(request: ServletRequest): Unit {
        val session = request.getAttribute("cupper.mall.session")
        if (session is Session) {
            authService.logout(session)
        }
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
