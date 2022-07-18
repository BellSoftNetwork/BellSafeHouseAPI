import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("org.springframework.boot") version "2.7.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"

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

val kotlinReflectVersion = "1.7.10"
val springBootVersion = "2.7.1"
val springSecurityVersion = "5.7.2"
val springBatchVersion = "4.3.6"
val springRabbitVersion = "2.4.6"
val liquibaseVersion = "4.9.1"
val mysqlConnectorVersion = "8.0.29"
val h2DatabaseVersion = "2.1.214"
val springdocOpenapiVersion = "1.6.9"
val kotestVersion = "5.3.1"
val kotestSpringExtensionVersion = "4.4.3"
val mockkVersion = "1.12.4"
val springMockkVersion = "3.1.1"

dependencies {
    // Language
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinReflectVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinReflectVersion")

    // Server
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")

    // DB
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
//    implementation("org.liquibase:liquibase-core:$liquibaseVersion")
    runtimeOnly("mysql:mysql-connector-java:$mysqlConnectorVersion")
    runtimeOnly("com.h2database:h2:$h2DatabaseVersion")

    // Broker
    implementation("org.springframework.boot:spring-boot-starter-amqp:$springBootVersion")
    testImplementation("org.springframework.amqp:spring-rabbit-test:$springRabbitVersion")

    // Security
//    implementation("org.springframework.boot:spring-boot-starter-security:$springBootVersion")
//    testImplementation("org.springframework.security:spring-security-test:$springSecurityVersion")

    // Communication
    implementation("org.springframework.boot:spring-boot-starter-mail:$springBootVersion")

    // Process
    implementation("org.springframework.boot:spring-boot-starter-batch:$springBootVersion")
    testImplementation("org.springframework.batch:spring-batch-test:$springBatchVersion")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-data-rest:$springdocOpenapiVersion")
    implementation("org.springdoc:springdoc-openapi-ui:$springdocOpenapiVersion")
    implementation("org.springdoc:springdoc-openapi-kotlin:$springdocOpenapiVersion")

    // Support
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-hateoas:$springBootVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Ops
    implementation("org.springframework.boot:spring-boot-starter-actuator:$springBootVersion")

    // Dev
    developmentOnly("org.springframework.boot:spring-boot-devtools:$springBootVersion")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-extensions-spring:$kotestSpringExtensionVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("com.ninja-squad:springmockk:$springMockkVersion")
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
    environment("SPRING_PROFILES_ACTIVE", "production")
}

tasks.named<BootBuildImage>("bootBuildImage") {
    setupEnvironment()
    setupImageProperty()
    setupDocker()
}

fun BootBuildImage.setupEnvironment() {
    environment("SPRING_PROFILES_ACTIVE", "production")
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

    val registryUrl: String by project
    val registryUser: String by project
    val registryPassword: String by project
    val registryEmail: String by project

    docker {
        if (project.hasProperty("dockerHost")) host = dockerHost
        if (project.hasProperty("isDockerTlsVerify")) isTlsVerify = isDockerTlsVerify.toBoolean()
        if (project.hasProperty("dockerCertPath")) certPath = dockerCertPath

        publishRegistry {
            if (project.hasProperty("registryUrl")) url = registryUrl
            if (project.hasProperty("registryUser")) username = registryUser
            if (project.hasProperty("registryPassword")) password = registryPassword
            if (project.hasProperty("registryEmail")) email = registryEmail
        }
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()

    environment("SPRING_PROFILES_ACTIVE", "test")

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
