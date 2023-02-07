package net.bellsoft.bellsafehouse.service

import net.bellsoft.bellsafehouse.controller.v1.check.dto.UserCheckRequest
import net.bellsoft.bellsafehouse.domain.user.UserRepository
import net.bellsoft.bellsafehouse.exception.UnprocessableEntityException
import net.bellsoft.bellsafehouse.service.type.UserAvailableType
import org.springframework.stereotype.Service

@Service
class CheckService(
    private val userRepository: UserRepository,
) {

    private fun checkUserByUserId(userId: String): UserAvailableType {
        if (userRepository.existsByUserId(userId))
            return UserAvailableType.DUPLICATED

        return UserAvailableType.AVAILABLE
    }

    private fun checkUserByEmail(email: String): UserAvailableType {
        if (userRepository.existsByEmail(email))
            return UserAvailableType.DUPLICATED

        return UserAvailableType.AVAILABLE
    }

    private fun checkUserByNickname(nickname: String): UserAvailableType {
        if (userRepository.existsByNickname(nickname))
            return UserAvailableType.DUPLICATED

        return UserAvailableType.AVAILABLE
    }

    fun checkUser(userCheckRequest: UserCheckRequest): UserAvailableType {
        return when (userCheckRequest.first()) {
            UserCheckRequest::userId -> checkUserByUserId(userCheckRequest.value())
            UserCheckRequest::email -> checkUserByEmail(userCheckRequest.value())
            UserCheckRequest::nickname -> checkUserByNickname(userCheckRequest.value())
            else -> throw UnprocessableEntityException("Cannot find checkUser method")
        }
    }
}
