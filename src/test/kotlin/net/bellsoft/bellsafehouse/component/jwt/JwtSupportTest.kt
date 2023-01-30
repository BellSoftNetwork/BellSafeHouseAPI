package net.bellsoft.bellsafehouse.component.jwt

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic
import net.bellsoft.bellsafehouse.exception.InvalidTokenException
import org.springframework.boot.test.context.SpringBootTest
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

            val generatedRefreshToken = jwtSupport.generateRefreshToken(userId, uuid)
            val generatedAccessToken = jwtSupport.generateAccessToken(generatedRefreshToken)

            When("임의의 문자열로 refreshToken 정보를 파싱하려고 할 경우") {
                val randomString = "123456abcdef"

                Then("InvalidTokenException 이 발생한다") {
                    shouldThrow<InvalidTokenException> {
                        jwtSupport.getRefreshToken(randomString)
                    }
                }
            }

            When("accessToken 으로 refreshToken 정보를 파싱하려고 할 경우") {
                Then("InvalidTokenException 이 발생한다") {
                    shouldThrow<InvalidTokenException> {
                        jwtSupport.getRefreshToken(generatedAccessToken.value)
                    }
                }
            }

            When("refreshToken 으로 refreshToken 정보를 파싱하려고 할 경우") {
                Then("정상적으로 파싱된다") {
                    val refreshTokenDto = jwtSupport.getRefreshToken(generatedRefreshToken.value)

                    refreshTokenDto.userId shouldBe userId
                    refreshTokenDto.uuid shouldBe uuid
                }
            }

            When("임의의 문자열로 accessToken 정보를 파싱하려고 할 경우") {
                val randomString = "123456abcdef"

                Then("InvalidTokenException 이 발생한다") {
                    shouldThrow<InvalidTokenException> {
                        jwtSupport.getAccessToken(randomString)
                    }
                }
            }

            When("refreshToken 으로 accessToken 정보를 파싱하려고 할 경우") {
                Then("InvalidTokenException 이 발생한다") {
                    shouldThrow<InvalidTokenException> {
                        jwtSupport.getAccessToken(generatedRefreshToken.value)
                    }
                }
            }

            When("accessToken 으로 accessToken 정보를 파싱하려고 할 경우") {
                Then("정상적으로 파싱된다") {
                    val accessTokenDto = jwtSupport.getAccessToken(generatedAccessToken.value)

                    accessTokenDto.userId shouldBe userId
                    accessTokenDto.uuid shouldBe uuid
                    accessTokenDto.role shouldBe "TODO"
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
        }
    },
) {
    companion object {
        val TEST_MOCK_INSTANT: Instant = ZonedDateTime
            .of(LocalDate.of(2020, 1, 29), LocalTime.MIN, ZoneId.systemDefault())
            .toInstant()
    }
}
