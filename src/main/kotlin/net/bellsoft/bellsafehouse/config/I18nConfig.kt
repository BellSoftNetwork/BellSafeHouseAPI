package net.bellsoft.bellsafehouse.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import java.util.Locale

@Configuration
class I18nConfig(
    @Value("\${i18n.basename}") private val i18nBasename: String,
    @Value("\${i18n.encoding}") private val i18nDefaultEncoding: String,
    @Value("\${i18n.cacheSeconds}") private val i18nCacheSeconds: Int,
) {
    @Bean
    fun messageSource(): ReloadableResourceBundleMessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setBasename(i18nBasename)
        messageSource.setDefaultEncoding(i18nDefaultEncoding)
        messageSource.setCacheSeconds(i18nCacheSeconds)
        return messageSource
    }

    @Bean
    fun defaultLocale(): Locale = Locale.KOREA

    @Bean
    fun messageSourceAccessor(messageSource: MessageSource, defaultLocale: Locale): MessageSourceAccessor {
        return MessageSourceAccessor(messageSource, defaultLocale)
    }
}
