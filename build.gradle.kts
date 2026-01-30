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
    implementation(libs.springBoot.starterWeb)

    // Spring AI
    implementation(libs.springAi.starterMcpClient)
    implementation(libs.springAi.starterModelOpenai)

    // Kotlin
    implementation(libs.kotlin.reflect)

    // Logging
    implementation(libs.kotlin.logging)

    // Serialization
    implementation(libs.jackson.moduleKotlin)

    // Testing
    testImplementation(libs.springBoot.starterTest)
    testImplementation(libs.kotlin.testJunit5)
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
