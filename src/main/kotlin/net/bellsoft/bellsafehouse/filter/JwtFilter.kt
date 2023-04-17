package net.bellsoft.bellsafehouse.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.bellsoft.bellsafehouse.component.UserTokenProvider
import net.bellsoft.bellsafehouse.component.jwt.BearerToken
import net.bellsoft.bellsafehouse.component.jwt.JwtSupport
import net.bellsoft.bellsafehouse.exception.InvalidTokenException
import net.bellsoft.bellsafehouse.exception.TokenNotFoundException
import net.bellsoft.bellsafehouse.service.auth.AuthService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

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
        val accessToken = jwtSupport.getAccessToken(request)
        val userDetails = authService.loadUserByUsername(jwtSupport.parseAccessToken(accessToken).userId)

        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    private fun tryReissueRefreshToken(request: HttpServletRequest, response: HttpServletResponse) {
        val refreshToken = getRefreshToken(request) ?: throw TokenNotFoundException()
        val refreshTokenDto = jwtSupport.parseRefreshToken(refreshToken)
        val bearerToken = BearerToken(refreshToken)

        if (jwtSupport.isWillRefreshTokenExpires(bearerToken))
            reissueRefreshToken(refreshTokenDto.userId, response)
    }

    private fun getRefreshToken(request: HttpServletRequest) =
        request.cookies?.firstOrNull { it.name == AuthService.REFRESH_TOKEN_NAME }?.value

    private fun reissueRefreshToken(userId: String, response: HttpServletResponse) {
        val userToken = userTokenProvider.issueUserToken(userId)
        val newRefreshToken = jwtSupport.generateRefreshToken(userId, userToken.id)

        response.addCookie(jwtSupport.createRefreshTokenCookie(newRefreshToken))
    }
}
