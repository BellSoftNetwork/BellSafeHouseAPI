package net.bellsoft.bellsafehouse.filter

import net.bellsoft.bellsafehouse.component.UserTokenProvider
import net.bellsoft.bellsafehouse.component.jwt.BearerToken
import net.bellsoft.bellsafehouse.component.jwt.JwtSupport
import net.bellsoft.bellsafehouse.exception.InvalidAuthHeaderException
import net.bellsoft.bellsafehouse.exception.InvalidTokenException
import net.bellsoft.bellsafehouse.exception.TokenNotFoundException
import net.bellsoft.bellsafehouse.service.AuthService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtFilter(
    private val jwtSupport: JwtSupport,
    private val userTokenProvider: UserTokenProvider,
    private val authService: AuthService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            authenticateRequest(request)
            tryReissueRefreshToken(request, response)
        } catch (_: InvalidTokenException) {
        } finally {
            filterChain.doFilter(request, response)
        }
    }

    private fun authenticateRequest(request: HttpServletRequest) {
        SecurityContextHolder.getContext().authentication = getAuthentication(request)
    }

    private fun getAuthentication(request: HttpServletRequest): Authentication {
        val accessToken = getAccessToken(request) ?: throw TokenNotFoundException()
        val userDetails = authService.loadUserByUsername(jwtSupport.getAccessToken(accessToken).userId)

        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    private fun getAccessToken(request: HttpServletRequest): String? {
        try {
            val authorizationHeader = getAuthToken(request)

            if (authorizationHeader.first == BEARER_TOKEN_HEADER_NAME)
                return authorizationHeader.second
        } catch (_: InvalidAuthHeaderException) {
            return null
        }

        return null
    }

    private fun getAuthToken(request: HttpServletRequest): Pair<String, String> {
        try {
            val authorizationHeader = request.getHeader(AUTHORIZATION_HEADER_NAME)
                ?: throw InvalidAuthHeaderException()
            val (authKey, authToken) = authorizationHeader.split(" ", limit = 2)

            return authKey to authToken
        } catch (ex: IndexOutOfBoundsException) {
            throw InvalidAuthHeaderException()
        }
    }

    private fun tryReissueRefreshToken(request: HttpServletRequest, response: HttpServletResponse) {
        val refreshToken = getRefreshToken(request) ?: throw TokenNotFoundException()
        val refreshTokenDto = jwtSupport.getRefreshToken(refreshToken)
        val bearerToken = BearerToken(refreshToken)

        if (jwtSupport.isWillRefreshTokenExpires(bearerToken))
            reissueRefreshToken(refreshTokenDto.userId, response)
    }

    private fun getRefreshToken(request: HttpServletRequest) =
        request.cookies.firstOrNull { it.name == AuthService.REFRESH_TOKEN_NAME }?.value

    private fun reissueRefreshToken(userId: String, response: HttpServletResponse) {
        val userToken = userTokenProvider.issueUserToken(userId)
        val newRefreshToken = jwtSupport.generateRefreshToken(userId, userToken.id)

        response.addCookie(jwtSupport.createRefreshTokenCookie(newRefreshToken))
    }

    companion object {
        private const val AUTHORIZATION_HEADER_NAME = "Authorization"
        private const val BEARER_TOKEN_HEADER_NAME = "Bearer"
    }
}
