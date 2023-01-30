package net.bellsoft.bellsafehouse.controller.v1.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import net.bellsoft.bellsafehouse.controller.v1.dto.SingleResponse
import net.bellsoft.bellsafehouse.service.dto.UserAuthToken

@Schema(description = "리프레시 토큰 정보")
data class RefreshResponse(
    @Schema(description = "액세스 토큰", example = "AAAA.BBBB.CCCC")
    val accessToken: String,
) : SingleResponse() {
    companion object {
        fun of(userAuthToken: UserAuthToken) = RefreshResponse(
            accessToken = userAuthToken.accessToken.credentials,
        )
    }
}
