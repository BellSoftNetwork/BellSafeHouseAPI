package net.bellsoft.bellsafehouse.config

import net.bellsoft.bellsafehouse.component.aspect.JpaDefaultFilterAspect
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class AspectConfig {
    @Bean
    fun jpaDefaultFilterAspect(): JpaDefaultFilterAspect {
        return JpaDefaultFilterAspect()
    }
}
