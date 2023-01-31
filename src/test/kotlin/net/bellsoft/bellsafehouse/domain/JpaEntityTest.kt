package net.bellsoft.bellsafehouse.domain

import net.bellsoft.bellsafehouse.config.QueryDslConfig
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@DataJpaTest
@EnableJpaAuditing
@Import(QueryDslConfig::class)
annotation class JpaEntityTest
