package cupper.hj2.mall.config

import cupper.hj2.mall.interceptor.AuthInterceptor
import cupper.hj2.mall.models.repositories.SessionRepository
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class MailConfig(private val sessionRepository: SessionRepository) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(AuthInterceptor(sessionRepository))
            .addPathPatterns("/v1/**")
            .excludePathPatterns("/v1/users/login")
    }
}
