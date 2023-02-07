package net.bellsoft.bellsafehouse.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>, UserCustomRepository {
    fun findByEmail(email: String): User?

    fun findByUserId(userId: String): User?

    fun existsByEmail(email: String): Boolean

    fun existsByUserId(userId: String): Boolean

    fun existsByNickname(userId: String): Boolean
}
