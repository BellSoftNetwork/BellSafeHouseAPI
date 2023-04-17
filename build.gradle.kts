import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    val kotlinVersion = "1.7.20"

    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
    id("org.liquibase.gradle") version "2.1.1"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("kapt") version kotlinVersion

    jacoco
    idea
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
    const val KOTLIN = "1.7.20"
    const val SPRING_BOOT = "3.0.4"
    const val QUERYDSL = "5.0.0"
    const val SPRING_SECURITY = "6.0.2"
    const val SPRING_BATCH = "5.0.1"
    const val SPRING_RABBIT = "3.0.2"
    const val LIQUIBASE = "4.20.0"
    const val MYSQL_CONNECTOR = "8.0.29"
    const val H2_DATABASE = "2.1.214"
    const val SPRINGDOC_OPENAPI = "2.0.2"
    const val KOTLIN_LOGGING = "2.1.23"
    const val DATA_FAKER = "1.7.0"
    const val KOTEST = "5.4.1"
    const val KOTEST_EXTENSION_SPRING = "1.1.2"
    const val MOCKK = "1.13.4"
    const val SPRING_MOCKK = "4.0.1"
    const val KOTLIN_FIXTURE = "1.2.0"
    const val JSON_WEB_TOKEN_FOR_JAVA = "0.11.5"
    const val ULID_CREATOR = "5.1.0"
    const val FINDBUGS_JSR305 = "3.0.2"

    /** ### bootBuildImage Task 에서 사용하는 빌더 이미지 버전
     * 빌더 버전 업데이트 시 `bindings/VERSION.md` 파일 업데이트 필요
     */
    const val PAKETO_BUILDPACKS_BUILDER = "0.3.44-base"
}

object Libraries {
    // Language
    const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}"
    const val KOTLIN_STANDARD_LIBRARY = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.KOTLIN}"

    // Server
    const val SPRING_BOOT_STARTER_WEB = "org.springframework.boot:spring-boot-starter-web:${Versions.SPRING_BOOT}"

    // DB
    const val SPRING_BOOT_STARTER_DATA_JPA =
        "org.springframework.boot:spring-boot-starter-data-jpa:${Versions.SPRING_BOOT}"
    const val QUERYDSL = "com.querydsl:querydsl-jpa:${Versions.QUERYDSL}:jakarta"
    const val QUERYDSL_APT = "com.querydsl:querydsl-apt:${Versions.QUERYDSL}:jakarta"
    const val LIQUIBASE = "org.liquibase:liquibase-core:${Versions.LIQUIBASE}"

    const val MYSQL_CONNECTOR = "mysql:mysql-connector-java:${Versions.MYSQL_CONNECTOR}"
    const val H2_DATABASE = "com.h2database:h2:${Versions.H2_DATABASE}"

    // Broker
    const val SPRING_BOOT_STARTER_AMQP = "org.springframework.boot:spring-boot-starter-amqp:${Versions.SPRING_BOOT}"
    const val SPRING_RABBIT_TEST = "org.springframework.amqp:spring-rabbit-test:${Versions.SPRING_RABBIT}"

    // Security
    const val SPRING_BOOT_STARTER_SECURITY =
        "org.springframework.boot:spring-boot-starter-security:${Versions.SPRING_BOOT}"
    const val SPRING_SECURITY_TEST = "org.springframework.security:spring-security-test:${Versions.SPRING_SECURITY}"
    const val JSON_WEB_TOKEN_FOR_JAVA_API = "io.jsonwebtoken:jjwt-api:${Versions.JSON_WEB_TOKEN_FOR_JAVA}"
    const val JSON_WEB_TOKEN_FOR_JAVA_IMPL = "io.jsonwebtoken:jjwt-impl:${Versions.JSON_WEB_TOKEN_FOR_JAVA}"
    const val JSON_WEB_TOKEN_FOR_JAVA_JACKSON = "io.jsonwebtoken:jjwt-jackson:${Versions.JSON_WEB_TOKEN_FOR_JAVA}"

    // Communication
    const val SPRING_BOOT_STARTER_MAIL = "org.springframework.boot:spring-boot-starter-mail:${Versions.SPRING_BOOT}"

    // Template Engine
    const val SPRING_BOOT_STARTER_THYMELEAF =
        "org.springframework.boot:spring-boot-starter-thymeleaf:${Versions.SPRING_BOOT}"

    // Process
    const val SPRING_BOOT_STARTER_BATCH = "org.springframework.boot:spring-boot-starter-batch:${Versions.SPRING_BOOT}"
    const val SPRING_BOOT_BATCH_TEST = "org.springframework.batch:spring-batch-test:${Versions.SPRING_BATCH}"

    // Swagger
    const val SPRINGDOC_OPENAPI_STARTER_WEB_MVC =
        "org.springdoc:springdoc-openapi-starter-webmvc-ui:${Versions.SPRINGDOC_OPENAPI}"

    // Support
    const val SPRING_BOOT_STARTER_VALIDATION =
        "org.springframework.boot:spring-boot-starter-validation:${Versions.SPRING_BOOT}"
    const val SPRING_BOOT_STARTER_HATEOAS =
        "org.springframework.boot:spring-boot-starter-hateoas:${Versions.SPRING_BOOT}"
    const val JACKSON_KOTLIN = "com.fasterxml.jackson.module:jackson-module-kotlin"
    const val KOTLIN_LOGGING = "io.github.microutils:kotlin-logging:${Versions.KOTLIN_LOGGING}"
    const val DATA_FAKER = "net.datafaker:datafaker:${Versions.DATA_FAKER}"
    const val ULID_CREATOR = "com.github.f4b6a3:ulid-creator:${Versions.ULID_CREATOR}"
    const val FINDBUGS_JSR305 = "com.google.code.findbugs:jsr305:${Versions.FINDBUGS_JSR305}"

    // Ops
    const val SPRING_BOOT_STARTER_ACTUATOR =
        "org.springframework.boot:spring-boot-starter-actuator:${Versions.SPRING_BOOT}"

    // Dev
    const val SPRING_BOOT_DEVTOOLS = "org.springframework.boot:spring-boot-devtools:${Versions.SPRING_BOOT}"

    // AOP
    const val SPRING_BOOT_STARTER_AOP = "org.springframework.boot:spring-boot-starter-aop:${Versions.SPRING_BOOT}"

    // Test
    const val SPRING_BOOT_STARTER_TEST = "org.springframework.boot:spring-boot-starter-test:${Versions.SPRING_BOOT}"
    const val KOTEST_RUNNER_JUNIT5 = "io.kotest:kotest-runner-junit5:${Versions.KOTEST}"
    const val KOTEST_ASSERTIONS_CORE = "io.kotest:kotest-assertions-core:${Versions.KOTEST}"
    const val KOTEST_PROPERTY = "io.kotest:kotest-property:${Versions.KOTEST}"

    const val KOTEST_EXTENSIONS_SPRING =
        "io.kotest.extensions:kotest-extensions-spring:${Versions.KOTEST_EXTENSION_SPRING}"
    const val MOCKK = "io.mockk:mockk:${Versions.MOCKK}"
    const val NINJA_SQUAD_SPRING_MOCKK = "com.ninja-squad:springmockk:${Versions.SPRING_MOCKK}"
    const val KOTLIN_FIXTURE_KOTEST = "com.appmattus.fixture:fixture-kotest:${Versions.KOTLIN_FIXTURE}"
    const val KOTLIN_FIXTURE_GENEREX = "com.appmattus.fixture:fixture-generex:${Versions.KOTLIN_FIXTURE}"
}

dependencies {
    // Language
    implementation(Libraries.KOTLIN_REFLECT)
    implementation(Libraries.KOTLIN_STANDARD_LIBRARY)

    // Server
    implementation(Libraries.SPRING_BOOT_STARTER_WEB)

    // DB
    implementation(Libraries.SPRING_BOOT_STARTER_DATA_JPA)
    implementation(Libraries.QUERYDSL)
    kapt(Libraries.QUERYDSL_APT)
    implementation(Libraries.LIQUIBASE)
    runtimeOnly(Libraries.MYSQL_CONNECTOR)
    testImplementation(Libraries.H2_DATABASE)

    // Broker
    implementation(Libraries.SPRING_BOOT_STARTER_AMQP)
    testImplementation(Libraries.SPRING_RABBIT_TEST)

    // Security
    implementation(Libraries.SPRING_BOOT_STARTER_SECURITY)
    testImplementation(Libraries.SPRING_SECURITY_TEST)
    implementation(Libraries.JSON_WEB_TOKEN_FOR_JAVA_API)
    runtimeOnly(Libraries.JSON_WEB_TOKEN_FOR_JAVA_IMPL)
    runtimeOnly(Libraries.JSON_WEB_TOKEN_FOR_JAVA_JACKSON)

    // Communication
    implementation(Libraries.SPRING_BOOT_STARTER_MAIL)

    // Template Engine
    implementation(Libraries.SPRING_BOOT_STARTER_THYMELEAF)

    // Process
    implementation(Libraries.SPRING_BOOT_STARTER_BATCH)
    testImplementation(Libraries.SPRING_BOOT_BATCH_TEST)

    // Swagger
    implementation(Libraries.SPRINGDOC_OPENAPI_STARTER_WEB_MVC)

    // Support
    implementation(Libraries.SPRING_BOOT_STARTER_VALIDATION)
    implementation(Libraries.SPRING_BOOT_STARTER_HATEOAS)
    implementation(Libraries.JACKSON_KOTLIN)
    implementation(Libraries.KOTLIN_LOGGING)
    implementation(Libraries.DATA_FAKER)
    implementation(Libraries.ULID_CREATOR)
    implementation(Libraries.FINDBUGS_JSR305)

    // Ops
    implementation(Libraries.SPRING_BOOT_STARTER_ACTUATOR)

    // Dev
    developmentOnly(Libraries.SPRING_BOOT_DEVTOOLS)

    // AOP
    implementation(Libraries.SPRING_BOOT_STARTER_AOP)

    // Test
    testImplementation(Libraries.SPRING_BOOT_STARTER_TEST) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
    testImplementation(Libraries.KOTEST_RUNNER_JUNIT5)
    testImplementation(Libraries.KOTEST_ASSERTIONS_CORE)
    testImplementation(Libraries.KOTEST_PROPERTY)
    testImplementation(Libraries.KOTEST_EXTENSIONS_SPRING)
    testImplementation(Libraries.MOCKK)
    testImplementation(Libraries.NINJA_SQUAD_SPRING_MOCKK)
    testImplementation(Libraries.KOTLIN_FIXTURE_KOTEST)
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
    setupEnvironment(this)
    setupBuildProperty(this)
    setupImageProperty(this)
    setupDocker(this)
}

fun setupEnvironment(bootBuildImage: BootBuildImage) {
    bootBuildImage.run {
        environment.set(environment.get() + mapOf("SPRING_PROFILES_ACTIVE" to "production"))
    }
}

fun setupBuildProperty(bootBuildImage: BootBuildImage) {
    bootBuildImage.run {
        val bindingsDir: String by project
        val gradleDir: String by project

        val bindingVolumes = mutableListOf<String>()

        if (project.hasProperty("bindingsDir")) bindingVolumes.add("$bindingsDir:/platform/bindings:rw")
        if (project.hasProperty("gradleDir")) bindingVolumes.add("$gradleDir:/home/cnb/.gradle:rw")

        bindings.set(bindingVolumes)
        builder.set("paketobuildpacks/builder:${Versions.PAKETO_BUILDPACKS_BUILDER}")
    }
}

fun setupImageProperty(bootBuildImage: BootBuildImage) {
    bootBuildImage.run {
        val imagePath: String by project
        val imageBaseName: String by project
        val imageTag: String by project

        if (project.hasProperty("imagePath")) imageName.set(imagePath)
        if (project.hasProperty("imageBaseName")) imageName.set("${imageName.get()}/$imageBaseName")
        if (project.hasProperty("imageTag")) tags.set(mutableListOf("${imageName.get()}:$imageTag"))
    }
}

fun setupDocker(bootBuildImage: BootBuildImage) {
    bootBuildImage.run {
        val dockerHost: String by project
        val isDockerTlsVerify: String by project
        val dockerCertPath: String by project

        val projectRegistryUrl: String by project
        val registryUser: String by project
        val registryPassword: String by project
        val registryEmail: String by project

        docker {
            if (project.hasProperty("dockerHost")) host.set(dockerHost)
            if (project.hasProperty("isDockerTlsVerify")) tlsVerify.set(isDockerTlsVerify.toBoolean())
            if (project.hasProperty("dockerCertPath")) certPath.set(dockerCertPath)

            publishRegistry {
                if (project.hasProperty("projectRegistryUrl")) url.set(projectRegistryUrl)
                if (project.hasProperty("registryUser")) username.set(registryUser)
                if (project.hasProperty("registryPassword")) password.set(registryPassword)
                if (project.hasProperty("registryEmail")) email.set(registryEmail)
            }
        }
    }
}

ktlint {
    version.set("0.48.2")
    verbose.set(true)
    relative.set(true)
    outputColorName.set("RED")
    enableExperimentalRules.set(true)
}

tasks.named<Test>("test") {
    useJUnitPlatform()

    jvmArgs(
        "--add-opens",
        "java.base/java.time=ALL-UNNAMED",
        "--add-opens",
        "java.base/java.lang.reflect=ALL-UNNAMED",
    )

    finalizedBy("jacocoTestReport")
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        xml.outputLocation.set(file("$buildDir/jacoco/jacoco.xml"))
    }

    finalizedBy("jacocoTestCoverageVerification") // NOTE: 활성화시 violationRules 통과 실패할경우 테스트도 실패처리 됨
}

private object JacocoViolationRuleSet {
    object Default {
        private val QUERY_DSL_DOMAINS = ('A'..'Z').map { "*.Q$it*" }

        val EXCLUDE_FILES = listOf(
            "*ApplicationKt",
            "*.config.*Config",
            "*.exception.*Exception",
            "*Dto",
            "*DTO",
        ) + QUERY_DSL_DOMAINS
    }

    object Business {
        val INCLUDE_FILES = listOf(
            "*.domain.*",
            "*.service.*",
        )
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            // NOTE: element 가 없으면 프로젝트의 전체 파일을 합친 값 기준

            limit {
                // NOTE: counter 를 지정하지 않으면 default 는 INSTRUCTION
                // NOTE: value 를 지정하지 않으면 default 는 COVEREDRATIO
                minimum = "0.30".toBigDecimal()
            }
        }

        rule {
            enabled = true
            element = "CLASS"

            // NOTE: 빈 줄을 제외한 코드의 라인수를 최대 200라인으로 제한
            limit {
                counter = "LINE"
                value = "TOTALCOUNT"
                maximum = "200".toBigDecimal()
            }
        }

        rule {
            enabled = true
            element = "CLASS"
            includes = JacocoViolationRuleSet.Business.INCLUDE_FILES
            excludes = JacocoViolationRuleSet.Default.EXCLUDE_FILES

            // NOTE: 브랜치 커버리지 최소 90% 만족
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.90".toBigDecimal()
            }

            // NOTE: 라인 커버리지 최소 80% 만족
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.80".toBigDecimal()
            }
        }
    }
}
