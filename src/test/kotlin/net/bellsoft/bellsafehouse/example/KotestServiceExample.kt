package net.bellsoft.bellsafehouse.example

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bellsoft.bellsafehouse.service.DummyService
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class KotestServiceExample(
    private val dummyService: DummyService,
) : BehaviorSpec(
    {
        Given("모든 환경에서") {
            When("DummyService 에 echo 메서드를 파라미터 없이 호출하면") {
                Then("'dummy' 가 반환된다") {
                    dummyService.echo() shouldBe "dummy"
                }
            }
            When("DummyService 에 echo 메서드를 'Bell' 파라미터를 넣어서 호출하면") {
                Then("'Bell' 이 반환된다") {
                    dummyService.echo("Bell") shouldBe "Bell"
                }
            }
        }
    },
)
