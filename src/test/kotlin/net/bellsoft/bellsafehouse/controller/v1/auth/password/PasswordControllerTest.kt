package net.bellsoft.bellsafehouse.controller.v1.auth.password

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import net.bellsoft.bellsafehouse.controller.v1.auth.password.dto.PerformResetRequest
import net.bellsoft.bellsafehouse.controller.v1.auth.password.dto.SendResetMailRequest
import net.bellsoft.bellsafehouse.exception.InvalidTokenException
import net.bellsoft.bellsafehouse.fixture.baseNullFixture
import net.bellsoft.bellsafehouse.service.auth.PasswordService
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.util.Locale

@WebMvcTest(PasswordController::class)
@WithAnonymousUser
@ActiveProfiles("test")
class PasswordControllerTest(
    private val webContext: WebApplicationContext,
    @MockkBean private val passwordService: PasswordService,
) : BehaviorSpec(
    {
        val fixture = baseNullFixture
        val mockMvc: MockMvc = MockMvcBuilders.webAppContextSetup(webContext).apply { springSecurity() }.build()
        val objectMapper = jacksonObjectMapper()

        Given("비밀번호 초기화 메일 발송을 요청했을 때") {
            When("정상적인 입력 값으로 요청하면") {
                val sendResetMailRequest: SendResetMailRequest = fixture {
                    property(SendResetMailRequest::email) { "bell@bellsoft.net" }
                }

                val postData = objectMapper.writeValueAsString(sendResetMailRequest)

                every { passwordService.sendReset(any()) } returns Unit

                Then("200 Ok 가 발생한다") {
                    mockMvc.post("/v1/auth/password/reset") {
                        locale(Locale.KOREA)
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        characterEncoding = "UTF-8"
                        content = postData
                    }.andExpect {
                        status { isOk() }
                    }
                }
            }

            When("제약 조건을 위반하는 이메일로 요청하면") {
                val sendResetMailRequest: SendResetMailRequest = fixture {
                    property(SendResetMailRequest::email) { "bruh-this-is-not-valid-email" }
                }

                every { passwordService.sendReset(any()) } returns Unit

                val postData = objectMapper.writeValueAsString(sendResetMailRequest)

                Then("400 Bad Request 가 발생한다") {
                    mockMvc.post("/v1/auth/password/reset") {
                        locale(Locale.KOREA)
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        characterEncoding = "UTF-8"
                        content = postData
                    }.andExpect {
                        status { isBadRequest() }
                    }
                }
            }

            When("요청이 내부 서비스에서 예외를 발생시키는 경우") {
                val sendResetMailRequest: SendResetMailRequest = fixture {
                    property(SendResetMailRequest::email) { "bell@incorrectdomain.net" }
                }

                every { passwordService.sendReset(any()) } throws Throwable()

                val postData = objectMapper.writeValueAsString(sendResetMailRequest)

                Then("400 Bad Request 가 발생한다") {
                    mockMvc.post("/v1/auth/password/reset") {
                        locale(Locale.KOREA)
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        characterEncoding = "UTF-8"
                        content = postData
                    }.andExpect {
                        status { isBadRequest() }
                    }
                }
            }
        }

        Given("비밀번호 초기화 토큰 검증을 요청했을 때") {
            When("정상적인 입력 값으로 요청하면") {
                val sampleUuid: String = "01234567-89ab-cdef-0123-456789abcdef"

                every { passwordService.validateResetToken(any()) } returns fixture()

                Then("200 Ok 가 발생한다") {
                    mockMvc.get("/v1/auth/password/reset") {
                        locale(Locale.KOREA)
                        param("resetToken", sampleUuid)
                    }.andExpect {
                        status { isOk() }
                    }
                }
            }

            When("제약 조건을 위반하는 토큰으로 요청하면") {
                val sampleUuid: String = "01234567-89ab-cdef-0123-456789abcdef-but-too-long"

                every { passwordService.validateResetToken(any()) } returns fixture()

                Then("400 Bad Request 가 발생한다") {
                    mockMvc.get("/v1/auth/password/reset") {
                        locale(Locale.KOREA)
                        param("resetToken", sampleUuid)
                    }.andExpect {
                        status { isBadRequest() }
                    }
                }
            }

            When("요청이 내부 서비스에서 예외를 발생시키는 경우") {
                val sampleUuid: String = "01234567-89ab-cdef-0123-456789abcdef"

                every { passwordService.validateResetToken(any()) } throws InvalidTokenException()

                Then("400 Bad Request 가 발생한다") {
                    mockMvc.get("/v1/auth/password/reset") {
                        locale(Locale.KOREA)
                        param("resetToken", sampleUuid)
                    }.andExpect {
                        status { isBadRequest() }
                    }
                }
            }
        }

        Given("비밀번호 초기화 수행을 요청했을 때") {
            When("정상적인 입력 값으로 요청하면") {
                val performResetRequest: PerformResetRequest = fixture {
                    property(PerformResetRequest::resetToken) { "01234567-89ab-cdef-0123-456789abcdef" }
                    property(PerformResetRequest::newPassword) { "newPassword" }
                }

                val postData = objectMapper.writeValueAsString(performResetRequest)

                every { passwordService.performReset(any()) } returns Unit

                Then("200 Ok 가 발생한다") {
                    mockMvc.put("/v1/auth/password") {
                        locale(Locale.KOREA)
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        characterEncoding = "UTF-8"
                        content = postData
                    }.andExpect {
                        status { isOk() }
                    }
                }
            }

            When("제약 조건을 위반하는 내용으로 요청하면") {
                val performResetRequest: PerformResetRequest = fixture {
                    property(PerformResetRequest::resetToken) { "01234567-89ab-cdef-0123-456789abcdef-but-too-long" }
                    property(PerformResetRequest::newPassword) { "newPasswordbutToooooooooooooooLong" }
                }

                val postData = objectMapper.writeValueAsString(performResetRequest)

                every { passwordService.performReset(any()) } returns Unit

                Then("400 Bad Request 가 발생한다") {
                    mockMvc.put("/v1/auth/password") {
                        locale(Locale.KOREA)
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        characterEncoding = "UTF-8"
                        content = postData
                    }.andExpect {
                        status { isBadRequest() }
                    }
                }
            }

            When("요청이 내부 서비스에서 예외를 발생시키는 경우") {
                val performResetRequest: PerformResetRequest = fixture {
                    property(PerformResetRequest::resetToken) { "01234567-89ab-cdef-0123-456789abcdef-but-too-long" }
                    property(PerformResetRequest::newPassword) { "newPasswordbutToooooooooooooooLong" }
                }

                val postData = objectMapper.writeValueAsString(performResetRequest)

                every { passwordService.performReset(any()) } throws InvalidTokenException()

                Then("400 Bad Request 가 발생한다") {
                    mockMvc.put("/v1/auth/password") {
                        locale(Locale.KOREA)
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        characterEncoding = "UTF-8"
                        content = postData
                    }.andExpect {
                        status { isBadRequest() }
                    }
                }
            }
        }
    },
)
