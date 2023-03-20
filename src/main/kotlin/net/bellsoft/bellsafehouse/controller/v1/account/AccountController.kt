package net.bellsoft.bellsafehouse.controller.v1.account

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import mu.KLogging
import net.bellsoft.bellsafehouse.controller.v1.account.dto.AccountEditRequest
import net.bellsoft.bellsafehouse.controller.v1.account.dto.AccountInfoResponse
import net.bellsoft.bellsafehouse.domain.user.User
import net.bellsoft.bellsafehouse.service.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "계정", description = "계정 관리 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@Validated
@RequestMapping("/v1/account")
class AccountController(
    private val accountService: AccountService,
) {
    @Operation(summary = "계정 탈퇴", description = "현재 로그인 된 계정 탈퇴")
    @DeleteMapping
    fun deleteUser(@AuthenticationPrincipal user: User): ResponseEntity<Nothing> {
        runCatching {
            accountService.deleteUser(user)
            return ResponseEntity.ok().build()
        }

        return ResponseEntity.badRequest().build()
    }

    @Operation(summary = "계정 정보 수정", description = "현재 로그인 된 계정 정보 수정")
    @PatchMapping
    fun modifyUser(
        @AuthenticationPrincipal user: User,
        @RequestBody @Valid
        accountEditRequest: AccountEditRequest,
    ): ResponseEntity<Nothing> {
        runCatching {
            accountService.update(user, accountEditRequest)
            return ResponseEntity.ok().build()
        }

        return ResponseEntity.badRequest().build()
    }

    @Operation(summary = "계정 정보 확인", description = "현재 로그인 된 계정 정보 확인")
    @GetMapping
    fun viewUser(@AuthenticationPrincipal user: User): ResponseEntity<AccountInfoResponse> {
        runCatching {
            return ResponseEntity.ok().body(accountService.getInfo(user))
        }

        return ResponseEntity.badRequest().build()
    }

    companion object : KLogging()
}
