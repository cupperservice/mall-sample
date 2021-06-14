package cupper.hj2.mall.controller

import cupper.hj2.mall.models.services.LoginService
import cupper.hj2.mall.models.values.LoginId
import cupper.hj2.mall.models.values.Password
import jdk.nashorn.internal.codegen.CompilerConstants
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException
import java.util.concurrent.Callable

@Validated
@RestController
@RequestMapping(value = ["/v1/users"])
class UserController(
    private val loginService: LoginService
) {
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    private class HttpStatus404: RuntimeException()

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
    val login_id: String,
    val password: String
)

data class LoginResponse(
    val token: String
)
