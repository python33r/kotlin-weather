plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka") version "1.9.20"
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("io.kotest:kotest-runner-junit5:5.9.0")
    testImplementation("io.kotest:kotest-assertions-core:5.9.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1")
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
    jvmArgs = listOf("-XX:+EnableDynamicAgentLoading")
}
