package net.bellsoft.bellsafehouse.exception

import org.springframework.dao.DataIntegrityViolationException

class BadRequestException(message: String) : Exception(message)
class UnprocessableEntityException(message: String) : DataIntegrityViolationException(message)
