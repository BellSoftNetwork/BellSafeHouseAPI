import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.0-M3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
}

group = "net.bellsoft"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

val kotlinReflectVersion = "1.7.10"
val springBootVersion = "3.0.0-M3"
val springSecurityVersion = "6.0.0-M5"
val springBatchVersion = "5.0.0-M3"
val liquibaseVersion = "4.12.0"
val mysqlConnectorVersion = "8.0.29"
val h2DatabaseVersion = "2.1.212"

dependencies {
    // Language
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinReflectVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinReflectVersion")

    // Server
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")

    // DB
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
    implementation("org.liquibase:liquibase-core:$liquibaseVersion")
    runtimeOnly("mysql:mysql-connector-java:$mysqlConnectorVersion")
    runtimeOnly("com.h2database:h2:$h2DatabaseVersion")

    // Broker
    implementation("org.springframework.boot:spring-boot-starter-amqp:$springBootVersion")
    testImplementation("org.springframework.amqp:spring-rabbit-test:$springBootVersion")

    // Security
    implementation("org.springframework.boot:spring-boot-starter-security:$springBootVersion")
    testImplementation("org.springframework.security:spring-security-test:$springSecurityVersion")

    // Communication
    implementation("org.springframework.boot:spring-boot-starter-mail:$springBootVersion")

    // Process
    implementation("org.springframework.boot:spring-boot-starter-batch:$springBootVersion")
    testImplementation("org.springframework.batch:spring-batch-test:$springBatchVersion")

    // Support
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-hateoas:$springBootVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Dev
    developmentOnly("org.springframework.boot:spring-boot-devtools:$springBootVersion")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
