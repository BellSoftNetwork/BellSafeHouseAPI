import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("org.liquibase.gradle") version "2.1.1"

    jacoco

    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
}

jacoco {
    toolVersion = "0.8.8"
}

group = "net.bellsoft"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

object Versions {
    const val kotlinReflect = "1.7.10"
    const val springBoot = "2.7.2"
    const val springSecurity = "5.7.2"
    const val springBatch = "4.3.6"
    const val springRabbit = "2.4.6"
    const val liquibase = "4.9.1"
    const val mysqlConnector = "8.0.29"
    const val h2Database = "2.1.214"
    const val springdocOpenapi = "1.6.9"
    const val kotlinLogging = "2.1.23"
    const val kotest = "5.4.1"
    const val kotestExtensionSpring = "1.1.2"
    const val mockk = "1.12.4"
    const val springMockk = "3.1.1"

    /** ### bootBuildImage Task 에서 사용하는 빌더 이미지 버전
     * 빌더 버전 업데이트 시 `bindings/VERSION.md` 파일 업데이트 필요
     */
    const val paketoBuildpacksBuilder = "0.3.44-base"
}

object Libraries {
    // Language
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlinReflect}"
    const val kotlinStandardLibrary = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinReflect}"

    // Server
    const val springBootStarterWeb = "org.springframework.boot:spring-boot-starter-web:${Versions.springBoot}"

    // DB
    const val springBootStarterDataJpa = "org.springframework.boot:spring-boot-starter-data-jpa:${Versions.springBoot}"
    const val liquibase = "org.liquibase:liquibase-core:${Versions.liquibase}"

    const val mysqlConnector = "mysql:mysql-connector-java:${Versions.mysqlConnector}"
    const val h2Database = "com.h2database:h2:${Versions.h2Database}"

    // Broker
    const val springBootStarterAMQP = "org.springframework.boot:spring-boot-starter-amqp:${Versions.springBoot}"
    const val springRabbitTest = "org.springframework.amqp:spring-rabbit-test:${Versions.springRabbit}"

    // Security
    const val springBootStarterSecurity = "org.springframework.boot:spring-boot-starter-security:${Versions.springBoot}"
    const val springSecurityTest = "org.springframework.security:spring-security-test:${Versions.springSecurity}"

    // Communication
    const val springBootStarterMail = "org.springframework.boot:spring-boot-starter-mail:${Versions.springBoot}"

    // Process
    const val springBootStarterBatch = "org.springframework.boot:spring-boot-starter-batch:${Versions.springBoot}"
    const val springBootBatchTest = "org.springframework.batch:spring-batch-test:${Versions.springBatch}"

    // Swagger
    const val springdocOpenapiDataRest = "org.springdoc:springdoc-openapi-data-rest:${Versions.springdocOpenapi}"
    const val springdocOpenapiUI = "org.springdoc:springdoc-openapi-ui:${Versions.springdocOpenapi}"
    const val springdocOpenapiKotlin = "org.springdoc:springdoc-openapi-kotlin:${Versions.springdocOpenapi}"

    // Support
    const val springBootStarterValidation =
        "org.springframework.boot:spring-boot-starter-validation:${Versions.springBoot}"
    const val springBootStarterHateoas = "org.springframework.boot:spring-boot-starter-hateoas:${Versions.springBoot}"
    const val jacksonKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin"
    const val kotlinLogging = "io.github.microutils:kotlin-logging:${Versions.kotlinLogging}"

    // Ops
    const val springBootStarterActuator = "org.springframework.boot:spring-boot-starter-actuator:${Versions.springBoot}"

    // Dev
    const val springBootDevtools = "org.springframework.boot:spring-boot-devtools:${Versions.springBoot}"

    // Test
    const val springBootStarterTest = "org.springframework.boot:spring-boot-starter-test:${Versions.springBoot}"
    const val kotestRunnerJunit5 = "io.kotest:kotest-runner-junit5:${Versions.kotest}"
    const val kotestAssertionsCore = "io.kotest:kotest-assertions-core:${Versions.kotest}"
    const val kotestProperty = "io.kotest:kotest-property:${Versions.kotest}"

    // const val kotestExtensionsSpring = "io.kotest:kotest-extensions-spring:4.4.3"
    const val kotestExtensionsSpring = "io.kotest.extensions:kotest-extensions-spring:${Versions.kotestExtensionSpring}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val ninjaSquadSpringMockk = "com.ninja-squad:springmockk:${Versions.springMockk}"
}

dependencies {
    // Language
    implementation(Libraries.kotlinReflect)
    implementation(Libraries.kotlinStandardLibrary)

    // Server
    implementation(Libraries.springBootStarterWeb)

    // DB
    implementation(Libraries.springBootStarterDataJpa)
    implementation(Libraries.liquibase)
    runtimeOnly(Libraries.mysqlConnector)
    testImplementation(Libraries.h2Database)

    // Broker
    implementation(Libraries.springBootStarterAMQP)
    testImplementation(Libraries.springRabbitTest)

    // Security
    // implementation(Libraries.springBootStarterSecurity)
    // testImplementation(Libraries.springSecurityTest)

    // Communication
    implementation(Libraries.springBootStarterMail)

    // Process
    implementation(Libraries.springBootStarterBatch)
    testImplementation(Libraries.springBootBatchTest)

    // Swagger
    implementation(Libraries.springdocOpenapiDataRest)
    implementation(Libraries.springdocOpenapiUI)
    implementation(Libraries.springdocOpenapiKotlin)

    // Support
    implementation(Libraries.springBootStarterValidation)
    implementation(Libraries.springBootStarterHateoas)
    implementation(Libraries.jacksonKotlin)
    implementation(Libraries.kotlinLogging)

    // Ops
    implementation(Libraries.springBootStarterActuator)

    // Dev
    developmentOnly(Libraries.springBootDevtools)

    // Test
    testImplementation(Libraries.springBootStarterTest) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
    testImplementation(Libraries.kotestRunnerJunit5)
    testImplementation(Libraries.kotestAssertionsCore)
    testImplementation(Libraries.kotestProperty)
    testImplementation(Libraries.kotestExtensionsSpring)
    testImplementation(Libraries.mockk)
    testImplementation(Libraries.ninjaSquadSpringMockk)
}

tasks.named<KotlinCompile>("compileKotlin") {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.named<BootRun>("bootRun") {
    setupEnvironment()
}

fun BootRun.setupEnvironment() {
    jvmArgs = listOf("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005")
    environment("SPRING_PROFILES_ACTIVE", "local")
}

// https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/#build-image
tasks.named<BootBuildImage>("bootBuildImage") {
    setupEnvironment()
    setupBuildProperty()
    setupImageProperty()
    setupDocker()
}

fun BootBuildImage.setupEnvironment() {
    environment("SPRING_PROFILES_ACTIVE", "production")
}

fun BootBuildImage.setupBuildProperty() {
    val bindingsDir: String by project
    val gradleDir: String by project

    val bindingVolumes = mutableListOf<String>()

    if (project.hasProperty("bindingsDir")) bindingVolumes.add("$bindingsDir:/platform/bindings:rw")
    if (project.hasProperty("gradleDir")) bindingVolumes.add("$gradleDir:/home/cnb/.gradle:rw")

    bindings = bindingVolumes
    builder = "paketobuildpacks/builder:${Versions.paketoBuildpacksBuilder}"
}

fun BootBuildImage.setupImageProperty() {
    val imagePath: String by project
    val imageBaseName: String by project
    val imageTag: String by project

    if (project.hasProperty("imagePath")) imageName = imagePath
    if (project.hasProperty("imageBaseName")) imageName += "/$imageBaseName"
    if (project.hasProperty("imageTag")) tags = mutableListOf("$imageName:$imageTag")
}

fun BootBuildImage.setupDocker() {
    val dockerHost: String by project
    val isDockerTlsVerify: String by project
    val dockerCertPath: String by project

    val proxyRegistryUrl: String by project
    val projectRegistryUrl: String by project
    val registryUser: String by project
    val registryPassword: String by project
    val registryEmail: String by project

    docker {
        if (project.hasProperty("dockerHost")) host = dockerHost
        if (project.hasProperty("isDockerTlsVerify")) isTlsVerify = isDockerTlsVerify.toBoolean()
        if (project.hasProperty("dockerCertPath")) certPath = dockerCertPath

        // builderRegistry {
        //     if (project.hasProperty("proxyRegistryUrl")) url = proxyRegistryUrl
        //     if (project.hasProperty("registryUser")) username = registryUser
        //     if (project.hasProperty("registryPassword")) password = registryPassword
        //     if (project.hasProperty("registryEmail")) email = registryEmail
        // }

        publishRegistry {
            if (project.hasProperty("projectRegistryUrl")) url = projectRegistryUrl
            if (project.hasProperty("registryUser")) username = registryUser
            if (project.hasProperty("registryPassword")) password = registryPassword
            if (project.hasProperty("registryEmail")) email = registryEmail
        }
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()

    finalizedBy("jacocoTestReport")
}

val jacocoDefaultExcludeFiles = listOf(
    "net.bellsoft.bellsafehouse.BellSafeHouseApplicationKt"
)

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        xml.outputLocation.set(file("$buildDir/jacoco/jacoco.xml"))
    }

    finalizedBy("jacocoTestCoverageVerification") // 활성화시 violationRules 통과 실패할경우 테스트도 실패처리 됨
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            // element 가 없으면 프로젝트의 전체 파일을 합친 값 기준

            limit {
                // counter 를 지정하지 않으면 default 는 INSTRUCTION
                // value 를 지정하지 않으면 default 는 COVEREDRATIO
                minimum = "0.30".toBigDecimal()
            }
        }

        rule {
            enabled = true
            element = "CLASS" // class 단위로 rule check
            excludes = jacocoDefaultExcludeFiles

            // 브랜치 커버리지 최소 90% 만족
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.90".toBigDecimal()
            }

            // 라인 커버리지 최소 80% 만족
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.80".toBigDecimal()
            }

            // 빈 줄을 제외한 코드의 라인수를 최대 200라인으로 제한
            limit {
                counter = "LINE"
                value = "TOTALCOUNT"
                maximum = "200".toBigDecimal()
            }
        }
    }
}
