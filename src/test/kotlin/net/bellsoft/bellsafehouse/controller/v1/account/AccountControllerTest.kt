package net.bellsoft.bellsafehouse.controller.v1.account

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import net.bellsoft.bellsafehouse.controller.WithUser
import net.bellsoft.bellsafehouse.service.AccountService
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@WebMvcTest(AccountController::class)
@WithUser
@ActiveProfiles("test")
class AccountControllerTest(
    private val webContext: WebApplicationContext,
    @MockkBean private val accountService: AccountService,
) : BehaviorSpec(
    {
        val mockMvc: MockMvc = MockMvcBuilders.webAppContextSetup(webContext).apply { springSecurity() }.build()

        Given("익명의 유저로 인증 된 상태에서") {
            When("유저 삭제를 요청했을 때") {
                every { accountService.deleteUser(any()) } throws Exception()

                Then("400 Bad Request 가 발생한다") {
                    mockMvc.delete("/v1/account").andExpect {
                        status { isBadRequest() }
                    }
                }
            }
        }

        Given("정상 유저로 인증 된 상태에서") {
            When("유저 삭제를 요청했을 때") {
                every { accountService.deleteUser(any()) } returns Unit

                Then("200 Ok 가 발생한다") {
                    mockMvc.delete("/v1/account").andExpect {
                        status { isOk() }
                    }
                }
            }
        }
    },
)
