package net.bellsoft.bellsafehouse.service.auth

import com.github.f4b6a3.ulid.UlidCreator
import net.bellsoft.bellsafehouse.controller.v1.auth.password.dto.PerformResetRequest
import net.bellsoft.bellsafehouse.controller.v1.auth.password.dto.SendResetMailRequest
import net.bellsoft.bellsafehouse.domain.user.User
import net.bellsoft.bellsafehouse.domain.user.UserRepository
import net.bellsoft.bellsafehouse.exception.InvalidTokenException
import net.bellsoft.bellsafehouse.service.common.MailService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

@Service
class PasswordService(
    private val mailService: MailService,
    private val messageSourceAccessor: MessageSourceAccessor,
    private val userRepository: UserRepository,
    @Value("\${frontend_url.reset}") private val resetUrl: String,
) {
    fun sendReset(sendResetMailRequest: SendResetMailRequest) {
        val user = userRepository.findByEmail(sendResetMailRequest.email) ?: return
        val token = generateResetToken(user)
        val variables = mapOf("reset_url" to resetUrl, "token" to token.toString())

        mailService.sendMail(
            sendResetMailRequest.email,
            messageSourceAccessor.getMessage("service.reset.mail.subject"),
            "reset-request",
            mailService.buildContext(variables),
        )
    }

    private fun generateResetToken(user: User): UUID {
        val token = UlidCreator.getMonotonicUlid().toUuid()
        user.resetToken = token
        user.resetTokenCreatedAt = LocalDateTime.now()
        userRepository.save(user)
        return token
    }

    fun performReset(performResetRequest: PerformResetRequest) {
        val user = validateResetToken(performResetRequest.resetToken)
        user.password = performResetRequest.newPassword
        userRepository.save(user)

        invalidateResetToken(user)
    }

    private fun invalidateResetToken(user: User) {
        user.resetToken = null
        userRepository.save(user)
    }

    fun validateResetToken(resetToken: String): User {
        val user = userRepository.findByResetToken(UUID.fromString(resetToken))
        val tokenExpiredAt = user?.resetTokenCreatedAt?.plus(10, ChronoUnit.MINUTES)

        if (user != null && LocalDateTime.now().isBefore(tokenExpiredAt))
            return user

        throw InvalidTokenException("잘못된 비밀번호 초기화 토큰")
    }
}
