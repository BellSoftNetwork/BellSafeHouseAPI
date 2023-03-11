package net.bellsoft.bellsafehouse.annotation.validator

import jakarta.validation.Constraint
import jakarta.validation.Payload
import net.bellsoft.bellsafehouse.component.validator.OnlyOneParameterValidator
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [OnlyOneParameterValidator::class])
annotation class OnlyOneField(
    vararg val value: String,
    val message: String = "{OnlyOneField.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)
