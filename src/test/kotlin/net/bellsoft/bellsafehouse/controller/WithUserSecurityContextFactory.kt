package net.bellsoft.bellsafehouse.controller

import net.bellsoft.bellsafehouse.domain.user.User
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory
import java.time.LocalDateTime

class WithUserSecurityContextFactory : WithSecurityContextFactory<WithUser> {
    override fun createSecurityContext(annotation: WithUser): SecurityContext {
        val user = User(
            annotation.userId,
            annotation.password,
            annotation.email,
            annotation.nickname,
            if (annotation.marketingAgreedAt) LocalDateTime.now() else null,
        )

        val context = SecurityContextHolder.getContext()
        context.authentication = UsernamePasswordAuthenticationToken(user, "", user.authorities)

        return context
    }
}
