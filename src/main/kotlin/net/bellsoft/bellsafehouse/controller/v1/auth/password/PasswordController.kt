package net.bellsoft.bellsafehouse.controller.v1.auth.password

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Size
import mu.KLogging
import net.bellsoft.bellsafehouse.controller.v1.auth.password.dto.PerformResetRequest
import net.bellsoft.bellsafehouse.controller.v1.auth.password.dto.SendResetMailRequest
import net.bellsoft.bellsafehouse.controller.v1.auth.password.dto.SendResetMailResponse
import net.bellsoft.bellsafehouse.service.auth.PasswordService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "인증", description = "인증 API")
@RestController
@Validated
@RequestMapping("/v1/auth/password")
class PasswordController(
    val passwordService: PasswordService,
) {
    @Operation(summary = "비밀번호 초기화 메일 발송", description = "이메일 주소로 비밀번호 초기화 요청 이메일 전송")
    @PostMapping("/reset")
    fun sendResetMail(
        @RequestBody @Valid
        sendResetMailRequest: SendResetMailRequest,
    ): ResponseEntity<SendResetMailResponse> {
        runCatching {
            passwordService.sendReset(sendResetMailRequest)
            return ResponseEntity.ok().build()
        }

        return ResponseEntity.badRequest().build()
    }

    @Operation(summary = "비밀번호 초기화 토큰 검증", description = "비밀번호 초기화 토큰 검증")
    @GetMapping("/reset")
    fun validateResetToken(
        @RequestParam
        @Valid
        @Schema(description = "비밀번호 초기화 토큰", example = "01234567-89ab-cdef-0123-456789abcdef")
        @Size(min = 36, max = 36)
        resetToken: String,
    ): ResponseEntity<SendResetMailResponse> {
        runCatching {
            passwordService.validateResetToken(resetToken)
            return ResponseEntity.ok().build()
        }

        return ResponseEntity.badRequest().build()
    }

    @Operation(summary = "비밀번호 초기화", description = "비밀번호 초기화 토큰과 비밀번호를 이용해 비밀번호 초기화 수행")
    @PutMapping
    fun performReset(
        @RequestBody @Valid
        performResetRequest: PerformResetRequest,
    ): ResponseEntity<Nothing> {
        runCatching {
            passwordService.performReset(performResetRequest)
            return ResponseEntity.ok().build()
        }

        return ResponseEntity.badRequest().build()
    }

    companion object : KLogging()
}
