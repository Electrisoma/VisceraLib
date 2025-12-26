plugins {
    `kotlin-dsl`
    kotlin("jvm") version "2.2.20"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.kikugie.dev/snapshots")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.neoforged.net/releases/")
}

dependencies {
    implementation("dev.kikugie:stonecutter:0.7")
    implementation("net.fabricmc:fabric-loom:1.12-SNAPSHOT")
    implementation("net.neoforged.moddev:net.neoforged.moddev.gradle.plugin:2.0.115")
    implementation("org.vineflower:vineflower:1.11.2")
}