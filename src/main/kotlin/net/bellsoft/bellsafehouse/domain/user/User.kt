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
    userId: String,
    password: String,
    email: String,
    nickname: String,
    marketingAgreedAt: LocalDateTime? = null,
) : BaseTime() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    var id: Long = 0
        private set

    @Column(name = "user_id", nullable = false, unique = true, updatable = false, length = 20)
    var userId: String = userId
        private set

    @Column(name = "password", nullable = false, length = 60)
    var password: String = password
        private set

    @Column(name = "email", nullable = false, unique = true, length = 100)
    var email: String = email
        private set

    @Column(name = "nickname", nullable = false, unique = true, length = 20)
    var nickname: String = nickname
        private set

    @Column(name = "marketing_agreed_at", nullable = true)
    var marketingAgreedAt: LocalDateTime? = marketingAgreedAt
        private set

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
