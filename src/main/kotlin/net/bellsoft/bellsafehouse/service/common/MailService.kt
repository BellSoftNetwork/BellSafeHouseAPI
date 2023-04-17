package net.bellsoft.bellsafehouse.service.common

import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import java.util.Locale

@Service
class MailService(
    @Value("\${SMTP_FROM:test_from@bellsafehouse.com}") private val senderEmail: String,
    private val mailSender: JavaMailSenderImpl,
    private val templateEngine: SpringTemplateEngine,
    private val messageSourceAccessor: MessageSourceAccessor,
    private val defaultLocale: Locale,
) {
    fun sendMail(to: String, subject: String, templateName: String, context: Context) {
        val message = buildMessage(to, subject, templateName, context)
        mailSender.send(message)
    }

    private fun buildMessage(to: String, subject: String, templateName: String, context: Context): MimeMessage {
        val mimeMessage = mailSender.createMimeMessage()
        val subjectPrefix = messageSourceAccessor.getMessage("application.name")

        MimeMessageHelper(mimeMessage, true).apply {
            setFrom(senderEmail)
            setTo(to)
            setSubject("[$subjectPrefix] $subject")
            setText(templateEngine.process(templateName, context), true)
        }

        return mimeMessage
    }

    fun buildContext(variables: Map<String, String>, locale: Locale = defaultLocale): Context {
        return Context().apply {
            setLocale(locale)
            variables.forEach {
                setVariable(it.key, it.value)
            }
        }
    }
}
