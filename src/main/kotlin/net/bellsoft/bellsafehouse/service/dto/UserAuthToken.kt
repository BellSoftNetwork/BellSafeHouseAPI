package net.bellsoft.bellsafehouse.service.dto

import jakarta.servlet.http.Cookie
import net.bellsoft.bellsafehouse.component.jwt.BearerToken
import net.bellsoft.bellsafehouse.domain.user.User

data class UserAuthToken(
    val user: User,
    val refreshTokenCookie: Cookie,
    val accessToken: BearerToken,
)
