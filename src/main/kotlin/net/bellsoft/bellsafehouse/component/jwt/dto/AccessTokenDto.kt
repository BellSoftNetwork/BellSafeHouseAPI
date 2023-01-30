package net.bellsoft.bellsafehouse.component.jwt.dto

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import net.bellsoft.bellsafehouse.component.jwt.AccessTokenClaim
import net.bellsoft.bellsafehouse.component.jwt.JwtTokenType
import java.util.UUID

data class AccessTokenDto(
    override val userId: String,
    override val uuid: UUID,
    val role: String,
) : TokenDto(userId, uuid) {
    companion object {
        fun ofJwsClaims(jws: Jws<Claims>): AccessTokenDto {
            val claims = jws.body

            validateToken(claims, JwtTokenType.ACCESS)

            return AccessTokenDto(
                userId = claims[AccessTokenClaim.USER_ID.name].toString(),
                uuid = UUID.fromString(claims[AccessTokenClaim.UUID.name].toString()),
                role = claims[AccessTokenClaim.ROLE.name].toString(),
            )
        }
    }
}
