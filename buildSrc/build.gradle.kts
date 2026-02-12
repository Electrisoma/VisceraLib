import java.util.Properties

plugins {
    `kotlin-dsl`
    kotlin("jvm") version "2.2.20"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.fabricmc.net/")
    maven("https://maven.neoforged.net/releases/")
    maven("https://maven.kikugie.dev/snapshots")
}

val props = Properties().apply {
    rootDir.parentFile.resolve("gradle.properties").inputStream().use { load(it) }
}

dependencies {
    implementation("net.fabricmc.fabric-loom-remap:net.fabricmc.fabric-loom-remap.gradle.plugin:${props["loom"]}")
    implementation("net.neoforged.moddev:net.neoforged.moddev.gradle.plugin:${props["mdg"]}")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:8.1.0")
    //implementation("dev.kikugie:stonecutter:${props["stonecutter"]}")
}