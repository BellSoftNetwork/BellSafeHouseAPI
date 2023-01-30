package net.bellsoft.bellsafehouse.domain.user

import net.bellsoft.bellsafehouse.annotation.ExcludeFromJacocoGeneratedReport
import net.bellsoft.bellsafehouse.domain.base.BaseTime
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "user")
class User(
    @Column(name = "user_id", nullable = false, unique = true, updatable = false, length = 20)
    val userId: String,

    @Column(name = "password", nullable = false, length = 60)
    private var password: String,

    email: String,
    nickname: String,
    marketingAgreedAt: LocalDateTime? = null,
) : BaseTime(), UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    var id: Long = 0
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

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var userTokens: MutableList<UserToken> = mutableListOf()
        private set

    @ExcludeFromJacocoGeneratedReport
    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? {
        return null
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return userId
    }

    @ExcludeFromJacocoGeneratedReport
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    @ExcludeFromJacocoGeneratedReport
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    @ExcludeFromJacocoGeneratedReport
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    @ExcludeFromJacocoGeneratedReport
    override fun isEnabled(): Boolean {
        return true
    }

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

    override fun toString(): String {
        return "User(id=$id, userId='$userId', password='$password', email='$email', nickname='$nickname'," +
            " marketingAgreedAt=$marketingAgreedAt)"
    }
}
