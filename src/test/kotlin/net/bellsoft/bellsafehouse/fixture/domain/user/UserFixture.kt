package net.bellsoft.bellsafehouse.fixture.domain.user

import net.bellsoft.bellsafehouse.domain.user.User
import net.bellsoft.bellsafehouse.fixture.FixtureFeature
import net.bellsoft.bellsafehouse.fixture.util.fixtureConfig
import net.datafaker.Faker
import java.util.Locale

class UserFixture {
    enum class Feature : FixtureFeature {
        NORMAL {
            override fun config() = fixtureConfig {
                property(User::nickname) { "normal-${FAKER.random().hex(10)}" }
            }
        },
        ADMIN {
            override fun config() = fixtureConfig {
                property(User::nickname) { "admin-${FAKER.random().hex(10)}" }
            }
        },
    }

    companion object {
        private val FAKER = Faker(Locale.KOREA)

        val BASE_CONFIGURATION = fixtureConfig {
            property(User::userId) { "userId-${FAKER.random().hex(10)}" }
            property(User::nickname) { "nickname-${FAKER.random().hex(10)}" }
            property(User::email) { FAKER.internet().emailAddress() }
        }
    }
}
