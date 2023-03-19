package net.bellsoft.bellsafehouse.controller

import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithUserSecurityContextFactory::class)
annotation class WithUser(
    val userId: String = "userId",
    val password: String = "password",
    val email: String = "user@email.com",
    val nickname: String = "userNickname",
    val marketingAgreedAt: Boolean = false,
)
