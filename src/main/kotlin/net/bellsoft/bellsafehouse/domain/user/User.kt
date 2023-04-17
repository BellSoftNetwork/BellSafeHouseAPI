package net.bellsoft.bellsafehouse.domain.user

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import net.bellsoft.bellsafehouse.annotation.ExcludeFromJacocoGeneratedReport
import net.bellsoft.bellsafehouse.domain.base.BaseTime
import org.hibernate.annotations.Filter
import org.hibernate.annotations.FilterDef
import org.hibernate.annotations.SQLDelete
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "user")
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
@FilterDef(name = "deletedEntityFilter", defaultCondition = "deleted_at IS NULL")
@Filter(name = "deletedEntityFilter")
class User(
    @Column(name = "user_id", nullable = false, unique = true, updatable = false, length = 20)
    val userId: String,

    @Column(name = "password", nullable = false, length = 60)
    private var password: String,

    email: String,
    nickname: String,
    marketingAgreed: Boolean = false,
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
    var marketingAgreedAt: LocalDateTime? = if (marketingAgreed) LocalDateTime.now() else null
        private set

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var userTokens: MutableList<UserToken> = mutableListOf()
        private set

    var marketingAgreed: Boolean
        get() = marketingAgreedAt != null
        set(value) {
            if (value && marketingAgreedAt != null)
                return

            marketingAgreedAt = if (value) LocalDateTime.now() else null
        }

    @Column(name = "reset_token", nullable = true, columnDefinition = "binary(16)")
    var resetToken: UUID? = null

    @Column(name = "reset_token_created_at", nullable = true)
    var resetTokenCreatedAt: LocalDateTime? = null

    fun withdraw() {
        softDelete()
    }

    @ExcludeFromJacocoGeneratedReport
    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? {
        return null
    }

    override fun getPassword(): String {
        return password
    }

    fun setPassword(password: String) {
        this.password = PASSWORD_ENCODER.encode(password)
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

    override fun toString(): String {
        return "User(id=$id, userId='$userId', password='$password', email='$email', nickname='$nickname'," +
            " marketingAgreedAt=$marketingAgreedAt, deletedAt=$deletedAt)"
    }

    companion object {
        private val PASSWORD_ENCODER = BCryptPasswordEncoder()
    }
}
