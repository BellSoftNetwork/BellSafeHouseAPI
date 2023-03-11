package net.bellsoft.bellsafehouse.component.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import net.bellsoft.bellsafehouse.annotation.validator.OnlyOneField
import org.springframework.expression.spel.standard.SpelExpressionParser
import java.util.Objects

class OnlyOneParameterValidator(
    private var fields: Array<out String>,
) : ConstraintValidator<OnlyOneField, Any?> {
    override fun initialize(constraintAnnotation: OnlyOneField) {
        fields = constraintAnnotation.value
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        val notNull: Int = fields
            .map { field -> PARSER.parseExpression(field).getValue(value) }
            .count(Objects::nonNull)

        if (notNull == 1)
            return true

        context.disableDefaultConstraintViolation()
        context.buildConstraintViolationWithTemplate(MESSAGE_MUST_HAVE_ONLY_ONE_FIELD)
            .addConstraintViolation()
        return false
    }

    companion object {
        private val PARSER = SpelExpressionParser()
        private const val MESSAGE_MUST_HAVE_ONLY_ONE_FIELD = "하나의 필드에만 값이 존재해야 합니다."
    }
}
