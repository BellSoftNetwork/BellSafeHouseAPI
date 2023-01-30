package net.bellsoft.bellsafehouse.controller.v1.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Size

@Schema(description = "로그인 요청 정보")
data class UserLoginRequest(
    @Schema(description = "사용자 ID", example = "userid")
    @field:Size(min = 4, max = 20)
    val userId: String,

    @Schema(description = "비밀번호", example = "password!@#")
    @field:Size(min = 8, max = 20)
    val password: String,
)
