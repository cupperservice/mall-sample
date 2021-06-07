package cupper.hj2.mall.filter

import org.springframework.core.annotation.Order
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

import org.springframework.stereotype.Component

@Component
@Order(2)
class AuthFilter : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        println("Called AuthFilter")
        chain?.doFilter(request, response)
    }
}
