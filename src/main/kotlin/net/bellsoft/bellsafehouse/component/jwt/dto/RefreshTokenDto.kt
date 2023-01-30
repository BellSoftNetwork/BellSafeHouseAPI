package net.bellsoft.bellsafehouse.component.jwt.dto

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import net.bellsoft.bellsafehouse.component.jwt.JwtTokenType
import net.bellsoft.bellsafehouse.component.jwt.RefreshTokenClaim
import java.util.UUID

data class RefreshTokenDto(
    override val userId: String,
    override val uuid: UUID,
) : TokenDto(userId, uuid) {
    companion object {
        fun ofJwsClaims(jws: Jws<Claims>): RefreshTokenDto {
            val claims = jws.body

            validateToken(claims, JwtTokenType.REFRESH)

            return RefreshTokenDto(
                userId = claims[RefreshTokenClaim.USER_ID.name].toString(),
                uuid = UUID.fromString(claims[RefreshTokenClaim.UUID.name].toString()),
            )
        }
    }
}
