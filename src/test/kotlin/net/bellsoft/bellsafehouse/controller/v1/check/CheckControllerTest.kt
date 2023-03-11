package net.bellsoft.bellsafehouse.controller.v1.check

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import net.bellsoft.bellsafehouse.controller.v1.check.dto.UserCheckRequest
import net.bellsoft.bellsafehouse.fixture.baseFixture
import net.bellsoft.bellsafehouse.service.CheckService
import net.bellsoft.bellsafehouse.service.type.UserAvailableType
import net.bellsoft.bellsafehouse.util.QueryParamsConverter
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.util.Locale

@WebMvcTest(CheckController::class)
@WithAnonymousUser
@ActiveProfiles("test")
class CheckControllerTest(
    private val webContext: WebApplicationContext,
    @MockkBean private val checkService: CheckService,
) : BehaviorSpec(
    {
        val mockMvc = MockMvcBuilders.webAppContextSetup(webContext)
            .apply { SecurityMockMvcConfigurers.springSecurity() }.build()
        val objectMapper = jacksonObjectMapper()
        val fixture = baseFixture

        Given("조회 할 유저가 존재하지 않는 상황에서") {
            every { checkService.checkUser(any()) } returns UserAvailableType.AVAILABLE

            When("유저 아이디로 조회 요청 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::userId) { "testId" }
                    property(UserCheckRequest::email) { null }
                    property(UserCheckRequest::nickname) { null }
                }

                Then("200 OK, available 상태가 반환된다") {
                    mockMvc.get("/v1/check") {
                        locale(Locale.KOREA)
                        params = QueryParamsConverter.convert(userCheckRequest, objectMapper)
                    }.andExpect {
                        status { isOk() }
                        jsonPath("$.status") { value(UserAvailableType.AVAILABLE.value) }
                        jsonPath("$.filterParams.userId") { value(userCheckRequest.userId) }
                        jsonPath("$.filterParams.email") { doesNotHaveJsonPath() }
                        jsonPath("$.filterParams.nickname") { doesNotHaveJsonPath() }
                    }
                }
            }

            When("2개 이상의 데이터로 조회 요청 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::userId) { "testId" }
                    property(UserCheckRequest::email) { "testId@test.com" }
                    property(UserCheckRequest::nickname) { null }
                }

                Then("400 Bad Request 가 반환된다") {
                    mockMvc.get("/v1/check") {
                        locale(Locale.KOREA)
                        params = QueryParamsConverter.convert(userCheckRequest, objectMapper)
                    }.andExpect {
                        status { isBadRequest() }
                    }
                }
            }

            When("잘못 된 형식의 데이터로 조회 요청 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::userId) { null }
                    property(UserCheckRequest::email) { "ThisIsNotValidEmail" }
                    property(UserCheckRequest::nickname) { null }
                }

                Then("400 Bad Request 가 반환된다") {
                    mockMvc.get("/v1/check") {
                        locale(Locale.KOREA)
                        params = QueryParamsConverter.convert(userCheckRequest, objectMapper)
                    }.andExpect {
                        status { isBadRequest() }
                    }
                }
            }
        }

        Given("조회 할 유저가 존재하는 상황에서") {
            every { checkService.checkUser(any()) } returns UserAvailableType.DUPLICATED

            When("유저 아이디로 조회 요청 시") {
                val userCheckRequest: UserCheckRequest = fixture {
                    property(UserCheckRequest::userId) { "testId" }
                    property(UserCheckRequest::email) { null }
                    property(UserCheckRequest::nickname) { null }
                }

                Then("200 OK, available 상태가 반환된다") {
                    mockMvc.get("/v1/check") {
                        locale(Locale.KOREA)
                        params = QueryParamsConverter.convert(userCheckRequest, objectMapper)
                    }.andExpect {
                        status { isOk() }
                        jsonPath("$.status") { value(UserAvailableType.DUPLICATED.value) }
                        jsonPath("$.filterParams.userId") { value(userCheckRequest.userId) }
                        jsonPath("$.filterParams.email") { doesNotHaveJsonPath() }
                        jsonPath("$.filterParams.nickname") { doesNotHaveJsonPath() }
                    }
                }
            }
        }
    },
)
