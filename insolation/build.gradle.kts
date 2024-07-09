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
    mainClass = "org.efford.weather.MainKt"
}

tasks.named<JavaExec>("run") {
    args = listOf("../data/leeds_2019.csv", "2019-07-01")
}
