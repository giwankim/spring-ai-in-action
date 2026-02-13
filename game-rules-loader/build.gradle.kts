plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)
    alias(libs.plugins.ktlint)
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Game Rules Loader"

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

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

dependencyManagement {
    imports {
        mavenBom(libs.springAi.bom.get().toString())
        mavenBom(libs.springCloud.bom.get().toString())
        mavenBom(libs.springCloudFn.bom.get().toString())
    }
}

dependencies {
    // Spring Boot
    implementation(libs.springBoot.starterWebmvc)
    // Spring AI
    implementation(libs.springAi.advisorsVectorStore)
    implementation(libs.springAi.starterModelOpenai)
    implementation(libs.springAi.starterVectorStoreQdrant)
    implementation(libs.springAi.tikaDocumentReader)
    // Spring Cloud Function
    implementation(libs.springCloud.functionContext)
    implementation(libs.springCloudFn.fileSupplier)
    // Kotlin reflection
    implementation(libs.kotlin.reflect)
    // Serialization
    implementation(libs.jackson.moduleKotlin)
    // Logging
    implementation(libs.kotlin.logging)
    // Test
    testImplementation(libs.springBoot.starterWebmvcTest)
    testImplementation(libs.kotlin.testJunit5)
    testRuntimeOnly(libs.junit.platformLauncher)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
