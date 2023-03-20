package net.bellsoft.bellsafehouse.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import net.bellsoft.bellsafehouse.controller.v1.account.dto.AccountEditRequest
import net.bellsoft.bellsafehouse.controller.v1.account.dto.AccountInfoResponse
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

    fun update(user: User, accountEditRequest: AccountEditRequest) {
        val editRequestWithoutMarketingAgree = accountEditRequest.copy()
        val objectMapper = jacksonObjectMapper()
        val objectReader = objectMapper.readerForUpdating(user)
        val editRequestJsonString = objectMapper.writeValueAsString(editRequestWithoutMarketingAgree)
        val updatedUser = objectReader.readValue<User>(editRequestJsonString)

        userRepository.save(updatedUser)
    }

    fun getInfo(user: User): AccountInfoResponse {
        return AccountInfoResponse.of(user)
    }
}
