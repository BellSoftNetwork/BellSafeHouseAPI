package net.bellsoft.bellsafehouse.controller.v1.admin

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.string.shouldStartWith
import net.bellsoft.bellsafehouse.controller.WithUser
import net.bellsoft.bellsafehouse.domain.user.UserRole
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@WebMvcTest(AdminController::class)
@EnableMethodSecurity(securedEnabled = true)
@WithUser(userId = "admin", role = UserRole.ADMIN)
@ActiveProfiles("test")
class AdminControllerTest(
    private val webContext: WebApplicationContext,
) : BehaviorSpec(
    {
        val mockMvc: MockMvc = MockMvcBuilders.webAppContextSetup(webContext).apply { springSecurity() }.build()

        Given("어드민 역할을 가진 유저로 인증 된 상태에서") {
            When("어드민 페이지에 접근했을 때") {
                Then("200 Ok 가 발생한다") {
                    mockMvc.get("/v1/admin").andExpect {
                        status { isOk() }
                    }.andReturn().response.contentAsString shouldStartWith "Admin Page"
                }
            }
        }
    },
)
