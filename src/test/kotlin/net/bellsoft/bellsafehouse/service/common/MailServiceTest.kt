package net.bellsoft.bellsafehouse.service.common

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldStartWith
import io.mockk.every
import io.mockk.slot
import jakarta.mail.Message
import jakarta.mail.Session
import jakarta.mail.internet.MimeMessage
import net.bellsoft.bellsafehouse.config.I18nConfig
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.test.context.ActiveProfiles
import java.io.ByteArrayOutputStream
import java.util.Locale
import java.util.Properties
import java.util.UUID

@SpringBootTest
@Import(I18nConfig::class)
@ActiveProfiles("test")
class MailServiceTest(
    private val mailService: MailService,
    private val messageSourceAccessor: MessageSourceAccessor,
    @MockkBean private val mailSender: JavaMailSenderImpl,
) : BehaviorSpec(
    {
        Given("메일 메시지 내용을 빌드할 때") {
            When("임의의 변수와 로케일로 테스트 컨텍스트를 빌드하면") {
                val variables = mapOf("var1" to "This is var1", "var2" to "This is var2")
                val context = mailService.buildContext(variables, Locale.ENGLISH)

                Then("성공적으로 컨텍스트가 빌드된다") {

                    context.locale shouldBe Locale.ENGLISH
                    context.getVariable("var1") shouldBe "This is var1"
                    context.getVariable("var2") shouldBe "This is var2"
                }
            }

            When("테스트 컨텍스트와 테스트 템플릿으로 메일 발송을 요청하면") {
                val randomToken = UUID.randomUUID()
                val variables = mapOf("random" to randomToken.toString())
                val context = mailService.buildContext(variables, Locale.ENGLISH)

                val slot = slot<MimeMessage>()

                every { mailSender.send(capture(slot)) } returns Unit
                every { mailSender.createMimeMessage() } returns MimeMessage(Session.getDefaultInstance(Properties()))

                mailService.sendMail("to@example.com", "Hello", "test-template", context)

                Then("메시지가 정상적으로 빌드된다") {
                    slot.isCaptured shouldBe true
                }

                Then("수신자가 정상적으로 표시된다") {
                    slot.captured.getRecipients(Message.RecipientType.TO).first().toString() shouldBe "to@example.com"
                }

                Then("제목이 \"[접두사] 제목\" 형식으로 표시된다") {
                    val (subjectPrefix, subject) = slot.captured.subject.split("] ")
                    subjectPrefix shouldStartWith "["
                    subjectPrefix.length shouldBeGreaterThan 1
                    subject shouldBe "Hello"
                }

                Then("메일 내용에 랜덤 토큰 및 애플리케이션 이름이 정상적으로 표시된다") {
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    slot.captured.writeTo(byteArrayOutputStream)
                    val mailBody = byteArrayOutputStream.toString()
                    mailBody shouldContain "<p>${messageSourceAccessor.getMessage("application.name")}</p>"
                    mailBody shouldContain "<p>$randomToken</p>"
                }
            }
        }
    },
)
