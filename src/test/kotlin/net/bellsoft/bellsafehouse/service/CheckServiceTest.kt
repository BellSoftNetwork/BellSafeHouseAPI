package net.bellsoft.bellsafehouse.service

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bellsoft.bellsafehouse.controller.v1.auth.dto.UserRegistrationRequest
import net.bellsoft.bellsafehouse.controller.v1.check.dto.UserCheckRequest
import net.bellsoft.bellsafehouse.exception.UnprocessableEntityException
import net.bellsoft.bellsafehouse.fixture.baseNullFixture
import net.bellsoft.bellsafehouse.service.type.UserAvailableType
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
internal class CheckServiceTest(
    private val checkService: CheckService,
    private val authService: AuthService,
) : BehaviorSpec(
    {
        val fixture = baseNullFixture

        Given("가입한 사용자가 없는 상황에서") {
            When("유저 아이디로 유저 조회 시도 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::userId) { "emptyUserId" }
                }

                Then("available 이 반환 된다.") {
                    checkService.checkUser(userCheckRequest) shouldBe UserAvailableType.AVAILABLE
                }
            }

            When("유저 이메일로 유저 조회 시도 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::email) { "emptyUserEmail" }
                }

                Then("available 이 반환 된다.") {
                    checkService.checkUser(userCheckRequest) shouldBe UserAvailableType.AVAILABLE
                }
            }

            When("유저 별명으로 유저 조회 시도 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::nickname) { "emptyUserNickname" }
                }

                Then("available 이 반환 된다.") {
                    checkService.checkUser(userCheckRequest) shouldBe UserAvailableType.AVAILABLE
                }
            }

            When("빈 데이터로 유저 조회 시도 시") {
                val userCheckRequest: UserCheckRequest = fixture { }

                Then("UnprocessableEntityException 이 발생한다.") {
                    shouldThrow<UnprocessableEntityException> {
                        checkService.checkUser(userCheckRequest)
                    }
                }
            }
        }

        Given("이미 가입한 사용자가 존재하는 상황에서") {
            val userRegistrationRequest: UserRegistrationRequest = fixture {
                property(UserRegistrationRequest::userId) { "checkUserId" }
                property(UserRegistrationRequest::password) { "checkUserPassword" }
                property(UserRegistrationRequest::email) { "checkUserEmail" }
                property(UserRegistrationRequest::nickname) { "checkUserNick" }
            }

            shouldNotThrowAny { authService.register(userRegistrationRequest) }

            When("유저 아이디로 유저 조회 시도 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::userId) { userRegistrationRequest.userId }
                }

                Then("duplicated 이 반환 된다.") {
                    checkService.checkUser(userCheckRequest) shouldBe UserAvailableType.DUPLICATED
                }
            }

            When("유저 이메일로 유저 조회 시도 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::email) { userRegistrationRequest.email }
                }

                Then("duplicated 가 반환 된다.") {
                    checkService.checkUser(userCheckRequest) shouldBe UserAvailableType.DUPLICATED
                }
            }

            When("유저 별명으로 유저 조회 시도 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::nickname) { userRegistrationRequest.nickname }
                }

                Then("duplicated 가 반환 된다.") {
                    checkService.checkUser(userCheckRequest) shouldBe UserAvailableType.DUPLICATED
                }
            }
        }
    },
)
