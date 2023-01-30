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
                property(User::nickname) { "normalUser${UNIQUE_SEQUENCE_ITERATOR.next()}" }
            }
        },
        ADMIN {
            override fun config() = fixtureConfig {
                property(User::nickname) { "adminUser${UNIQUE_SEQUENCE_ITERATOR.next()}" }
            }
        },
    }

    companion object {
        private val UNIQUE_SEQUENCE_ITERATOR = (0..Int.MAX_VALUE).iterator()
        private val FAKER = Faker(Locale.KOREA)

        val BASE_CONFIGURATION = fixtureConfig {
            property(User::userId) { "userId${UNIQUE_SEQUENCE_ITERATOR.next()}" }
            property(User::nickname) { "nickname${UNIQUE_SEQUENCE_ITERATOR.next()}" }
            property(User::email) { FAKER.internet().emailAddress() }
        }
    }
}
