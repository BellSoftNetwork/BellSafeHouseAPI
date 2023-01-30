package net.bellsoft.bellsafehouse.fixture.controller.v1

import net.bellsoft.bellsafehouse.controller.v1.auth.dto.UserLoginRequest
import net.bellsoft.bellsafehouse.fixture.FixtureFeature
import net.bellsoft.bellsafehouse.fixture.util.fixtureConfig
import net.datafaker.Faker
import java.util.Locale

class UserLoginRequestFixture {
    enum class Feature : FixtureFeature

    companion object {
        private val UNIQUE_SEQUENCE_ITERATOR = (0..Int.MAX_VALUE).iterator()
        private val FAKER = Faker(Locale.KOREA)

        val BASE_CONFIGURATION = fixtureConfig {
            property(UserLoginRequest::userId) { "userId${UNIQUE_SEQUENCE_ITERATOR.next()}" }
            property(UserLoginRequest::password) { "password${UNIQUE_SEQUENCE_ITERATOR.next()}" }
        }
    }
}
