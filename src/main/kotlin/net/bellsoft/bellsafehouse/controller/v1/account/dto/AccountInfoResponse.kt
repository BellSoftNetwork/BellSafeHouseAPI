package net.bellsoft.bellsafehouse.controller.v1.account.dto

import io.swagger.v3.oas.annotations.media.Schema
import net.bellsoft.bellsafehouse.domain.user.User
import java.time.LocalDateTime

@Schema(description = "계정 정보")
data class AccountInfoResponse(
    @Schema(description = "사용자 ID", example = "userid")
    val userId: String,

    @Schema(description = "이메일", example = "bell@bellsoft.net")
    val email: String,

    @Schema(description = "닉네임", example = "방울")
    val nickname: String,

    @Schema(description = "마케팅 메일 동의 여부", example = "true")
    val marketingAgreed: Boolean,

    @Schema(description = "가입일")
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(user: User): AccountInfoResponse {
            return AccountInfoResponse(user.userId, user.email, user.nickname, user.marketingAgreed, user.createdAt)
        }
    }
}
