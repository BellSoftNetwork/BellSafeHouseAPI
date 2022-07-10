package net.bellsoft.bellsafehouse

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class BellSafeHouseApplicationTests : BehaviorSpec({
    given("닉네임이 설정되어 있지 않을 때") {
        var nickname: String?

        `when`("닉네임을 'Bell' 로 설정해주면") {
            nickname = "Bell"

            then("닉네임이 'Bell' 로 반환된다") {
                nickname shouldBe "Bell"
            }
        }

        nicknameMatchMap.forAll { (originalNickname, renameNickname) ->
            `when`("닉네임을 '$originalNickname' 로 설정하면") {
                nickname = originalNickname

                then("닉네임이 '$renameNickname' 로 설정된다") {
                    nickname shouldBe renameNickname
                }
            }
        }
    }

    given("닉네임이 설정되어 있을 때") {
        var nickname = "Woohaha!!"

        `when`("닉네임을 불러오면") {
            then("null 이 아닌 값이 나온다") {
                nickname shouldNotBe null
            }
        }

        nicknameContainMap.forAll { (originalNickname, containNickname) ->
            `when`("닉네임을 '$originalNickname' 로 설정하면") {
                nickname = originalNickname

                then("닉네임에 '$containNickname' 이 포함된다") {
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
