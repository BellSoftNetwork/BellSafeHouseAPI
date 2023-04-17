package net.bellsoft.bellsafehouse.controller.v1.auth.password.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email

@Schema(description = "비밀번호 초기화 메일 발송 요청 정보")
data class SendResetMailRequest(
    @Schema(description = "메일 발송 요청 이메일", example = "bell@bellsoft.net")
    @field:Email
    val email: String,
)
