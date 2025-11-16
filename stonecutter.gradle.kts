import java.util.Properties

plugins {
    `maven-publish`
    id("dev.kikugie.stonecutter")
    id("dev.architectury.loom") version "1.9.+" apply false
    id("architectury-plugin") version "3.4.+" apply false
    id("com.gradleup.shadow") version "8.3.5" apply false
    id("me.modmuss50.mod-publish-plugin") version "0.7.4" apply false
    id("dev.ithundxr.silk") version "0.11.+"
}

stonecutter active "1.21.1" /* [SC] DO NOT EDIT */

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

subprojects {
    apply(plugin = "maven-publish")
    repositories {
        mavenCentral()
        maven("https://maven.parchmentmc.org")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases/")

        maven("https://maven.blamejared.com")
        maven("https://maven.createmod.net/")
        maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
        maven("https://jitpack.io/")
        maven("https://api.modrinth.com/maven")

        exclusiveContent {
            forRepository { maven { url = uri("https://api.modrinth.com/maven") } }
            filter { includeGroup("maven.modrinth") }
        }
        exclusiveContent {
            forRepository { maven { url = uri("https://cursemaven.com") } }
            filter { includeGroup("curse.maven") }
        }
        flatDir{ dir("libs") }
    }
    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/electrisoma/ElectrisomaMaven")
                credentials {
                    username = System.getenv("GITHUB_USERNAME") ?: localProperties.getProperty("mavenUsername", "")
                    password = System.getenv("GITHUB_TOKEN") ?: localProperties.getProperty("mavenToken", "")
                }
            }
//            maven {
//                name = "realRobotixMaven"
//                url = uri("https://maven.realrobotix.me/createbigcannons")
//                credentials(PasswordCredentials::class)
//            }
            mavenLocal()
        }
    }
}