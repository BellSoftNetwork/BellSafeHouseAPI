package net.bellsoft.bellsafehouse.controller.v1.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import net.bellsoft.bellsafehouse.controller.v1.dto.SingleResponse

@Schema(description = "회원가입 정보")
data class RegisteredUserResponse(
    @Schema(description = "사용자 ID", example = "userid")
    val userId: String,
) : SingleResponse()
