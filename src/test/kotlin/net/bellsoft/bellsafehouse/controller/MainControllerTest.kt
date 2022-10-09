package net.bellsoft.bellsafehouse.controller

import io.kotest.core.spec.style.BehaviorSpec
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest
@ActiveProfiles("test")
internal class MainControllerTest(
    private val mockMvc: MockMvc,
    @Value("\${application.deploy.environment}") private val applicationDeployEnvironment: String,
) : BehaviorSpec({
    Given("모든 상황에서") {
        When("displayIndex 요청 시") {
            Then("'Bell Safe House Index API' 반환") {
                mockMvc.perform(MockMvcRequestBuilders.get("/api/"))
                    .andExpect(MockMvcResultMatchers.status().isOk)
                    .andExpect(
                        MockMvcResultMatchers.content()
                            .string("Bell Safe House Index API ($applicationDeployEnvironment)")
                    )
                    .andDo(MockMvcResultHandlers.print())
            }
        }

        When("displayEnvironment 요청 시") {
            Then("환경 정보 반환") {
                mockMvc.perform(MockMvcRequestBuilders.get("/api/environment"))
                    .andExpect(MockMvcResultMatchers.status().isOk)
                    .andExpect(
                        MockMvcResultMatchers.content().string(applicationDeployEnvironment)
                    )
                    .andDo(MockMvcResultHandlers.print())
            }
        }
    }
})
