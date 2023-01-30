package net.bellsoft.bellsafehouse.fixture.controller.v1

import net.bellsoft.bellsafehouse.controller.v1.auth.dto.UserRegistrationRequest
import net.bellsoft.bellsafehouse.fixture.FixtureFeature
import net.bellsoft.bellsafehouse.fixture.util.fixtureConfig
import net.datafaker.Faker
import java.util.Locale

class UserRegistrationRequestFixture {
    enum class Feature : FixtureFeature {
        NORMAL {
            override fun config() = fixtureConfig {
                property(UserRegistrationRequest::nickname) { "normalUser${UNIQUE_SEQUENCE_ITERATOR.next()}" }
            }
        },
        ADMIN {
            override fun config() = fixtureConfig {
                property(UserRegistrationRequest::nickname) { "adminUser${UNIQUE_SEQUENCE_ITERATOR.next()}" }
            }
        },
    }

    companion object {
        private val UNIQUE_SEQUENCE_ITERATOR = (0..Int.MAX_VALUE).iterator()
        private val FAKER = Faker(Locale.KOREA)

        val BASE_CONFIGURATION = fixtureConfig {
            property(UserRegistrationRequest::userId) { "userId${UNIQUE_SEQUENCE_ITERATOR.next()}" }
            property(UserRegistrationRequest::nickname) { "nickname${UNIQUE_SEQUENCE_ITERATOR.next()}" }
            property(UserRegistrationRequest::email) { FAKER.internet().emailAddress() }
        }
    }
}
