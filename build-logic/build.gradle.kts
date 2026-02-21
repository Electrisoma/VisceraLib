plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    kotlin("jvm") version "2.2.20"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    //maven("https://maven.fabricmc.net/")
    //maven("https://maven.neoforged.net/releases/")
    //maven("https://maven.kikugie.dev/snapshots")
}

gradlePlugin {
    plugins {
        register("visceralibGradle") {
            id = "net.electrisoma.visceralib.gradle"
            implementationClass = "net.electrisoma.visceralib.gradle.VisceraLibPlugin"
        }
    }
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.spotless)
    implementation(libs.tasktree)

    //compileOnly(libs.loom)
}