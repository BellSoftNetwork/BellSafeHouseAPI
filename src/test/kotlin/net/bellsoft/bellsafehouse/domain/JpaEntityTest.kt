package net.bellsoft.bellsafehouse.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@DataJpaTest
@EnableJpaAuditing
annotation class JpaEntityTest
