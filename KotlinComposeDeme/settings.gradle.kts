import org.gradle.internal.impldep.com.google.gson.internal.bind.TypeAdapters.URI

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            name = "TarsosDSP repository"
            url = uri("https://mvn.0110.be/releases")
        }

    }
}

rootProject.name = "KotlinComposeDemo"
include(":app")
 