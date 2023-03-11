package net.bellsoft.bellsafehouse.domain.user

import com.github.f4b6a3.ulid.UlidCreator
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import net.bellsoft.bellsafehouse.domain.base.BaseTime
import java.util.UUID

@Entity
@Table(name = "user_token")
class UserToken(
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user", nullable = false)
    val user: User,
) : BaseTime() {
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    val id: UUID = UlidCreator.getMonotonicUlid().toUuid()

    override fun toString(): String {
        return "UserToken(id=$id, user.id=${user.id})"
    }
}
