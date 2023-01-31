package net.bellsoft.bellsafehouse.domain.user

interface UserCustomRepository {
    fun getYearCreatedUsers(year: Int): List<User>
}
