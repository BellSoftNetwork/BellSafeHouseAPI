package net.bellsoft.bellsafehouse.controller.v1.check.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import net.bellsoft.bellsafehouse.annotation.validator.OnlyOneField
import net.bellsoft.bellsafehouse.exception.UnprocessableEntityException
import org.springdoc.api.annotations.ParameterObject
import org.springframework.lang.Nullable
import javax.validation.constraints.Email
import javax.validation.constraints.Size
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

@Schema(description = "유저 존재 여부 확인 데이터")
@ParameterObject
@OnlyOneField("userId", "email", "nickname")
class UserCheckRequest(
    @field:Parameter(description = "사용자 계정 아이디", example = "testId")
    @field:Size(min = 4, max = 20)
    @field:Nullable
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val userId: String?,

    @field:Parameter(description = "사용자 이메일", example = "user@email.com")
    @field:Email
    @field:Nullable
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val email: String?,

    @field:Parameter(description = "사용자 별명", example = "testNickname")
    @field:Size(min = 2, max = 20)
    @field:Nullable
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val nickname: String?,
) {
    fun first(): KProperty1<UserCheckRequest, *> {
        try {
            return getNotNullParameterFields().first()
        } catch (ex: NoSuchElementException) {
            throw UnprocessableEntityException("Cannot find checkUser method")
        }
    }

    fun value() = first().get(this) as String

    private fun getNotNullParameterFields() = UserCheckRequest::class.declaredMemberProperties.filter {
        it.javaField?.getAnnotation(Parameter::class.java) != null && it.getter.call(this) != null
    }
}
