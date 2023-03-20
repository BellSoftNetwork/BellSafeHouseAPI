package net.bellsoft.bellsafehouse.controller.v1.account

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import net.bellsoft.bellsafehouse.controller.WithUser
import net.bellsoft.bellsafehouse.controller.v1.account.dto.AccountEditRequest
import net.bellsoft.bellsafehouse.controller.v1.account.dto.AccountInfoResponse
import net.bellsoft.bellsafehouse.domain.user.User
import net.bellsoft.bellsafehouse.fixture.baseNullFixture
import net.bellsoft.bellsafehouse.service.AccountService
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@WebMvcTest(AccountController::class)
@WithUser
@ActiveProfiles("test")
class AccountControllerTest(
    private val webContext: WebApplicationContext,
    @MockkBean private val accountService: AccountService,
) : BehaviorSpec(
    {
        val fixture = baseNullFixture
        val mockMvc: MockMvc = MockMvcBuilders.webAppContextSetup(webContext).apply { springSecurity() }.build()
        val objectMapper = jacksonObjectMapper()

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

            When("빈 유저 수정 정보 요청을 보냈을 때") {
                val accountEditRequest: AccountEditRequest = fixture {
                    property(AccountEditRequest::password) { null }
                    property(AccountEditRequest::email) { null }
                    property(AccountEditRequest::nickname) { null }
                    property(AccountEditRequest::marketingAgreed) { null }
                }
                val editData = objectMapper.writeValueAsString(accountEditRequest)

                every { accountService.update(any(), any()) } returns Unit

                Then("400 Bad Request 가 반환된다") {
                    mockMvc.patch("/v1/account") {
                        locale(Locale.KOREA)
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        characterEncoding = "UTF-8"
                        content = editData
                    }.andExpect {
                        status { isBadRequest() }
                    }
                }
            }

            When("제약 조건을 위반하는 수정 정보 요청을 보냈을 때") {
                val accountEditRequest: AccountEditRequest = fixture {
                    property(AccountEditRequest::password) { "toooooooooooLongPasswordToChange" }
                    property(AccountEditRequest::email) { null }
                    property(AccountEditRequest::nickname) { null }
                    property(AccountEditRequest::marketingAgreed) { null }
                }
                val editData = objectMapper.writeValueAsString(accountEditRequest)

                every { accountService.update(any(), any()) } returns Unit

                Then("400 Bad Request 가 반환된다") {
                    mockMvc.patch("/v1/account") {
                        locale(Locale.KOREA)
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        characterEncoding = "UTF-8"
                        content = editData
                    }.andExpect {
                        status { isBadRequest() }
                    }
                }
            }

            When("정상적인 유저 수정 정보 요청을 보냈을 때") {
                val accountEditRequest: AccountEditRequest = fixture {
                    property(AccountEditRequest::password) { "passwordToChange" }
                    property(AccountEditRequest::email) { "emailToChange@bellsoft.net" }
                    property(AccountEditRequest::nickname) { null }
                    property(AccountEditRequest::marketingAgreed) { null }
                }
                val editData = objectMapper.writeValueAsString(accountEditRequest)

                every { accountService.update(any(), any()) } returns Unit

                Then("200 Ok 가 반환된다") {
                    mockMvc.patch("/v1/account") {
                        locale(Locale.KOREA)
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        characterEncoding = "UTF-8"
                        content = editData
                    }.andExpect {
                        status { isOk() }
                    }
                }
            }

            When("유저 정보 조회를 요청했을 때") {
                val user = User("userId", "password", "user@email.com", "userNickname", true)
                val dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss")

                every { accountService.getInfo(any()) } returns AccountInfoResponse.of(user)

                Then("200 Ok 가 발생한다") {
                    mockMvc.get("/v1/account").andExpect {
                        status { isOk() }
                        jsonPath("$.userId") { value(user.userId) }
                        jsonPath("$.email") { value(user.email) }
                        jsonPath("$.nickname") { value(user.nickname) }
                        jsonPath("$.marketingAgreed") { value(true) }
                        jsonPath("$.createdAt") { value(LocalDateTime.MIN.format(dateTimeFormatter)) }
                    }
                }
            }
        }
    },
)
