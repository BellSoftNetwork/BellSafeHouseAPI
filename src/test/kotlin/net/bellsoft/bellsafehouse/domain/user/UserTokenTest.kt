package net.bellsoft.bellsafehouse.domain.user

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import net.bellsoft.bellsafehouse.domain.JpaEntityTest
import net.bellsoft.bellsafehouse.fixture.baseFixture
import net.bellsoft.bellsafehouse.fixture.domain.user.UserFixture
import net.bellsoft.bellsafehouse.fixture.util.feature

@JpaEntityTest
class UserTokenTest(
    private val userRepository: UserRepository,
    private val userTokenRepository: UserTokenRepository,
) : BehaviorSpec(
    {
        val fixture = baseFixture.feature(UserFixture.Feature.NORMAL)

        Given("유저가 가입한 직후") {
            val user = userRepository.save(fixture())

            When("토큰 발급 시도 시") {
                val userToken = userTokenRepository.save(
                    baseFixture {
                        property(UserToken::user) { user }
                    },
                )

                Then("신규 토큰이 정상적으로 등록된다") {
                    userToken.id.toString().length shouldBeGreaterThan 1
                    userToken.user.id shouldBe user.id
                }
            }
        }
    },
)
