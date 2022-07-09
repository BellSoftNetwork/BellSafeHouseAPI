import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"

    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
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
