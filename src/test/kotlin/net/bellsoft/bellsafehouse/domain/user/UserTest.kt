package net.bellsoft.bellsafehouse.domain.user

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockkStatic
import net.bellsoft.bellsafehouse.domain.JpaEntityTest
import net.bellsoft.bellsafehouse.fixture.baseNullFixture
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

@JpaEntityTest
internal class UserTest(
    private val userRepository: UserRepository,
) : BehaviorSpec(
    {
        val fixture = baseNullFixture

        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now() } returns TEST_LOCAL_DATE_TIME

        Given("유저가 없는 상황에서") {
            When("최소 파라미터로 유저 엔티티를 생성하면") {
                val originalUser: User = fixture()
                val createdUser = userRepository.save(originalUser)

                Then("설정한 값이 정상적으로 등록된다") {
                    assertSoftly {
                        createdUser.userId shouldBe originalUser.userId
                        createdUser.password shouldBe originalUser.password
                        createdUser.email shouldBe originalUser.email
                        createdUser.nickname shouldBe originalUser.nickname
                    }
                }

                Then("값을 제공하지 않은 칼럼에 기본 값이 등록된다") {
                    assertSoftly {
                        createdUser.id shouldNotBe null
                        createdUser.marketingAgreedAt shouldBe null
                    }
                }

                Then("생성 및 수정 시각이 현재로 등록된다") {
                    assertSoftly {
                        createdUser.createdAt shouldBe TEST_LOCAL_DATE_TIME
                        createdUser.updatedAt shouldBe TEST_LOCAL_DATE_TIME
                    }
                }
            }
        }

        Given("유저가 생성된 상황에서") {
            val user = userRepository.save(fixture())

            When("기존에 존재하는 아이디로 유저 엔티티를 생성하면") {
                val duplicatedUser: User = fixture {
                    property(User::userId) { user.userId }
                }

                Then("DataIntegrityViolationException 예외가 발생된다") {
                    shouldThrow<DataIntegrityViolationException> {
                        userRepository.save(duplicatedUser)
                    }
                }
            }

            When("기존에 존재하는 이메일로 유저 엔티티를 생성하면") {
                val duplicatedUser: User = fixture {
                    property(User::email) { user.email }
                }

                Then("DataIntegrityViolationException 예외가 발생된다") {
                    shouldThrow<DataIntegrityViolationException> {
                        userRepository.save(duplicatedUser)
                    }
                }
            }

            When("기존에 존재하는 닉네임으로 유저 엔티티를 생성하면") {
                val duplicatedUser: User = fixture {
                    property(User::nickname) { user.nickname }
                }

                Then("DataIntegrityViolationException 예외가 발생된다") {
                    shouldThrow<DataIntegrityViolationException> {
                        userRepository.save(duplicatedUser)
                    }
                }
            }

            When("마케팅 수신 동의를 수행하면") {
                user.agreeToMarketing()
                userRepository.save(user)

                Then("동의 시각에 현재 시각이 기록된다") {
                    val marketingAgreedUser = userRepository.findByIdOrNull(user.id)

                    marketingAgreedUser shouldNotBe null
                    marketingAgreedUser?.marketingAgreedAt shouldBe TEST_LOCAL_DATE_TIME
                }

                Then("마케팅 수신 동의 여부 메서드가 true 를 반환한다") {
                    user.isMarketingAgree() shouldBe true
                }
            }

            When("마케팅 수신 동의를 철회하면") {
                user.disagreeToMarketing()
                userRepository.save(user)

                Then("동의 시각이 제거된다") {
                    val marketingAgreedUser = userRepository.findByIdOrNull(user.id)

                    marketingAgreedUser shouldNotBe null
                    marketingAgreedUser?.marketingAgreedAt shouldBe null
                }

                Then("마케팅 수신 동의 여부 메서드가 false 를 반환한다") {
                    user.isMarketingAgree() shouldBe false
                }
            }

            When("유저가 탈퇴 요청 시") {
                user.withdraw()
                userRepository.save(user)

                Then("탈퇴 시각이 설정된다") {
                    user.deletedAt shouldBe TEST_LOCAL_DATE_TIME
                }
            }
        }
    },
) {
    companion object {
        val TEST_LOCAL_DATE_TIME: LocalDateTime = LocalDateTime.of(2022, 10, 9, 17, 30)
    }
}
