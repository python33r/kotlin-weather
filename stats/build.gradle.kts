plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":lib"))
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "org.efford.weather.StatsKt"
}

tasks.named<JavaExec>("run") {
    args = listOf("../data/leeds_2019.csv")
}
