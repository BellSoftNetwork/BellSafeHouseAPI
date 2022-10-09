package net.bellsoft.bellsafehouse.domain.user

import net.bellsoft.bellsafehouse.domain.base.BaseTime
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "user")
class User(
    @Column(name = "user_id", nullable = false, unique = true, updatable = false, length = 20)
    val userId: String,

    @Column(name = "password", nullable = false, length = 60)
    var password: String,

    @Column(name = "email", nullable = false, unique = true, length = 100)
    var email: String,

    @Column(name = "nickname", nullable = false, unique = true, length = 20)
    var nickname: String,

    @Column(name = "marketing_agreed_at", nullable = true)
    var marketingAgreedAt: LocalDateTime?,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    var id: Long? = null,
) : BaseTime() {
    fun isMarketingAgree(): Boolean {
        return marketingAgreedAt != null
    }

    fun agreeToMarketing() {
        marketingAgreedAt = LocalDateTime.now()
    }

    fun disagreeToMarketing() {
        marketingAgreedAt = null
    }

    fun withdraw() {
        softDelete()
    }

    companion object {
        fun fixture(
            userId: String = "userId",
            password: String = "password",
            email: String = "email",
            nickname: String = "nickname",
            marketingAgreedAt: LocalDateTime? = null,
        ): User {
            return User(
                userId = userId,
                password = password,
                email = email,
                nickname = nickname,
                marketingAgreedAt = marketingAgreedAt,
            )
        }
    }
}
