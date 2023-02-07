package net.bellsoft.bellsafehouse.controller.v1.check.dto

import io.swagger.v3.oas.annotations.media.Schema
import net.bellsoft.bellsafehouse.service.type.UserAvailableType

@Schema(description = "유저 존재 여부 확인 데이터")
data class UserCheckResponse(
    @Schema(description = "입력 받은 사용자 데이터")
    val filterParams: UserCheckRequest,

    @Schema(description = "사용자 ID 존재 여부")
    val status: UserAvailableType,
)
