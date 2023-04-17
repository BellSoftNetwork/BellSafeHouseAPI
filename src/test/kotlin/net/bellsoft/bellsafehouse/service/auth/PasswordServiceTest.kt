package net.bellsoft.bellsafehouse.service.auth

import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import net.bellsoft.bellsafehouse.controller.v1.auth.password.dto.PerformResetRequest
import net.bellsoft.bellsafehouse.controller.v1.auth.password.dto.SendResetMailRequest
import net.bellsoft.bellsafehouse.domain.user.User
import net.bellsoft.bellsafehouse.domain.user.UserRepository
import net.bellsoft.bellsafehouse.exception.InvalidTokenException
import net.bellsoft.bellsafehouse.fixture.baseFixture
import net.bellsoft.bellsafehouse.service.common.MailService
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.thymeleaf.context.Context
import java.time.LocalDateTime
import java.util.UUID

@SpringBootTest
@ActiveProfiles("test")
class PasswordServiceTest(
    private val passwordService: PasswordService,
    @MockkBean private val mailService: MailService,
    @MockkBean private val userRepository: UserRepository,
) : BehaviorSpec(
    {
        Given("비밀번호 초기화 메일을 요청할 때") {
            val email = "bell@bellsoft.net"

            When("정상적으로 가입된 계정으로 요청하면") {
                val user: User = baseFixture()

                every { mailService.sendMail(any(), any(), any(), any()) } returns Unit
                every { mailService.buildContext(any()) } returns Context()
                every { userRepository.findByEmail(any()) } returns user
                every { userRepository.save(user) } returns user

                Then("메일이 정상적으로 발송된다") {
                    shouldNotThrowAny {
                        passwordService.sendReset(SendResetMailRequest(email))
                    }
                    verify(exactly = 1) { mailService.sendMail(any(), any(), any(), any()) }
                }
            }

            When("가입되지 않은 계정으로 요청하면") {
                every { userRepository.findByEmail(any()) } returns null

                Then("메일이 발송되지 않는다") {
                    shouldNotThrowAny {
                        passwordService.sendReset(SendResetMailRequest(email))
                    }
                    verify(exactly = 0) { mailService.sendMail(any(), any(), any(), any()) }
                }
            }
        }

        Given("비밀번호 초기화 검증을 요청할 때") {
            val token = UUID.randomUUID()
            val tokenString = token.toString()

            When("정상적으로 발급된 토큰으로 요청하면") {
                val user: User = baseFixture {
                    property(User::resetToken) { token }
                    property(User::resetTokenCreatedAt) { LocalDateTime.now() }
                }

                every { userRepository.findByResetToken(any()) } returns user

                Then("정상적으로 검증된다") {
                    shouldNotThrowAny {
                        passwordService.validateResetToken(tokenString) shouldBe user
                    }
                }
            }

            When("기간이 만료 된 토큰으로 요청하면") {
                val user: User = baseFixture {
                    property(User::resetToken) { token }
                    property(User::resetTokenCreatedAt) { LocalDateTime.MIN }
                }

                every { userRepository.findByResetToken(any()) } returns user

                Then("InvalidTokenException 이 발생한다") {
                    shouldThrow<InvalidTokenException> {
                        passwordService.validateResetToken(tokenString)
                    }
                }
            }

            When("존재하지 않는 토큰으로 요청하면") {
                every { userRepository.findByResetToken(any()) } returns null

                Then("InvalidTokenException 이 발생한다") {
                    shouldThrow<InvalidTokenException> {
                        passwordService.validateResetToken(tokenString)
                    }
                }
            }
        }

        Given("비밀번호 초기화 수행을 요청할 때") {
            val oldPassword = "oldPassword"
            val newPassword = "newPassword"
            val passwordEncoder = BCryptPasswordEncoder()
            val token = UUID.randomUUID()
            val tokenString = token.toString()

            When("정상적으로 발급된 토큰과 비밀번호로 요청하면") {
                val user = baseFixture<User> {
                    property(User::resetToken) { token }
                    property(User::resetTokenCreatedAt) { LocalDateTime.now() }
                    property(User::setPassword) { oldPassword }
                }

                val slot = slot<User>()

                every { userRepository.findByResetToken(any()) } returns user
                every { userRepository.save(capture(slot)) } returns user

                Then("정상적으로 새 비밀번호로 변경되고 토큰이 초기화된다") {
                    shouldNotThrowAny {
                        passwordService.performReset(PerformResetRequest(tokenString, newPassword))
                    }
                    passwordEncoder.matches(newPassword, user.password) shouldBe true
                    slot.isCaptured shouldBe true
                    slot.captured.resetToken shouldBe null
                }
            }

            When("존재하지 않는 토큰으로 요청하면") {
                every { userRepository.findByResetToken(any()) } throws InvalidTokenException()

                Then("InvalidTokenException 이 발생한다") {
                    shouldThrow<InvalidTokenException> {
                        passwordService.performReset(PerformResetRequest(tokenString, newPassword))
                    }
                }
            }
        }
    },
)
