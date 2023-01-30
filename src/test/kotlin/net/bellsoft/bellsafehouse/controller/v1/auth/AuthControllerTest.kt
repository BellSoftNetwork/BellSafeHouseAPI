package net.bellsoft.bellsafehouse.controller.v1.auth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.email
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.azstring
import io.mockk.every
import net.bellsoft.bellsafehouse.component.jwt.JwtSupport
import net.bellsoft.bellsafehouse.controller.v1.auth.dto.UserLoginRequest
import net.bellsoft.bellsafehouse.controller.v1.auth.dto.UserRegistrationRequest
import net.bellsoft.bellsafehouse.exception.InvalidTokenException
import net.bellsoft.bellsafehouse.exception.PasswordMismatchException
import net.bellsoft.bellsafehouse.exception.UnprocessableEntityException
import net.bellsoft.bellsafehouse.fixture.baseFixture
import net.bellsoft.bellsafehouse.service.AuthService
import net.bellsoft.bellsafehouse.service.dto.UserAuthToken
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.util.Locale
import javax.servlet.http.Cookie

@WebMvcTest(AuthController::class)
@WithAnonymousUser
@ActiveProfiles("test")
internal class AuthControllerTest(
    private val webContext: WebApplicationContext,
    @MockkBean private val authService: AuthService,
    @MockkBean private val jwtSupport: JwtSupport,
) : BehaviorSpec(
    {
        val mockMvc: MockMvc = MockMvcBuilders.webAppContextSetup(webContext).apply { springSecurity() }.build()
        val objectMapper = jacksonObjectMapper()
        val fixture = baseFixture.new {
            property(UserRegistrationRequest::password) { "password" }
            property(UserRegistrationRequest::marketingAgreedAt) { true }
        }

        Given("처음 접속한 상황에서") {
            When("정상적인 데이터로 회원 가입 요청 시") {
                checkAll(
                    10,
                    Arb.string(4..20),
                    Arb.string(8..20),
                    Arb.email(),
                ) { userId, password, email ->
                    val userRegistrationRequest: UserRegistrationRequest = fixture {
                        property(UserRegistrationRequest::userId) { userId }
                        property(UserRegistrationRequest::nickname) { password }
                        property(UserRegistrationRequest::email) { email }
                    }

                    every { authService.register(userRegistrationRequest) } returns fixture()

                    Then("201 Created 가 반환된다") {
                        val userRegistrationJson = objectMapper.writeValueAsString(userRegistrationRequest)

                        mockMvc.post("/api/v1/auth/register") {
                            locale(Locale.KOREA)
                            contentType = MediaType.APPLICATION_JSON
                            accept = MediaType.APPLICATION_JSON
                            content = userRegistrationJson
                        }.andExpect {
                            status { isCreated() }
                        }
                    }
                }
            }

            When("비정상적인 데이터로 회원 가입 요청 시") {
                checkAll(
                    10,
                    Arb.choice(Arb.string(3), Arb.string(21)),
                    Arb.choice(Arb.string(7), Arb.string(21)),
                    Exhaustive.azstring(5..10),
                ) { userId, password, email ->
                    val userRegistrationRequest: UserRegistrationRequest = fixture {
                        property(UserRegistrationRequest::userId) { userId }
                        property(UserRegistrationRequest::nickname) { password }
                        property(UserRegistrationRequest::email) { email }
                    }

                    every { authService.register(userRegistrationRequest) } returns fixture()

                    Then("400 Bad Request 가 반환된다") {
                        val userRegistrationJson = objectMapper.writeValueAsString(userRegistrationRequest)

                        mockMvc.post("/api/v1/auth/register") {
                            locale(Locale.KOREA)
                            contentType = MediaType.APPLICATION_JSON
                            accept = MediaType.APPLICATION_JSON
                            content = userRegistrationJson
                        }.andExpect {
                            status { isBadRequest() }
                        }
                    }
                }
            }

            When("회원 가입에 필요한 데이터를 채우지 않고 가입 요청 시") {
                Then("400 Bad Request 가 반환된다") {
                    mockMvc.post("/api/v1/auth/register") {
                        locale(Locale.KOREA)
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        content = ""
                    }.andExpect {
                        status { isBadRequest() }
                    }
                }
            }
        }

        Given("기존에 가입한 이력이 있는 상황에서") {
            val duplicatedUserRequest: UserRegistrationRequest = fixture()

            every { authService.register(duplicatedUserRequest) } throws UnprocessableEntityException("중복")

            When("중복된 데이터로 회원 가입 요청 시") {
                Then("422 Unprocessable Entity 가 반환된다") {
                    val userRegistrationJson = objectMapper.writeValueAsString(duplicatedUserRequest)

                    mockMvc.post("/api/v1/auth/register") {
                        locale(Locale.KOREA)
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        content = userRegistrationJson
                    }.andExpect {
                        status { isUnprocessableEntity() }
                    }
                }
            }

            When("정상적인 계정으로 로그인 시도 시") {
                Then("쿠키에 신규 Refresh Token 이 등록되고 응답에 Access Token 이 반환된다") {
                    val userLoginRequest: UserLoginRequest = fixture()
                    val userAuthToken: UserAuthToken = fixture {
                        property(UserAuthToken::refreshTokenCookie) { Cookie("refreshToken", "REAL_TOKEN") }
                    }

                    every { authService.login(userLoginRequest) } returns userAuthToken

                    val userLogin = objectMapper.writeValueAsString(userLoginRequest)

                    mockMvc.post("/api/v1/auth/refresh") {
                        locale(Locale.KOREA)
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        content = userLogin
                    }.andExpect {
                        status { isOk() }
                        cookie { value("refreshToken", userAuthToken.refreshTokenCookie.value) }
                        jsonPath("$.accessToken") { value(userAuthToken.accessToken.credentials) }
                    }
                }
            }

            When("존재하는 계정 아이디에 잘못된 비밀번호로 로그인 시도 시") {
                Then("400 Bad Request 가 반환된다") {
                    val userLoginRequest: UserLoginRequest = fixture()

                    every {
                        authService.login(userLoginRequest)
                    } throws PasswordMismatchException("비밀번호가 일치하지 않습니다")

                    val userLogin = objectMapper.writeValueAsString(userLoginRequest)

                    mockMvc.post("/api/v1/auth/refresh") {
                        locale(Locale.KOREA)
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        content = userLogin
                    }.andExpect {
                        status { isBadRequest() }
                    }
                }
            }

            When("Refresh Token 값이 쿠키에 없을 때") {
                Then("401 Unauthorized 가 반환된다") {
                    mockMvc.post("/api/v1/auth/access") {
                        locale(Locale.KOREA)
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                    }.andExpect {
                        status { isUnauthorized() }
                    }
                }
            }

            When("잘못된 Refresh Token 값이 쿠키에 담겨있을 때") {
                Then("401 Unauthorized 가 반환된다") {
                    val invalidRefreshToken: String = fixture()

                    every {
                        authService.reissueAccessToken(invalidRefreshToken)
                    } throws InvalidTokenException("유효한 인증 토큰 형식이 아님")

                    mockMvc.post("/api/v1/auth/access") {
                        locale(Locale.KOREA)
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        cookie(Cookie("refreshToken", invalidRefreshToken))
                    }.andExpect {
                        status { isUnauthorized() }
                    }
                }
            }

            When("정상적인 Refresh Token 값이 쿠키에 담겨있을 때") {
                Then("응답에 Access Token 이 정상적으로 반환된다") {
                    val validRefreshToken: String = fixture()
                    val validAccessToken: String = fixture()

                    every { authService.reissueAccessToken(validRefreshToken) } returns validAccessToken

                    mockMvc.post("/api/v1/auth/access") {
                        locale(Locale.KOREA)
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        cookie(Cookie("refreshToken", validRefreshToken))
                    }.andExpect {
                        status { isOk() }
                        jsonPath("$.accessToken") { value(validAccessToken) }
                    }
                }
            }
        }
    },
)
