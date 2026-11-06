import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management")
}

allprojects {
    val projectGroup: String by project
    group = projectGroup
    version = "0.0.1"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        // Lombok
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        testCompileOnly("org.projectlombok:lombok")
        testAnnotationProcessor("org.projectlombok:lombok")
        // Test
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.boot:spring-boot-testcontainers")
        testImplementation("org.testcontainers:junit-jupiter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    // modules 는 일반 jar, apps 만 실행 가능한 BootJar 를 만든다.
    tasks.withType(Jar::class) { enabled = true }
    tasks.withType(BootJar::class) { enabled = false }

    configure(allprojects.filter { it.parent?.name.equals("apps") }) {
        tasks.withType(Jar::class) { enabled = false }
        tasks.withType(BootJar::class) { enabled = true }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        systemProperty("user.timezone", "Asia/Seoul")
    }
}

// 모듈을 묶기만 하는 컨테이너 프로젝트는 task 를 실행하지 않는다.
project("apps") { tasks.configureEach { enabled = false } }
project("modules") { tasks.configureEach { enabled = false } }
