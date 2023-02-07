package net.bellsoft.bellsafehouse.annotation.validator

import net.bellsoft.bellsafehouse.component.validator.OnlyOneParameterValidator
import javax.validation.Constraint
import javax.validation.Payload
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
