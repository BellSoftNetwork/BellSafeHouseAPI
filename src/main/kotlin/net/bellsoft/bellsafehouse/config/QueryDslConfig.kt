package net.bellsoft.bellsafehouse.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.PersistenceContext
import jakarta.persistence.PersistenceUnit
import mu.KLogging
import net.bellsoft.bellsafehouse.component.aspect.JpaDefaultFilterAspect
import org.hibernate.Session
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class QueryDslConfig {
    @PersistenceUnit
    lateinit var entityManagerFactory: EntityManagerFactory

    @PersistenceContext
    lateinit var entityManager: EntityManager

    @Primary
    @Bean
    fun jpaQueryFactory(): JPAQueryFactory {
        return JPAQueryFactory(entityManager)
    }

    @Bean
    fun rawJpaQueryFactory(): JPAQueryFactory {
        val rawEntityManager = entityManagerFactory.createEntityManager()
        val session = rawEntityManager.unwrap(Session::class.java)
        JpaDefaultFilterAspect.DEFAULT_FILTER_LIST.forEach {
            session.disableFilter(it)
            logger.debug("Filter $it disabled for raw JPAQueryFactory")
        }
        return JPAQueryFactory(rawEntityManager)
    }

    companion object : KLogging()
}
