package net.bellsoft.bellsafehouse.domain.user

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockkStatic
import net.bellsoft.bellsafehouse.domain.JpaEntityTest
import net.bellsoft.bellsafehouse.fixture.baseFixture
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

@JpaEntityTest
internal class UserRepositoryTest(
    private val userRepository: UserRepository,
) : BehaviorSpec(
    {
        val fixture = baseFixture

        mockkStatic(LocalDateTime::class)

        Given("한 유저가 생성된 상황에서") {
            val user = userRepository.save(fixture())

            When("한 유저를 같은 트랜잭션 안에서 여러번 조회하면") {
                Then("캐싱된 값이 나온다") {
                    val selectedUser1 = userRepository.findByIdOrNull(user.id)
                    val selectedUser2 = userRepository.findByIdOrNull(user.id)

                    selectedUser1 shouldNotBe null
                    selectedUser1 shouldBe selectedUser2
                }
            }

            When("등록된 이메일로 유저를 조회하면") {
                val selectedUser = userRepository.findByEmail(user.email)

                Then("정상적으로 조회된다") {
                    selectedUser?.id shouldBe user.id
                }
            }

            When("등록되지 않은 이메일로 유저를 조회하면") {
                val unknownUser = userRepository.findByEmail("NON-EXIST-EMAIL@bellsoft.net")

                Then("빈 값이 조회된다") {
                    unknownUser shouldBe null
                }
            }
        }

        Given("2022년 가입 유저 2명, 2023년 가입 유저가 1명 생성된 상황에서") {
            val createdAtLocalDateTimes = listOf(
                LocalDateTime.of(2022, 1, 1, 0, 0),
                LocalDateTime.of(2022, 12, 30, 23, 23),
                LocalDateTime.of(2023, 1, 1, 0, 0),
            )

            createdAtLocalDateTimes.forEach {
                every { LocalDateTime.now() } returns it
                userRepository.save(fixture())
            }

            When("2022년 가입 유저를 조회하면") {
                val users = userRepository.getYearCreatedUsers(2022)

                Then("2명이 조회된다") {
                    users.size shouldBe 2
                }
            }
        }
    },
)
