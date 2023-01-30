package net.bellsoft.bellsafehouse.fixture.config

import net.bellsoft.bellsafehouse.fixture.controller.v1.UserLoginRequestFixture
import net.bellsoft.bellsafehouse.fixture.controller.v1.UserRegistrationRequestFixture
import net.bellsoft.bellsafehouse.fixture.domain.user.UserFixture
import net.bellsoft.bellsafehouse.fixture.domain.user.UserTokenFixture

// NOTE: 신규 도메인 추가 시 해당 도메인 Configuration 생성 후 아래에 등록 필요
@Suppress("ktlint:experimental:property-naming")
private val domainConfigurations = listOf(
    UserFixture.BASE_CONFIGURATION,
    UserTokenFixture.BASE_CONFIGURATION,
)

// NOTE: 신규 DTO 추가 시 해당 DTO Configuration 생성 후 아래에 등록 필요
@Suppress("ktlint:experimental:property-naming")
private val dtoFixtureConfigurations = listOf(
    UserRegistrationRequestFixture.BASE_CONFIGURATION,
    UserLoginRequestFixture.BASE_CONFIGURATION,
)

// NOTE: 신규 설정 리스트 생성 시 해당 설정 리스트를 아래에 등록 필요
@Suppress("ktlint:experimental:property-naming")
val integratedFixtureConfigurations = domainConfigurations + dtoFixtureConfigurations
