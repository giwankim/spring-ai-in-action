plugins {
    id("de.fayard.refreshVersions") version "0.60.6"
}

rootProject.name = "spring-ai-in-action"

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        mavenCentral()
    }
}
