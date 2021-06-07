package cupper.hj2.mall.controllers.userController

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@EnableAutoConfiguration
@Transactional
class LoginTests {
    val TARGET_URL = "/v1/users/login"
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var context: WebApplicationContext

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
//            .apply<DefaultMockMvcBuilder>()
            .build()
    }

    @Test
    @Sql("classpath:sql/user/init.sql")
    fun loginOK() {
        mockMvc.perform(MockMvcRequestBuilders
            .post(TARGET_URL)
            .content("""{
                |"login_id":"sample1@example.com",
                |"password":"password00"
                |}""".trimMargin())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .characterEncoding("utf-8")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json("""
                |{
                |"token":"token00"
                |}
            """.trimMargin(), true))
    }
}
