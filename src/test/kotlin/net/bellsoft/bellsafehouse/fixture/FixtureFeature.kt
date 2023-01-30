package net.bellsoft.bellsafehouse.fixture

import com.appmattus.kotlinfixture.config.ConfigurationBuilder

interface FixtureFeature {
    fun config(): ConfigurationBuilder.() -> Unit
}
