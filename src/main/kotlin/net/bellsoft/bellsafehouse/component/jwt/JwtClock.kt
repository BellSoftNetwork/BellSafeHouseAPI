package net.bellsoft.bellsafehouse.component.jwt

import io.jsonwebtoken.Clock
import java.time.Instant
import java.util.Date

class JwtClock : Clock {
    override fun now(): Date = Date.from(Instant.now())
}
