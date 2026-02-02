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
    // Spring Boot
    implementation(libs.bundles.springBoot)

    // Spring AI
    implementation(libs.bundles.springAi)

    // Kotlin & Serialization
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.logging)
    implementation(libs.jackson.moduleKotlin)

    // Database
    implementation(libs.bundles.database)
    runtimeOnly(libs.h2)

    // Docker Compose (development)
    developmentOnly(libs.bundles.dockerCompose)

    // Testing
    testImplementation(libs.bundles.testing)
    testImplementation(libs.bundles.testcontainers)
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
