plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)
    alias(libs.plugins.ktlint)
}

group = "com.giwankim"
version = "0.0.1-SNAPSHOT"
description = "spring-ai-in-action"

java {
    toolchain {
        languageVersion =
            JavaLanguageVersion.of(
                libs.versions.java
                    .get()
                    .toInt(),
            )
    }
}

dependencyManagement {
    imports {
        mavenBom(
            libs.springAi.bom
                .get()
                .toString(),
        )
    }
}

dependencies {
    // Spring Boot starters
    implementation(libs.bundles.springBoot.starter)
    // Spring AI
    implementation(libs.bundles.springAi)
    // Kotlin reflection
    implementation(libs.kotlin.reflect)
    // Serialization
    implementation(libs.jackson.moduleKotlin)
    // Logging
    implementation(libs.kotlin.logging)
    // Docker Compose
    developmentOnly(libs.bundles.dockerCompose)
    // Spring Boot test
    testImplementation(libs.bundles.springBoot.test)
    // Kotlin test
    testImplementation(libs.kotlin.testJunit5)
    // Testcontainers
    testImplementation(libs.bundles.testcontainers)
    // JUnit launcher
    testRuntimeOnly(libs.junit.platformLauncher)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
