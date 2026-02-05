plugins {
    id("de.fayard.refreshVersions") version "0.60.6"
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        mavenCentral()
    }
}

rootProject.name = "spring-ai-in-action"

include("board-game-buddy")
