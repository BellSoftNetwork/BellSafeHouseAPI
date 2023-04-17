package net.bellsoft.bellsafehouse.component.jwt

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic
import net.bellsoft.bellsafehouse.domain.user.UserRole
import net.bellsoft.bellsafehouse.exception.InvalidTokenException
import net.bellsoft.bellsafehouse.exception.TokenNotFoundException
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletRequest
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

@SpringBootTest
class JwtSupportTest(
    private val jwtSupport: JwtSupport,
) : BehaviorSpec(
    {
        mockkStatic(Instant::class)

        every { Instant.now() } returns TEST_MOCK_INSTANT

        Given("Refresh, Access Token 이 정상적으로 발급된 상태에서") {
            val userId = "testUserId"
            val uuid = UUID.randomUUID()
            val userRole = UserRole.NORMAL

            val generatedRefreshToken = jwtSupport.generateRefreshToken(userId, uuid)
            val generatedAccessToken = jwtSupport.generateAccessToken(generatedRefreshToken, userRole)
            val refreshTokenCookie = jwtSupport.createRefreshTokenCookie(generatedRefreshToken)
            val httpServletRequest = MockHttpServletRequest().apply {
                setCookies(refreshTokenCookie)
                addHeader(AUTHORIZATION_HEADER_NAME, "$BEARER_TOKEN_HEADER_NAME ${generatedAccessToken.credentials}")
            }

            When("임의의 문자열로 refreshToken 정보를 파싱하려고 할 경우") {
                val randomString = "123456abcdef"

                Then("InvalidTokenException 이 발생한다") {
                    shouldThrow<InvalidTokenException> {
                        jwtSupport.parseRefreshToken(randomString)
                    }
                }
            }

            When("accessToken 으로 refreshToken 정보를 파싱하려고 할 경우") {
                Then("InvalidTokenException 이 발생한다") {
                    shouldThrow<InvalidTokenException> {
                        jwtSupport.parseRefreshToken(generatedAccessToken.value)
                    }
                }
            }

            When("refreshToken 으로 refreshToken 정보를 파싱하려고 할 경우") {
                Then("정상적으로 파싱된다") {
                    val refreshTokenDto = jwtSupport.parseRefreshToken(generatedRefreshToken.value)

                    refreshTokenDto.userId shouldBe userId
                    refreshTokenDto.uuid shouldBe uuid
                }
            }

            When("임의의 문자열로 accessToken 정보를 파싱하려고 할 경우") {
                val randomString = "123456abcdef"

                Then("InvalidTokenException 이 발생한다") {
                    shouldThrow<InvalidTokenException> {
                        jwtSupport.parseAccessToken(randomString)
                    }
                }
            }

            When("refreshToken 으로 accessToken 정보를 파싱하려고 할 경우") {
                Then("InvalidTokenException 이 발생한다") {
                    shouldThrow<InvalidTokenException> {
                        jwtSupport.parseAccessToken(generatedRefreshToken.value)
                    }
                }
            }

            When("accessToken 으로 accessToken 정보를 파싱하려고 할 경우") {
                Then("정상적으로 파싱된다") {
                    val accessTokenDto = jwtSupport.parseAccessToken(generatedAccessToken.value)

                    accessTokenDto.userId shouldBe userId
                    accessTokenDto.uuid shouldBe uuid
                    accessTokenDto.role shouldBe userRole.name
                }
            }

            When("refreshToken 의 만료 시간 7일 - 1 나노 초 전 일 경우") {
                every { Instant.now() } returns TEST_MOCK_INSTANT
                    .plus(30 - 7, ChronoUnit.DAYS)
                    .plus(1, ChronoUnit.NANOS)

                Then("자동 재갱신 기간 확인 시 갱신 가능한 상태로 반환된다") {
                    jwtSupport.isWillRefreshTokenExpires(generatedRefreshToken) shouldBe true
                }
            }

            When("refreshToken 의 만료 시간 7일 전 일 경우") {
                every { Instant.now() } returns TEST_MOCK_INSTANT
                    .plus(30 - 7, ChronoUnit.DAYS)

                Then("자동 재갱신 기간 확인 시 갱신 불가능한 상태로 반환된다") {
                    jwtSupport.isWillRefreshTokenExpires(generatedRefreshToken) shouldBe false
                }
            }

            When("refreshToken 의 만료 시간을 경과했을 경우") {
                every { Instant.now() } returns TEST_MOCK_INSTANT
                    .plus(30, ChronoUnit.DAYS)
                    .plus(1, ChronoUnit.MILLIS)

                Then("InvalidTokenException 이 발생한다") {
                    shouldThrow<InvalidTokenException> {
                        jwtSupport.isWillRefreshTokenExpires(generatedRefreshToken)
                    }
                }
            }

            When("정상적인 요청으로 Access Token 을 얻으려고 했을 경우") {
                Then("해당 Access Token 을 정상적으로 파싱할 수 있다") {
                    shouldNotThrowAny {
                        jwtSupport.getAccessToken(httpServletRequest)
                    }
                }
            }

            When("잘못된 액세스 토큰 헤더로 Access Token 을 얻으려고 했을 경우") {
                val abnormalTokenRequest = MockHttpServletRequest().apply {
                    removeHeader(AUTHORIZATION_HEADER_NAME)
                    addHeader(AUTHORIZATION_HEADER_NAME, "$BEARER_TOKEN_HEADER_NAME abnormal.access.token")
                }

                Then("해당 Access Token 을 파싱하는 도중 예외가 발생한다") {
                    shouldThrowAny {
                        jwtSupport.parseClaimsJws(jwtSupport.getAccessToken(abnormalTokenRequest))
                    }
                }
            }

            When("빈 $AUTHORIZATION_HEADER_NAME 헤더 요청으로 Access Token 을 얻으려고 했을 경우") {
                val emptyAuthorizationRequest = MockHttpServletRequest()
                emptyAuthorizationRequest.removeHeader(AUTHORIZATION_HEADER_NAME)

                Then("해당 Access Token 을 파싱하는 도중 예외가 발생한다") {
                    shouldThrow<TokenNotFoundException> {
                        jwtSupport.getAccessToken(emptyAuthorizationRequest)
                    }
                }
            }
        }
    },
) {
    companion object {
        val TEST_MOCK_INSTANT: Instant = ZonedDateTime
            .of(LocalDate.of(2020, 1, 29), LocalTime.MIN, ZoneId.systemDefault())
            .toInstant()

        private const val AUTHORIZATION_HEADER_NAME = "Authorization"
        private const val BEARER_TOKEN_HEADER_NAME = "Bearer"
    }
}
