package net.bellsoft.bellsafehouse.service

import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.slot
import net.bellsoft.bellsafehouse.controller.v1.account.dto.AccountEditRequest
import net.bellsoft.bellsafehouse.domain.user.User
import net.bellsoft.bellsafehouse.domain.user.UserRepository
import net.bellsoft.bellsafehouse.fixture.baseFixture
import net.bellsoft.bellsafehouse.fixture.baseNullFixture
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest(
    private val accountService: AccountService,
    @MockkBean private val userRepository: UserRepository,
) : BehaviorSpec(
    {
        val fixture = baseFixture

        Given("기존에 가입한 사용자가 있는 상황에서") {
            val normalUser: User = fixture {
                property(User::marketingAgreed) { false }
            }

            When("정상적인 유저로 유저 삭제 요청시") {
                every { userRepository.deleteById(any()) } returns Unit

                Then("정상적으로 삭제된다") {
                    shouldNotThrowAny {
                        accountService.deleteUser(normalUser)
                    }
                }
            }

            When("정상적인 유저로 유저 정보 전체 변경 요청시") {
                val userSlot = slot<User>()

                every { userRepository.save(capture(userSlot)) } answers { userSlot.captured }

                val accountEditRequest: AccountEditRequest = baseNullFixture {
                    property(AccountEditRequest::password) { "passwordToChange" }
                    property(AccountEditRequest::email) { "emailToChange@bellsoft.net" }
                    property(AccountEditRequest::nickname) { "nicknameToChange" }
                    property(AccountEditRequest::marketingAgreed) { true }
                }

                accountService.update(normalUser, accountEditRequest)

                Then("정상적으로 변경된다") {
                    assertSoftly {
                        userSlot.captured.id shouldBe normalUser.id
                        userSlot.captured.password shouldBe accountEditRequest.password
                        userSlot.captured.email shouldBe accountEditRequest.email
                        userSlot.captured.nickname shouldBe accountEditRequest.nickname
                        userSlot.captured.marketingAgreed shouldBe accountEditRequest.marketingAgreed
                    }
                }
            }

            When("정상적인 유저로 유저 정보 하나만 변경 요청시") {
                val userSlot = slot<User>()

                every { userRepository.save(capture(userSlot)) } answers { userSlot.captured }

                val accountEditRequest: AccountEditRequest = baseNullFixture {
                    property(AccountEditRequest::password) { "passwordToChange" }
                }

                accountService.update(normalUser, accountEditRequest)

                Then("정상적으로 변경된다") {
                    assertSoftly {
                        userSlot.captured.id shouldBe normalUser.id
                        userSlot.captured.password shouldBe accountEditRequest.password
                        userSlot.captured.email shouldBe normalUser.email
                        userSlot.captured.nickname shouldBe normalUser.nickname
                        userSlot.captured.marketingAgreed shouldBe normalUser.marketingAgreed
                    }
                }
            }

            When("유저 정보 조회 요청시") {
                val user = User("userId", "password", "user@email.com", "userNickname", false)
                val info = accountService.getInfo(user)

                Then("정상적으로 조회된다") {
                    assertSoftly {
                        user.userId shouldBe info.userId
                        user.email shouldBe info.email
                        user.nickname shouldBe info.nickname
                        user.marketingAgreed shouldBe info.marketingAgreed
                        user.createdAt shouldBe info.createdAt
                    }
                }
            }
        }
    },
)
