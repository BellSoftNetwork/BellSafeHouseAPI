package net.bellsoft.bellsafehouse.service.dto

import net.bellsoft.bellsafehouse.component.jwt.BearerToken
import net.bellsoft.bellsafehouse.domain.user.User
import javax.servlet.http.Cookie

data class UserAuthToken(
    val user: User,
    val refreshTokenCookie: Cookie,
    val accessToken: BearerToken,
)
