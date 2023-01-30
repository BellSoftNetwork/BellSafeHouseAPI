package net.bellsoft.bellsafehouse.component

import net.bellsoft.bellsafehouse.domain.user.UserRepository
import net.bellsoft.bellsafehouse.domain.user.UserToken
import net.bellsoft.bellsafehouse.domain.user.UserTokenRepository
import net.bellsoft.bellsafehouse.exception.UserNotFoundException
import org.springframework.stereotype.Component

@Component
class UserTokenProvider(
    private val userRepository: UserRepository,
    private val userTokenRepository: UserTokenRepository,
) {
    fun issueUserToken(userId: String): UserToken {
        val user = userRepository.findByUserId(userId)
            ?: throw UserNotFoundException("존재하지 않는 사용자 계정: $userId")

        return userTokenRepository.save(UserToken(user = user))
    }
}
