package cupper.hj2.mall.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        // Basic認証を無効化し、デフォルトのレスポンスヘッダーのみを使用するように設定する。
        http?.authorizeRequests()?.anyRequest()?.permitAll()
        // csrfを無効化し、全てのHTTPメソッドを使用できるように設定。
        http?.csrf()?.disable()
    }
}
