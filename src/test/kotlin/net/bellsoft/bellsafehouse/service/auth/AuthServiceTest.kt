package net.bellsoft.bellsafehouse.service.auth

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bellsoft.bellsafehouse.controller.v1.auth.dto.UserLoginRequest
import net.bellsoft.bellsafehouse.controller.v1.auth.dto.UserRegistrationRequest
import net.bellsoft.bellsafehouse.exception.InvalidTokenException
import net.bellsoft.bellsafehouse.exception.PasswordMismatchException
import net.bellsoft.bellsafehouse.exception.UserNotFoundException
import net.bellsoft.bellsafehouse.fixture.baseFixture
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
internal class AuthServiceTest(
    private val authService: AuthService,
) : BehaviorSpec(
    {
        val fixture = baseFixture.new {
            property(UserRegistrationRequest::password) { "password" }
            property(UserRegistrationRequest::marketingAgreed) { true }
        }

        Given("가입한 사용자가 없는 상황에서") {
            When("신규 회원 가입 시도 시") {
                val userRegistrationRequest: UserRegistrationRequest = fixture {
                    property(UserRegistrationRequest::userId) { "userId" }
                    property(UserRegistrationRequest::nickname) { "userNick" }
                }
                val user = authService.register(userRegistrationRequest)

                Then("정상적으로 가입된다") {
                    user.userId shouldBe userRegistrationRequest.userId
                }
            }

            When("존재하지 않는 계정 아이디로 유저 로드 시도 시") {
                val userId = "NOT_EXISTS_USER_ID"

                Then("유저 로드에 실패한다") {
                    shouldThrow<UsernameNotFoundException> {
                        authService.loadUserByUsername(userId)
                    }.message shouldBe "$userId 은 존재하지 않는 사용자입니다"
                }
            }

            When("존재하지 않는 계정 아이디로 로그인 시도 시") {
                val userId = "NOT_EXISTS_USER_ID"
                val userLoginRequest: UserLoginRequest = fixture {
                    property(UserLoginRequest::userId) { userId }
                }

                Then("로그인에 실패한다") {
                    shouldThrow<UserNotFoundException> {
                        authService.login(userLoginRequest)
                    }.message shouldBe "$userId 는 존재하지 않는 아이디입니다"
                }
            }

            When("존재하지 않는 RefreshToken 으로 AccessToken 재발급 시도 시") {
                val refreshToken = "NOT_EXISTS_REFRESH_TOKEN"

                Then("재발급에 실패한다") {
                    shouldThrow<InvalidTokenException> {
                        authService.reissueAccessToken(refreshToken)
                    }.message shouldBe "유효한 인증 토큰 형식이 아님"
                }
            }
        }

        Given("기존에 가입한 사용자가 있는 상황에서") {
            val userRegistrationRequest: UserRegistrationRequest = fixture {
                property(UserRegistrationRequest::userId) { "existsUserId" }
                property(UserRegistrationRequest::nickname) { "existsUserNick" }
            }
            val user = authService.register(userRegistrationRequest)

            When("기존 사용자 ID 와 동일한 ID 로 가입 요청 시") {
                Then("가입이 거부된다") {
                    shouldThrow<DataIntegrityViolationException> {
                        authService.register(userRegistrationRequest)
                    }
                }
            }

            When("기존 사용자 ID 와 다른 ID 로 가입 요청 시") {
                val newUserRegistrationRequest: UserRegistrationRequest = fixture {
                    property(UserRegistrationRequest::userId) { "newUserId" }
                    property(UserRegistrationRequest::nickname) { "newUserNick" }
                }
                val newUser = authService.register(newUserRegistrationRequest)

                Then("정상적으로 가입된다") {
                    newUser.userId shouldBe newUserRegistrationRequest.userId
                }
            }

            When("존재하는 계정 ID 에 잘못된 비밀번호로 로그인 시도 시") {
                val userLoginRequest: UserLoginRequest = fixture {
                    property(UserLoginRequest::userId) { user.userId }
                    property(UserLoginRequest::password) { "InvalidPassword" }
                }

                Then("로그인에 실패한다") {
                    shouldThrow<PasswordMismatchException> {
                        authService.login(userLoginRequest)
                    }.message shouldBe "비밀번호가 일치하지 않습니다"
                }
            }

            When("유효한 계정 아이디로 유저 로드 시도 시") {
                val userId = userRegistrationRequest.userId

                Then("유저 엔티티가 정상적으로 로드된다") {
                    authService.loadUserByUsername(userId).username shouldBe userId
                }
            }

            When("유효한 계정 정보로 로그인 시도 시") {
                val userLoginRequest: UserLoginRequest = fixture {
                    property(UserLoginRequest::userId) { userRegistrationRequest.userId }
                    property(UserLoginRequest::password) { userRegistrationRequest.password }
                }

                Then("RefreshToken 과 AccessToken 이 정상적으로 발급된다") {
                    shouldNotThrowAny {
                        authService.login(userLoginRequest)
                    }
                }
            }

            When("정상적인 RefreshToken 으로 AccessToken 재발급 요청시") {
                val loginUserDto = authService.login(
                    fixture {
                        property(UserLoginRequest::userId) { userRegistrationRequest.userId }
                        property(UserLoginRequest::password) { userRegistrationRequest.password }
                    },
                )

                Then("정상적으로 발급된다") {
                    shouldNotThrowAny {
                        authService.reissueAccessToken(loginUserDto.refreshTokenCookie.value)
                    }
                }
            }
        }
    },
)
