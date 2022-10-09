package net.bellsoft.bellsafehouse.domain.user

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import net.bellsoft.bellsafehouse.domain.JpaEntityTest
import org.springframework.data.repository.findByIdOrNull

@JpaEntityTest
internal class UserRepositoryTest(
    private val userRepository: UserRepository,
) : BehaviorSpec({
    Given("유저가 생성된 상황에서") {
        val user = userRepository.save(User.fixture())

        When("한 유저를 같은 트랜잭션 안에서 여러번 조회하면") {
            val selectedUser1 = userRepository.findByIdOrNull(user.id)
            val selectedUser2 = userRepository.findByIdOrNull(user.id)

            Then("캐싱된 값이 나온다") {
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
})
