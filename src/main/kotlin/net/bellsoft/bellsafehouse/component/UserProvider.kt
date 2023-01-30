package net.bellsoft.bellsafehouse.component

import net.bellsoft.bellsafehouse.domain.user.User
import net.bellsoft.bellsafehouse.domain.user.UserRepository
import net.bellsoft.bellsafehouse.exception.PasswordMismatchException
import net.bellsoft.bellsafehouse.exception.UserNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserProvider(
    private val userRepository: UserRepository,
) {
    fun findValidatedUser(userId: String, password: String): User {
        val user = userRepository.findByUserId(userId)
            ?: throw UserNotFoundException("$userId 는 존재하지 않는 아이디입니다")

        if (!PASSWORD_ENCODER.matches(password, user.password))
            throw PasswordMismatchException("비밀번호가 일치하지 않습니다")

        return user
    }

    companion object {
        private val PASSWORD_ENCODER = BCryptPasswordEncoder()
    }
}
