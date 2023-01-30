package net.bellsoft.bellsafehouse.controller.v1.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import net.bellsoft.bellsafehouse.controller.v1.dto.SingleResponse

@Schema(description = "액세스 토큰 정보")
data class AccessResponse(
    @Schema(description = "액세스 토큰", example = "AAAA.BBBB.CCCC")
    val accessToken: String,
) : SingleResponse()
