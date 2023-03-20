package net.bellsoft.bellsafehouse.component.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import net.bellsoft.bellsafehouse.annotation.validator.AtLeastOneField
import org.springframework.expression.spel.standard.SpelExpressionParser
import java.util.Objects

class AtLeastOneParameterValidator(
    private var fields: Array<out String>,
) : ConstraintValidator<AtLeastOneField, Any?> {
    override fun initialize(constraintAnnotation: AtLeastOneField) {
        fields = constraintAnnotation.value
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        val notNull: Int = fields
            .map { field -> PARSER.parseExpression(field).getValue(value) }
            .count(Objects::nonNull)

        if (notNull > 0)
            return true

        context.disableDefaultConstraintViolation()
        context.buildConstraintViolationWithTemplate(MESSAGE_MUST_HAVE_AT_LEAST_ONE_FIELD)
            .addConstraintViolation()
        return false
    }

    companion object {
        private val PARSER = SpelExpressionParser()
        private const val MESSAGE_MUST_HAVE_AT_LEAST_ONE_FIELD = "최소한 하나의 필드에는 값이 존재해야 합니다."
    }
}
