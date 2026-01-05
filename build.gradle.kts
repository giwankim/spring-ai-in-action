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
    implementation(libs.springBoot.starterValidation)
    implementation(libs.springBoot.starterWeb)
    implementation(libs.jackson.moduleKotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.springAi.starterModelOpenai)
    implementation(libs.kotlin.logging)
    testImplementation(libs.springBoot.starterTest)
    testImplementation(libs.kotlin.testJunit5)
    testRuntimeOnly(libs.junit.platformLauncher)
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

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
