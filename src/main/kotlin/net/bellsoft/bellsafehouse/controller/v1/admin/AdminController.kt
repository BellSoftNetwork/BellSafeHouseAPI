package net.bellsoft.bellsafehouse.controller.v1.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KLogging
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "관리", description = "관리 API (관리자 권한 필요)")
@RestController
@Validated
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/admin")
@Secured("ADMIN")
class AdminController {
    @Operation(summary = "어드민 권한 확인 페이지", description = "어드민 페이지 접근 권한 확인")
    @GetMapping
    fun displayAdminPage(): String {
        logger.info("Admin Page Call")

        return "Admin Page"
    }

    companion object : KLogging()
}
