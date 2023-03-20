package net.bellsoft.bellsafehouse.annotation.validator

import jakarta.validation.Constraint
import jakarta.validation.Payload
import net.bellsoft.bellsafehouse.component.validator.AtLeastOneParameterValidator
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [AtLeastOneParameterValidator::class])
annotation class AtLeastOneField(
    vararg val value: String,
    val message: String = "{AtLeastOneField.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)
