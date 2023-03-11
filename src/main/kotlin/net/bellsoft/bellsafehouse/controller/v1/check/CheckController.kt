package net.bellsoft.bellsafehouse.controller.v1.check

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import net.bellsoft.bellsafehouse.controller.v1.check.dto.UserCheckRequest
import net.bellsoft.bellsafehouse.controller.v1.check.dto.UserCheckResponse
import net.bellsoft.bellsafehouse.service.CheckService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "유저 확인", description = "유저 확인 API")
@RestController
@Validated
@RequestMapping("/v1/check")
class CheckController(
    private val checkService: CheckService,
) {
    @Operation(summary = "유저 존재 여부 확인", description = "입력 받은 유저 데이터를 기반으로 존재 여부를 반환")
    @ApiResponse(responseCode = "200", description = "입력 받은 유저 데이터 / 존재 여부")
    @GetMapping
    fun checkUser(@Valid userCheckRequest: UserCheckRequest): ResponseEntity<UserCheckResponse> {
        val userAvailableType = checkService.checkUser(userCheckRequest)
        val userCheckResponse = UserCheckResponse(userCheckRequest, userAvailableType)

        return ResponseEntity.ok().body(userCheckResponse)
    }
}
