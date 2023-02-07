package net.bellsoft.bellsafehouse.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogging
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

class QueryParamsConverter {
    companion object : KLogging() {
        fun convert(dto: Any, objectMapper: ObjectMapper): MultiValueMap<String, String> {
            try {
                val typeReference = object : TypeReference<Map<String, String>>() {}
                val dtoMap = objectMapper.convertValue(dto, typeReference)

                return LinkedMultiValueMap<String, String>().apply { setAll(dtoMap) }
            } catch (ex: Exception) {
                val errorMessage = "$dto 를 MultiValueMap 으로 변환할 수 없습니다. ${ex.message}"
                logger.error(errorMessage)
                throw IllegalArgumentException(errorMessage)
            }
        }
    }
}
