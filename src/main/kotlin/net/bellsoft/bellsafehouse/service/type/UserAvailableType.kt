package net.bellsoft.bellsafehouse.service.type

import com.fasterxml.jackson.annotation.JsonValue

enum class UserAvailableType(val value: String) {
    AVAILABLE("available"),
    DUPLICATED("duplicated"),
    DELETED("deleted"),
    ;

    @JsonValue
    override fun toString(): String = value
}
