package net.bellsoft.bellsafehouse.fixture.domain.user

import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import net.bellsoft.bellsafehouse.domain.user.UserToken
import net.bellsoft.bellsafehouse.fixture.FixtureFeature
import net.bellsoft.bellsafehouse.fixture.baseFixture

class UserTokenFixture {
    enum class Feature : FixtureFeature

    companion object {
        private val UNIQUE_SEQUENCE_ITERATOR = (0..Int.MAX_VALUE).iterator()

        internal val BASE_CONFIGURATION: ConfigurationBuilder.() -> Unit = {
            property(UserToken::user) { baseFixture() }
        }
    }
}
