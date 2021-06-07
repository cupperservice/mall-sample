package cupper.hj2.mall.filter

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

@Component
@Order(1)
class AccessLogFilter : Filter {
    val log: Log? = LogFactory.getLog("cupper.hj2.mall.logger")

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        if (request is HttpServletRequest) {
            log?.info("${request.method} ${request.servletPath}")
        }

        chain?.doFilter(request, response)
    }
}
