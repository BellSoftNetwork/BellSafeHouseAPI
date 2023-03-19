package net.bellsoft.bellsafehouse.service

import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import net.bellsoft.bellsafehouse.domain.user.User
import net.bellsoft.bellsafehouse.domain.user.UserRepository
import net.bellsoft.bellsafehouse.fixture.baseFixture
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
            val normalUser: User = fixture { }

            When("정상적인 유저로 유저 삭제 요청시") {
                every { userRepository.deleteById(any()) } returns Unit
                Then("정상적으로 삭제된다") {
                    shouldNotThrowAny {
                        accountService.deleteUser(normalUser)
                    }
                }
            }
        }
    },
)
