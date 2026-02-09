import java.util.*

plugins {
    id("java")
    id("java-library")
    `maven-publish`
    idea
}

base {
    version = "${project.mod.version}+mc${project.stonecutterBuild.current.version}-${project.loader}"

    val mName = findProperty("module")?.toString()?.takeIf { it.isNotBlank() }
    val mSuffix = findProperty("suffix")?.toString()?.takeIf { it.isNotBlank() }
    val mVer = findProperty("module_version")?.toString()?.takeIf { it.isNotBlank() }
    val modulePart = listOfNotNull(mName, mSuffix, mVer).joinToString("-")

    archivesName = project.mod.id + modulePart.takeIf { it.isNotBlank() }?.let { "-$it" }.orEmpty()
}

java {
    toolchain.languageVersion.set(project.providers.provider {
        JavaLanguageVersion.of(project.mod.dep("java_version").toInt())
    })

    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()

    strictMaven("https://maven.parchmentmc.org", "org.parchmentmc.data")
    strictMaven("https://api.modrinth.com/maven", "maven.modrinth")
    strictMaven("https://cursemaven.com", "curse.maven")
    strictMaven("https://repo.spongepowered.org/repository/maven-public", "org.spongepowered")
    strictMaven("https://maven.terraformersmc.com/releases/", "com.terraformersmc")
    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
}

dependencies {
    annotationProcessor("com.google.auto.service:auto-service:${project.mod.dep("auto_service")}")
    compileOnly("com.google.auto.service:auto-service-annotations:${project.mod.dep("auto_service")}")
    api("com.google.code.findbugs:jsr305:${project.mod.dep("find_bugs")}")
}

tasks {
    val modId: String = project.mod.id
    val moduleSuffix = project.module
    val namespace: String = if (moduleSuffix.isNullOrBlank()) modId else "${modId}_$moduleSuffix"

    val expandProps = mapOf(
        "java"               to project.mod.dep("java_version"),
        "compatibilityLevel" to "JAVA_${project.mod.dep("java_version")}",
        "id"                 to project.mod.id,
        "name"               to project.mod.name,
        "version"            to project.mod.version,
        "group"              to project.mod.group,
        "authors"            to project.mod.authors,
        "contributors"       to project.mod.contributors,
        "description"        to project.mod.description,
        "license"            to project.mod.license,
        "github"             to project.mod.github,
        "minecraft"          to project.mod.mc,
        "loader"             to project.loader,
        "minMinecraft"       to project.mod.dep("min_minecraft_version"),
        "fabric"             to project.mod.depOrNull("fabric_loader"),
        "FApi"               to project.mod.depOrNull("fabric_api"),
        "neoForge"           to project.mod.depOrNull("neoforge")
    ).filterValues { !it.isNullOrBlank() }

    val jsonExpandProps: Map<String, String> = expandProps.mapValues { (_, v) -> v.toString().replace("\n", "\\\\n") }

    val jsonFiles = listOf(
        "pack.mcmeta",
        "fabric.mod.json",
        "*.mixins.json",
        "**/*.mixins.json",
        "*.mixins.json5",
        "**/*.mixins.json5",
    )

    processResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        val masterLogo = rootProject.file("branding/logo.png")
        if (masterLogo.exists()) {
            from(masterLogo) {
                into("assets/$namespace")
            }
        }

        filesMatching(listOf("META-INF/neoforge.mods.toml")) {
            expand(expandProps)
        }

        filesMatching(jsonFiles) {
            expand(jsonExpandProps)
        }

        inputs.properties(expandProps)
    }

    withType<ProcessResources>().configureEach {
        mustRunAfter(tasks.matching { it.name.contains("stonecutterGenerate") })
    }

    jar {
        manifest {
            attributes(
                "Fabric-Loom-Remap" to "true"
            )
        }
    }

    javadoc {
        exclude("**/package-info.java")
        exclude("net/electrisoma/visceralib/impl/**")
        exclude("net/electrisoma/visceralib/mixin/**")
        exclude("net/electrisoma/visceralib/platform/**")
    }
}

val localProps = Properties().apply {
    project.rootDir.resolve("local.properties").takeIf { it.exists() }?.inputStream()?.use(::load)
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            artifactId = base.archivesName.get()
            groupId = "${project.mod.group}.${project.mod.id}"
            from(components["java"])
        }
    }

    repositories {
//        maven {
//            name = "GitHubPackages"
//            url = uri("https://maven.pkg.github.com/electrisoma/VisceraLib")
//            credentials {
//                username = System.getenv("GITHUB_ACTOR") ?: localProps.getProperty("ghpUsername")
//                password = System.getenv("GITHUB_TOKEN") ?: localProps.getProperty("ghpToken")
//            }
//        }
        mavenLocal()
    }
}