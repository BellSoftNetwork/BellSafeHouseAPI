package net.bellsoft.bellsafehouse.service.auth

import net.bellsoft.bellsafehouse.component.UserProvider
import net.bellsoft.bellsafehouse.component.UserTokenProvider
import net.bellsoft.bellsafehouse.component.jwt.BearerToken
import net.bellsoft.bellsafehouse.component.jwt.JwtSupport
import net.bellsoft.bellsafehouse.controller.v1.auth.dto.RegisteredUserResponse
import net.bellsoft.bellsafehouse.controller.v1.auth.dto.UserLoginRequest
import net.bellsoft.bellsafehouse.controller.v1.auth.dto.UserRegistrationRequest
import net.bellsoft.bellsafehouse.domain.user.UserRepository
import net.bellsoft.bellsafehouse.exception.InvalidTokenException
import net.bellsoft.bellsafehouse.exception.UnprocessableEntityException
import net.bellsoft.bellsafehouse.exception.UserNotFoundException
import net.bellsoft.bellsafehouse.service.dto.UserAuthToken
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val userProvider: UserProvider,
    private val userTokenProvider: UserTokenProvider,
    private val jwtSupport: JwtSupport,
) : UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByUserId(username)
            ?: throw UserNotFoundException("$username 은 존재하지 않는 사용자입니다")
    }

    fun register(userRegistrationRequest: UserRegistrationRequest): RegisteredUserResponse {
        try {
            val user = userRepository.save(userRegistrationRequest.toEntity())

            return RegisteredUserResponse(user.userId)
        } catch (ex: DataIntegrityViolationException) {
            throw UnprocessableEntityException("${userRegistrationRequest.userId} 로 가입할 수 없습니다")
        }
    }

    fun login(userLoginRequest: UserLoginRequest): UserAuthToken {
        val user = userProvider.findValidatedUser(userLoginRequest.userId, userLoginRequest.password)
        val userToken = userTokenProvider.issueUserToken(user.userId)
        val refreshToken = jwtSupport.generateRefreshToken(user.userId, userToken.id)
        val accessToken = jwtSupport.generateAccessToken(refreshToken, user.role)

        return UserAuthToken(
            user = user,
            refreshTokenCookie = jwtSupport.createRefreshTokenCookie(refreshToken),
            accessToken = accessToken,
        )
    }

    fun reissueAccessToken(refreshToken: String): String {
        val userId = jwtSupport.parseRefreshToken(refreshToken).userId
        val user = userRepository.findByUserId(userId)
            ?: throw InvalidTokenException("토큰을 가진 유저를 찾을 수 없음")

        return jwtSupport.generateAccessToken(BearerToken(refreshToken), user.role).credentials
    }

    companion object {
        const val REFRESH_TOKEN_NAME = "refreshToken"
    }
}
