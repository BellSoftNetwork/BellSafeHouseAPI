package net.bellsoft.bellsafehouse.component.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import mu.KLogging
import net.bellsoft.bellsafehouse.component.jwt.dto.AccessTokenDto
import net.bellsoft.bellsafehouse.component.jwt.dto.RefreshTokenDto
import net.bellsoft.bellsafehouse.exception.InvalidAuthHeaderException
import net.bellsoft.bellsafehouse.exception.InvalidTokenException
import net.bellsoft.bellsafehouse.exception.TokenNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.UUID

@Component
class JwtSupport(
    @Value("\${security.jwt.signature-key}") private val jwtSignatureKey: ByteArray,
    @Value("\${security.jwt.refresh.expiration-minute}") private val refreshTokenExpirationMinute: Long,
    @Value("\${security.jwt.refresh.auto-renew-minute}") private val refreshTokenAutoRenewMinute: Long,
    @Value("\${security.jwt.access.expiration-minute}") private val accessTokenExpirationMinute: Long,
) {
    private val jwtSecretKey = Keys.hmacShaKeyFor(jwtSignatureKey)
    private val jwtParser = Jwts
        .parserBuilder()
        .setClock(JwtClock())
        .setSigningKey(jwtSecretKey)
        .build()

    // TODO: 여기서 발급한 리프레시 토큰을 토큰 프로바이더에서 브라우저 정보를 DB에 저장하여 로그인이 성공한 기기의 정보 기록
    // TODO: 프로바이더에서 쿠키에 주입시켜줘야 됨
    fun generateRefreshToken(userId: String, uuid: UUID): BearerToken {
        val builder = Jwts.builder()
            .setSubject(JwtTokenType.REFRESH.name)
            .claim(RefreshTokenClaim.USER_ID.name, userId)
            .claim(RefreshTokenClaim.UUID.name, uuid)
            .setIssuedAt(Date.from(getCurrentInstant()))
            .setExpiration(Date.from(getCurrentInstant().plus(refreshTokenExpirationMinute, ChronoUnit.MINUTES)))
            .signWith(jwtSecretKey)

        return BearerToken(builder.compact())
    }

    fun generateAccessToken(refreshToken: BearerToken): BearerToken {
        val refreshTokenDto = RefreshTokenDto.ofJwsClaims(parseClaimsJws(refreshToken.value))

        val builder = Jwts.builder()
            .setSubject(JwtTokenType.ACCESS.name)
            .claim(AccessTokenClaim.USER_ID.name, refreshTokenDto.userId)
            .claim(AccessTokenClaim.UUID.name, refreshTokenDto.uuid)
            .claim(AccessTokenClaim.ROLE.name, "TODO") // TODO
            .setIssuedAt(Date.from(getCurrentInstant()))
            .setExpiration(Date.from(getCurrentInstant().plus(accessTokenExpirationMinute, ChronoUnit.MINUTES)))
            .signWith(jwtSecretKey)

        return BearerToken(builder.compact())
    }

    fun getRefreshToken(refreshToken: String): RefreshTokenDto {
        return RefreshTokenDto.ofJwsClaims(parseClaimsJws(refreshToken))
    }

    fun getAccessToken(accessToken: String): AccessTokenDto {
        return AccessTokenDto.ofJwsClaims(parseClaimsJws(accessToken))
    }

    fun getAccessToken(request: HttpServletRequest): String {
        try {
            val authorizationHeader = getAuthToken(request)

            if (authorizationHeader.first == BEARER_TOKEN_HEADER_NAME)
                return authorizationHeader.second
        } catch (_: InvalidAuthHeaderException) {
            logger.error("Unable to found authorization header named '$BEARER_TOKEN_HEADER_NAME' on request '$request'")
        }

        throw TokenNotFoundException()
    }

    fun getAuthToken(request: HttpServletRequest): Pair<String, String> {
        try {
            val authorizationHeader = request.getHeader(AUTHORIZATION_HEADER_NAME)
                ?: throw InvalidAuthHeaderException()
            val (authKey, authToken) = authorizationHeader.split(" ", limit = 2)

            return authKey to authToken
        } catch (ex: IndexOutOfBoundsException) {
            throw InvalidAuthHeaderException()
        }
    }

    fun isWillRefreshTokenExpires(token: BearerToken): Boolean {
        val tokenExpirationAt = parseClaimsJws(token.value).body.expiration
        val renewBaseAt = tokenExpirationAt.toInstant().minus(refreshTokenAutoRenewMinute, ChronoUnit.MINUTES)

        return getCurrentInstant().isAfter(renewBaseAt)
    }

    fun createRefreshTokenCookie(refreshToken: BearerToken, isSecure: Boolean = true): Cookie {
        return Cookie(REFRESH_TOKEN_NAME, refreshToken.credentials).apply {
            this.maxAge = refreshTokenExpirationMinute.toInt()
            this.isHttpOnly = true
            this.path = "/"
            this.secure = isSecure
        }
    }

    fun parseClaimsJws(token: String): Jws<Claims> {
        try {
            return jwtParser.parseClaimsJws(token)
        } catch (ex: UnsupportedJwtException) {
            throw InvalidTokenException("지원되지 않는 인증 토큰")
        } catch (ex: MalformedJwtException) {
            throw InvalidTokenException("유효한 인증 토큰 형식이 아님")
        } catch (ex: SignatureException) {
            throw InvalidTokenException("인증 토큰 발급처 불일치")
        } catch (ex: ExpiredJwtException) {
            throw InvalidTokenException("만료된 인증 토큰")
        } catch (ex: IllegalArgumentException) {
            throw InvalidTokenException("인증 토큰 인자 누락")
        }
    }

    companion object : KLogging() {
        private const val REFRESH_TOKEN_NAME = "refreshToken"

        private const val AUTHORIZATION_HEADER_NAME = "Authorization"
        private const val BEARER_TOKEN_HEADER_NAME = "Bearer"

        private fun getCurrentInstant(): Instant = Instant.now()
    }
}
