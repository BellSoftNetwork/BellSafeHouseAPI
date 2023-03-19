package net.bellsoft.bellsafehouse.service

import net.bellsoft.bellsafehouse.domain.user.User
import net.bellsoft.bellsafehouse.domain.user.UserRepository
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val userRepository: UserRepository,
) {
    fun deleteUser(user: User) {
        userRepository.deleteById(user.id)
    }
}
