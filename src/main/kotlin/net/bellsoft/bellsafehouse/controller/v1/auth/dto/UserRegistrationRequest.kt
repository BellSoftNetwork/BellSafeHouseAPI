package net.bellsoft.bellsafehouse.controller.v1.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import net.bellsoft.bellsafehouse.domain.user.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDateTime
import javax.validation.constraints.Email
import javax.validation.constraints.Size

@Schema(description = "회원가입 요청 정보")
data class UserRegistrationRequest(
    @Schema(description = "사용자 ID", example = "userid")
    @field:Size(min = 4, max = 20)
    val userId: String,

    @Schema(description = "비밀번호", example = "password!@#")
    @field:Size(min = 8, max = 20)
    val password: String,

    @Schema(description = "이메일", example = "bell@bellsoft.net")
    @field:Email
    val email: String,

    @Schema(description = "닉네임", example = "방울")
    @field:Size(min = 2, max = 20)
    val nickname: String,

    @Schema(description = "마케팅 메일 동의 여부", defaultValue = "true")
    val marketingAgreedAt: Boolean = true,
) {
    fun toEntity(): User {
        return User(
            userId = userId,
            password = PASSWORD_ENCODER.encode(password),
            email = email,
            nickname = nickname,
            marketingAgreedAt = if (marketingAgreedAt) LocalDateTime.now() else null,
        )
    }

    companion object {
        private val PASSWORD_ENCODER = BCryptPasswordEncoder()
    }
}
