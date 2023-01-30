package net.bellsoft.bellsafehouse.controller

import io.kotest.core.spec.style.BehaviorSpec
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(MainController::class)
@WithMockUser
@ActiveProfiles("test")
internal class MainControllerTest(
    private val mockMvc: MockMvc,
    @Value("\${application.deploy.environment}") private val applicationDeployEnvironment: String,
) : BehaviorSpec(
    {
        Given("로그인 한 상황에서") {
            When("displayIndex 요청 시") {
                Then("'Bell Safe House Index API' 반환") {
                    mockMvc.get("/api/").andExpect {
                        status { isOk() }
                        content { string("Bell Safe House Index API ($applicationDeployEnvironment)") }
                    }
                }
            }

            When("displayEnvironment 요청 시") {
                Then("환경 정보 반환") {
                    mockMvc.get("/api/environment").andExpect {
                        status { isOk() }
                        content { string(applicationDeployEnvironment) }
                    }
                }
            }
        }
    },
)
