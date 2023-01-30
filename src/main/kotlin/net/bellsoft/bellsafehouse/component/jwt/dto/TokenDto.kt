package net.bellsoft.bellsafehouse.component.jwt.dto

import io.jsonwebtoken.Claims
import net.bellsoft.bellsafehouse.component.jwt.JwtTokenType
import net.bellsoft.bellsafehouse.exception.InvalidTokenException
import java.util.UUID

open class TokenDto(
    open val userId: String,
    open val uuid: UUID,
) {
    companion object {
        fun validateToken(claims: Claims, tokenType: JwtTokenType) {
            try {
                val isValidTokenType = JwtTokenType.valueOf(claims.subject) == tokenType

                if (!isValidTokenType)
                    throw InvalidTokenException("Invalid token")
            } catch (ex: IllegalArgumentException) {
                throw InvalidTokenException("Invalid token type")
            }
        }
    }
}
