package net.bellsoft.bellsafehouse.controller.v1.account.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import net.bellsoft.bellsafehouse.annotation.validator.AtLeastOneField
import org.springframework.lang.Nullable

@Schema(name = "계정 수정 요청 정보")
@AtLeastOneField("password", "email", "nickname", "marketingAgreed")
data class AccountEditRequest(
    @Schema(description = "변경 할 비밀번호", example = "passwordToChange")
    @field:Size(min = 8, max = 20)
    @field:Nullable
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val password: String?,

    @Schema(description = "변경 할 이메일 주소", example = "changed@bellsoft.net")
    @field:Email
    @field:Nullable
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val email: String?,

    @Schema(description = "변경 할 닉네임", example = "변경할닉네임")
    @field:Size(min = 2, max = 20)
    @field:Nullable
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val nickname: String?,

    @Schema(description = "변경 할 마케팅 메일 동의 여부", defaultValue = "true")
    @field:Nullable
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val marketingAgreed: Boolean?,
)
