package net.bellsoft.bellsafehouse.domain.user

import com.github.f4b6a3.ulid.UlidCreator
import net.bellsoft.bellsafehouse.domain.base.BaseTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

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
