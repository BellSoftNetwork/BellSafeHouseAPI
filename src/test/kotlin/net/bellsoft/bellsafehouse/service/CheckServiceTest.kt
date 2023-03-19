package net.bellsoft.bellsafehouse.service

import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import net.bellsoft.bellsafehouse.controller.v1.auth.dto.UserRegistrationRequest
import net.bellsoft.bellsafehouse.controller.v1.check.dto.UserCheckRequest
import net.bellsoft.bellsafehouse.domain.user.UserRepository
import net.bellsoft.bellsafehouse.exception.UnprocessableEntityException
import net.bellsoft.bellsafehouse.fixture.baseNullFixture
import net.bellsoft.bellsafehouse.service.type.UserAvailableType
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
internal class CheckServiceTest(
    private val checkService: CheckService,
    @MockkBean private val userRepository: UserRepository,
) : BehaviorSpec(
    {
        val fixture = baseNullFixture

        Given("가입한 사용자가 없는 상황에서") {
            When("유저 아이디로 유저 조회 시도 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::userId) { "emptyUserId" }
                }

                every { userRepository.existsByUserId(userCheckRequest.userId!!) } returns false
                every { userRepository.existsDeletedByUserId(userCheckRequest.userId!!) } returns false

                Then("available 이 반환 된다.") {
                    checkService.checkUser(userCheckRequest) shouldBe UserAvailableType.AVAILABLE
                }
            }

            When("유저 이메일로 유저 조회 시도 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::email) { "emptyUserEmail" }
                }

                every { userRepository.existsByEmail(userCheckRequest.email!!) } returns false
                every { userRepository.existsDeletedByEmail(userCheckRequest.email!!) } returns false

                Then("available 이 반환 된다.") {
                    checkService.checkUser(userCheckRequest) shouldBe UserAvailableType.AVAILABLE
                }
            }

            When("유저 별명으로 유저 조회 시도 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::nickname) { "emptyUserNickname" }
                }

                every { userRepository.existsByNickname(userCheckRequest.nickname!!) } returns false
                every { userRepository.existsDeletedByNickname(userCheckRequest.nickname!!) } returns false

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

            When("유저 아이디로 유저 조회 시도 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::userId) { userRegistrationRequest.userId }
                }

                every { userRepository.existsByUserId(userRegistrationRequest.userId) } returns true

                Then("duplicated 가 반환 된다.") {
                    checkService.checkUser(userCheckRequest) shouldBe UserAvailableType.DUPLICATED
                }
            }

            When("유저 이메일로 유저 조회 시도 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::email) { userRegistrationRequest.email }
                }

                every { userRepository.existsByEmail(userRegistrationRequest.email) } returns true

                Then("duplicated 가 반환 된다.") {
                    checkService.checkUser(userCheckRequest) shouldBe UserAvailableType.DUPLICATED
                }
            }

            When("유저 별명으로 유저 조회 시도 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::nickname) { userRegistrationRequest.nickname }
                }

                every { userRepository.existsByNickname(userRegistrationRequest.nickname) } returns true

                Then("duplicated 가 반환 된다.") {
                    checkService.checkUser(userCheckRequest) shouldBe UserAvailableType.DUPLICATED
                }
            }
        }

        Given("가입한 사용자가 삭제 된 상황에서") {
            val userRegistrationRequest: UserRegistrationRequest = fixture {
                property(UserRegistrationRequest::userId) { "deletedUserId" }
                property(UserRegistrationRequest::password) { "deletedPassword" }
                property(UserRegistrationRequest::email) { "deletedEmail" }
                property(UserRegistrationRequest::nickname) { "deletedUserNick" }
            }

            When("유저 아이디로 유저 조회 시도 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::userId) { userRegistrationRequest.userId }
                }

                every { userRepository.existsByUserId(userRegistrationRequest.userId) } returns false
                every { userRepository.existsDeletedByUserId(userRegistrationRequest.userId) } returns true

                Then("deleted 가 반환 된다.") {
                    checkService.checkUser(userCheckRequest) shouldBe UserAvailableType.DELETED
                }
            }

            When("유저 이메일로 유저 조회 시도 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::email) { userRegistrationRequest.email }
                }

                every { userRepository.existsByEmail(userRegistrationRequest.email) } returns false
                every { userRepository.existsDeletedByEmail(userRegistrationRequest.email) } returns true

                Then("deleted 가 반환 된다.") {
                    checkService.checkUser(userCheckRequest) shouldBe UserAvailableType.DELETED
                }
            }

            When("유저 별명으로 유저 조회 시도 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::nickname) { userRegistrationRequest.nickname }
                }

                every { userRepository.existsByNickname(userRegistrationRequest.nickname) } returns false
                every { userRepository.existsDeletedByNickname(userRegistrationRequest.nickname) } returns true

                Then("deleted 가 반환 된다.") {
                    checkService.checkUser(userCheckRequest) shouldBe UserAvailableType.DELETED
                }
            }
        }
    },
)
