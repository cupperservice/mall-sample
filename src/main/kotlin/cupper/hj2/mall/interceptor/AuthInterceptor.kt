package cupper.hj2.mall.interceptor

import cupper.hj2.mall.models.repositories.SessionRepository
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.websocket.Session

class AuthInterceptor(private val sessionRepository: SessionRepository) : HandlerInterceptor{
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val authHeader: String? = request.getHeader(HttpHeaders.AUTHORIZATION)
        return if (authHeader == null) {
            response.status = HttpStatus.UNAUTHORIZED.value()
            false
        } else {
            val keys = authHeader.split(" ")
            val session = sessionRepository.get(keys[1])
            if (session == null) {
                response.status = HttpStatus.UNAUTHORIZED.value()
                false
            } else {
                request.setAttribute("cupper.mall.session", session)
                true
            }
        }
    }
}
