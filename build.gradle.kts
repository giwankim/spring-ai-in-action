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

dependencies {
    implementation(libs.spring.boot.starterValidation)
    implementation(libs.spring.boot.starterWeb)
    implementation(libs.jackson.moduleKotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.spring.ai.starterModelOpenai)
    implementation(libs.kotlin.logging)
    testImplementation(libs.spring.boot.starterTest)
    testImplementation(libs.kotlin.testJunit5)
    testRuntimeOnly(libs.junit.platformLauncher)
}

dependencyManagement {
    imports {
        mavenBom(
            libs.spring.ai.bom
                .get()
                .toString(),
        )
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
