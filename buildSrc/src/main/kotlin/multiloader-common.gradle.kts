import java.util.*

plugins {
    id("java")
    id("java-library")
    `maven-publish`
    idea
}

base {
    version = "${currentMod.version}+mc${stonecutterBuild.current.version}-${loader}"
    archivesName = "${currentMod.id}-${currentMod.module}"
}

java {
    toolchain.languageVersion.set(project.providers.provider {
        JavaLanguageVersion.of(currentMod.prop("java.version").toInt())
    })

    withSourcesJar()
    withJavadocJar()
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

repositories {
    mavenCentral()

    strictMaven("https://maven.parchmentmc.org", "org.parchmentmc.data")
    strictMaven("https://api.modrinth.com/maven", "maven.modrinth")
    strictMaven("https://cursemaven.com", "curse.maven")
    strictMaven("https://repo.spongepowered.org/repository/maven-public", "org.spongepowered")
    strictMaven("https://maven.terraformersmc.com/releases/", "com.terraformersmc")
}

dependencies {
    annotationProcessor("com.google.auto.service:auto-service:${currentMod.propOrNull("auto_service")}")
    compileOnly("com.google.auto.service:auto-service-annotations:${currentMod.propOrNull("auto_service")}")
    api("com.google.code.findbugs:jsr305:${currentMod.propOrNull("find_bugs")}")
}

tasks {

    processResources {
        val expandProps = mapOf(
            "java"               to currentMod.propOrNull("java.version"),
            "compatibilityLevel" to currentMod.propOrNull("java.version")?.let { "JAVA_$it" },
            "id"                 to currentMod.id,
            "name"               to currentMod.name,
            "module"             to currentMod.module,
            "moduleCaps"         to currentMod.moduleCaps,
            "version"            to currentMod.version,
            "group"              to currentMod.group,
            "authors"            to currentMod.authors,
            "contributors"       to currentMod.contributors,
            "description"        to currentMod.description,
            "license"            to currentMod.license,
            "github"             to currentMod.github,
            "minecraft"          to currentMod.propOrNull("minecraft_version"),
            "minMinecraft"       to currentMod.propOrNull("min_minecraft_version"),
            "fabric"             to currentMod.depOrNull("fabric-loader"),
            "FApi"               to currentMod.depOrNull("fabric-api"),
            "neoForge"           to currentMod.depOrNull("neoforge")
        ).filterValues { it?.isNotEmpty() == true }.mapValues { (_, v) -> v!! }

        val jsonExpandProps = expandProps.mapValues { (_, v) -> v.replace("\n", "\\\\n") }

        filesMatching(listOf("META-INF/neoforge.mods.toml")) {
            expand(expandProps)
        }

        filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "*.mixins.json", "**/*.mixins.json")) {
            expand(jsonExpandProps)
        }

        inputs.properties(expandProps)
    }
}

tasks.matching { it.name == "processResources" }.configureEach {
    mustRunAfter(tasks.matching { it.name.contains("stonecutterGenerate") })
}

val localProperties: Properties = Properties()
val localPropertiesFile: File = rootProject.file("local.properties")
if (localPropertiesFile.exists())
    localPropertiesFile.inputStream().use(localProperties::load)

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            artifactId = base.archivesName.get()
            groupId = "${currentMod.group}.${currentMod.id}"
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/electrisoma/VisceraLib")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
//                username = System.getenv("GITHUB_ACTOR") ?: localProperties.getProperty("mavenUsername", "")
//                password = System.getenv("GITHUB_TOKEN") ?: localProperties.getProperty("mavenToken", "")
            }
        }
        mavenLocal()
    }
}