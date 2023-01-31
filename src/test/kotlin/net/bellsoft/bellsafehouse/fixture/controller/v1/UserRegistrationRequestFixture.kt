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
                property(UserRegistrationRequest::nickname) { "normal-${FAKER.random().hex(10)}" }
            }
        },
        ADMIN {
            override fun config() = fixtureConfig {
                property(UserRegistrationRequest::nickname) { "admin-${FAKER.random().hex(10)}" }
            }
        },
    }

    companion object {
        private val FAKER = Faker(Locale.KOREA)

        val BASE_CONFIGURATION = fixtureConfig {
            property(UserRegistrationRequest::userId) { "userId-${FAKER.random().hex(10)}" }
            property(UserRegistrationRequest::nickname) { "nickname-${FAKER.random().hex(10)}" }
            property(UserRegistrationRequest::email) { FAKER.internet().emailAddress() }
        }
    }
}
