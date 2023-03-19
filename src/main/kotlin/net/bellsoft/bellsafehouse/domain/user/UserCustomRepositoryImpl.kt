package net.bellsoft.bellsafehouse.domain.user

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Repository
class UserCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    @Qualifier("rawJpaQueryFactory") private val rawJpaQueryFactory: JPAQueryFactory,
) : UserCustomRepository {
    override fun getYearCreatedUsers(year: Int): List<User> {
        val startDate = LocalDateTime.of(LocalDate.of(year, 1, 1), LocalTime.MIN)
        val endDate = startDate.plusYears(1)

        return jpaQueryFactory
            .selectFrom(QUser.user)
            .where(QUser.user.createdAt.goe(startDate).and(QUser.user.createdAt.lt(endDate)))
            .fetch()
    }

    override fun findDeletedByUserId(userId: String): User? {
        return rawJpaQueryFactory
            .selectFrom(QUser.user)
            .where(QUser.user.userId.eq(userId).and(QUser.user.deletedAt.isNotNull))
            .fetchFirst()
    }

    override fun existsDeletedByUserId(userId: String): Boolean {
        return findDeletedByUserId(userId) != null
    }

    override fun findDeletedByEmail(email: String): User? {
        return rawJpaQueryFactory
            .selectFrom(QUser.user)
            .where(QUser.user.email.eq(email).and(QUser.user.deletedAt.isNotNull))
            .fetchFirst()
    }

    override fun existsDeletedByEmail(email: String): Boolean {
        return findDeletedByEmail(email) != null
    }

    override fun findDeletedByNickname(nickname: String): User? {
        return rawJpaQueryFactory
            .selectFrom(QUser.user)
            .where(QUser.user.nickname.eq(nickname).and(QUser.user.deletedAt.isNotNull))
            .fetchFirst()
    }

    override fun existsDeletedByNickname(nickname: String): Boolean {
        return findDeletedByNickname(nickname) != null
    }
}
