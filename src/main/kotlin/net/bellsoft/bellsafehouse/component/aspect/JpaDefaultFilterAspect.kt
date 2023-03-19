package net.bellsoft.bellsafehouse.component.aspect

import jakarta.persistence.EntityManager
import mu.KLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.hibernate.Session
import org.springframework.stereotype.Component

@Aspect
@Component
class JpaDefaultFilterAspect {
    init {
        logger.info("Initialized JPA Default Filter Aspect! Please be careful of use filter!")
    }

    @AfterReturning(
        pointcut = "bean(entityManagerFactory) && execution(* createEntityManager(..))",
        returning = "retVal",
    )
    fun enableDefaultFilters(joinPoint: JoinPoint?, retVal: Any?) {
        if (retVal != null && retVal is EntityManager) {
            val session: Session = retVal.unwrap(Session::class.java)
            DEFAULT_FILTER_LIST.forEach {
                logger.debug("Enabling session filter - $it")
                session.enableFilter(it)
            }
        }
    }

    companion object : KLogging() {
        val DEFAULT_FILTER_LIST = listOf(
            "deletedEntityFilter",
        )
    }
}
