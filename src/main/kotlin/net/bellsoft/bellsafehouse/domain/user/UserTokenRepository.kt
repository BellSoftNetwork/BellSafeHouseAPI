package net.bellsoft.bellsafehouse.domain.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserTokenRepository : JpaRepository<UserToken, Long>
