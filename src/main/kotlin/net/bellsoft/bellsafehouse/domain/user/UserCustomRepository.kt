package net.bellsoft.bellsafehouse.domain.user

interface UserCustomRepository {
    fun getYearCreatedUsers(year: Int): List<User>

    fun findDeletedByUserId(userId: String): User?

    fun existsDeletedByUserId(userId: String): Boolean

    fun findDeletedByEmail(email: String): User?

    fun existsDeletedByEmail(email: String): Boolean

    fun findDeletedByNickname(nickname: String): User?

    fun existsDeletedByNickname(nickname: String): Boolean
}
