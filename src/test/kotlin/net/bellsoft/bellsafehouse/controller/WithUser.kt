package net.bellsoft.bellsafehouse.controller

import net.bellsoft.bellsafehouse.domain.user.UserRole
import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithUserSecurityContextFactory::class)
annotation class WithUser(
    val userId: String = "userId",
    val password: String = "password",
    val email: String = "user@email.com",
    val nickname: String = "userNickname",
    val marketingAgreed: Boolean = false,
    val role: UserRole = UserRole.NORMAL,
)
