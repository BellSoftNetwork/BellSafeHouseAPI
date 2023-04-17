package net.bellsoft.bellsafehouse.controller.v1.auth.password.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size

@Schema(description = "비밀번호 초기화 요청 정보")
data class PerformResetRequest(
    @Schema(description = "비밀번호 초기화 토큰", example = "01234567-89ab-cdef-0123-456789abcdef")
    @field:Size(min = 36, max = 36)
    val resetToken: String,

    @Schema(description = "초기화 할 비밀번호", example = "password!@#")
    @field:Size(min = 8, max = 20)
    val newPassword: String,
)
