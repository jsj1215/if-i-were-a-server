pluginManagement {
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings

    repositories {
        gradlePluginPortal()
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.springframework.boot" -> useVersion(springBootVersion)
                "io.spring.dependency-management" -> useVersion(springDependencyManagementVersion)
            }
        }
    }
}

plugins {
    // JDK 21 이 없는 환경에서도 clone 직후 빌드되도록 toolchain 을 자동으로 받아온다.
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

rootProject.name = "if-i-were-a-server"

include(
    ":apps:commerce-api",
    ":apps:commerce-streamer",
    ":modules:jpa",
    ":modules:redis",
    ":modules:kafka",
)
