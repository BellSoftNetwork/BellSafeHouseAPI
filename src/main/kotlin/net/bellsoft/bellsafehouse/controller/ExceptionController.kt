package net.bellsoft.bellsafehouse.controller

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import mu.KLogging
import net.bellsoft.bellsafehouse.controller.dto.ErrorResponse
import net.bellsoft.bellsafehouse.exception.BadRequestException
import net.bellsoft.bellsafehouse.exception.InvalidTokenException
import net.bellsoft.bellsafehouse.exception.UnprocessableEntityException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionController {
    @ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotValidException(
        ex: MethodArgumentNotValidException,
    ): ResponseEntity<ErrorResponse> {
        val errors = ex.fieldErrors.map {
            "'${it.field}'은(는) ${it.defaultMessage} (요청 값: ${it.rejectedValue})"
        }

        return ResponseEntity
            .badRequest()
            .body(
                ErrorResponse(
                    message = "잘못된 요청",
                    errors = errors,
                ),
            )
    }

    @ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        logger.info(ex.message.toString())

        return ResponseEntity
            .badRequest()
            .body(ErrorResponse("JSON 파싱 실패"))
    }

    @ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(ex: BadRequestException): ResponseEntity<ErrorResponse> {
        logger.info(ex.message.toString())

        return ResponseEntity
            .badRequest()
            .body(ErrorResponse(ex.message.toString()))
    }

    @ApiResponse(responseCode = "422", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    @ExceptionHandler(UnprocessableEntityException::class)
    fun handleUnprocessableEntityException(ex: UnprocessableEntityException): ResponseEntity<ErrorResponse> {
        logger.error(ex.message.toString())

        return ResponseEntity
            .unprocessableEntity()
            .body(ErrorResponse(ex.message.toString()))
    }

    @ApiResponse(responseCode = "422", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<ErrorResponse> {
        logger.error(ex.message.toString())

        return ResponseEntity
            .unprocessableEntity()
            .body(ErrorResponse("처리할 수 없는 데이터"))
    }

    @ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUsernameNotFoundException(ex: UsernameNotFoundException): ResponseEntity<ErrorResponse> {
        logger.info(ex.message.toString())

        return ResponseEntity
            .badRequest()
            .body(ErrorResponse("유효하지 않은 계정"))
    }

    @ApiResponse(responseCode = "401", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    @ExceptionHandler(InvalidTokenException::class)
    fun handleInvalidTokenException(ex: InvalidTokenException): ResponseEntity<ErrorResponse> {
        logger.info(ex.message.toString())

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse(ex.message.toString()))
    }

    companion object : KLogging()
}
