package net.bellsoft.bellsafehouse.controller.v1.auth

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import net.bellsoft.bellsafehouse.controller.v1.auth.dto.AccessResponse
import net.bellsoft.bellsafehouse.controller.v1.auth.dto.RefreshResponse
import net.bellsoft.bellsafehouse.controller.v1.auth.dto.RegisteredUserResponse
import net.bellsoft.bellsafehouse.controller.v1.auth.dto.UserLoginRequest
import net.bellsoft.bellsafehouse.controller.v1.auth.dto.UserRegistrationRequest
import net.bellsoft.bellsafehouse.exception.InvalidTokenException
import net.bellsoft.bellsafehouse.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@Tag(name = "인증", description = "인증 API")
@RestController
@Validated
@RequestMapping("/api/v1/auth")
class AuthController(
    val authService: AuthService,
) {
    @Operation(summary = "회원가입", description = "회원가입 처리")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201"),
        ],
    )
    @PostMapping("/register")
    fun registerUser(
        @RequestBody @Valid
        userRegistrationRequest: UserRegistrationRequest,
    ): ResponseEntity<RegisteredUserResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(authService.register(userRegistrationRequest))
    }

    @Operation(summary = "로그인", description = "로그인 성공시 쿠키에 Refresh Token 이 설정됨")
    @PostMapping("/refresh")
    fun performLogin(
        @RequestBody @Valid
        userLoginRequest: UserLoginRequest,
        httpServletResponse: HttpServletResponse,
    ): ResponseEntity<RefreshResponse> {
        val userAuthToken = authService.login(userLoginRequest)

        httpServletResponse.addCookie(userAuthToken.refreshTokenCookie)

        return ResponseEntity.ok().body(RefreshResponse.of(userAuthToken))
    }

    @Operation(summary = "액세스 토큰 재발급", description = "쿠키에 담긴 Refresh Token 을 사용하여 Access Token 재발급")
    @PostMapping("/access")
    fun getAccessToken(httpServletRequest: HttpServletRequest): ResponseEntity<AccessResponse> {
        val cookies = httpServletRequest.cookies
        val refreshToken = cookies?.firstOrNull { it.name == AuthService.REFRESH_TOKEN_NAME }?.value
            ?: throw InvalidTokenException("토큰이 존재하지 않습니다")

        val accessToken = authService.reissueAccessToken(refreshToken)

        return ResponseEntity.ok().body(AccessResponse(accessToken))
    }
}
