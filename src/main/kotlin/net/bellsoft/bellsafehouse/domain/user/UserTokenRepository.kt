package net.bellsoft.bellsafehouse.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserTokenRepository : JpaRepository<UserToken, UUID>
