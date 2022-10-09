package net.bellsoft.bellsafehouse.example

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain

internal class KotestBasicExample : BehaviorSpec({
    Given("닉네임이 설정되어 있지 않을 때") {
        var nickname: String?

        When("닉네임을 'Bell' 로 설정해주면") {
            nickname = "Bell"

            Then("닉네임이 'Bell' 로 반환된다") {
                nickname shouldBe "Bell"
            }
        }

        nicknameMatchMap.forAll { (originalNickname, renameNickname) ->
            When("닉네임을 '$originalNickname' 로 설정하면") {
                nickname = originalNickname

                Then("닉네임이 '$renameNickname' 로 설정된다") {
                    nickname shouldBe renameNickname
                }
            }
        }
    }

    Given("닉네임이 설정되어 있을 때") {
        var nickname = "Woohaha!!"

        When("닉네임을 불러오면") {
            Then("null 이 아닌 값이 나온다") {
                nickname shouldNotBe null
            }
        }

        nicknameContainMap.forAll { (originalNickname, containNickname) ->
            When("닉네임을 '$originalNickname' 로 설정하면") {
                nickname = originalNickname

                Then("닉네임에 '$containNickname' 이 포함된다") {
                    nickname shouldContain containNickname
                }
            }
        }
    }
}) {
    companion object {
        private val nicknameMatchMap = listOf(
            "Bell" to "Bell",
            "Soft Bell" to "Soft Bell",
            "Hard Bell" to "Hard Bell"
        )
        private val nicknameContainMap = listOf(
            "Bell" to "Bell",
            "Soft Bell" to "Bell",
            "Hard Bell" to "Bell"
        )
    }
}
