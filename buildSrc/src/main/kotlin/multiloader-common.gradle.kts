import java.util.*

plugins {
    id("java")
    id("java-library")
    `maven-publish`
    idea
}

version = "${mod.version}+mc${stonecutterBuild.current.version}-${loader}"
group = "${mod.group}.${mod.id}"

base {
    val mName = findProperty("module")?.toString()?.takeIf { it.isNotBlank() }
    val mSuffix = findProperty("suffix")?.toString()?.takeIf { it.isNotBlank() }
    val mVer = findProperty("module_version")?.toString()?.takeIf { it.isNotBlank() }
    val modulePart = listOfNotNull(mName, mSuffix, mVer).joinToString("-")

    archivesName = mod.id + modulePart.takeIf { it.isNotBlank() }?.let { "-$it" }.orEmpty()
}

java {
    toolchain.languageVersion.set(providers.provider {
        JavaLanguageVersion.of(mod.dep("java_version").toInt())
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
    annotationProcessor("com.google.auto.service:auto-service:${mod.dep("auto_service")}")
    compileOnly("com.google.auto.service:auto-service-annotations:${mod.dep("auto_service")}")
    api("com.google.code.findbugs:jsr305:${mod.dep("find_bugs")}")
}

tasks {
    val modId: String = mod.id
    val moduleSuffix = module
    val namespace: String = if (moduleSuffix.isNullOrBlank()) modId else "${modId}_$moduleSuffix"

    val expandProps = mapOf(
        "java"               to mod.dep("java_version"),
        "compatibilityLevel" to "JAVA_${mod.dep("java_version")}",
        "id"                 to mod.id,
        "name"               to mod.name,
        "version"            to mod.version,
        "group"              to mod.group,
        "authors"            to mod.authors,
        "contributors"       to mod.contributors,
        "description"        to mod.description,
        "license"            to mod.license,
        "github"             to mod.github,
        "minecraft"          to mod.mc,
        "loader"             to loader,
        "minMinecraft"       to mod.dep("min_minecraft_version"),
        "fabric"             to mod.depOrNull("fabric_loader"),
        "FApi"               to mod.depOrNull("fabric_api"),
        "neoForge"           to mod.depOrNull("neoforge")
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
        from(rootProject.file("LICENSE")) {
            rename { "${it}_${mod.name}" }
        }

        manifest {
            attributes(
                "Fabric-Loom-Remap"      to "true",
                "Specification-Title"    to mod.name,
                "Specification-Vendor"   to mod.authors,
                "Specification-Version"  to mod.version,
                "Implementation-Title"   to name,
                "Implementation-Version" to mod.version,
                "Implementation-Vendor"  to mod.authors,
                "Built-On-Minecraft"     to mod.mc
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
    rootDir.resolve("local.properties").takeIf { it.exists() }?.inputStream()?.use(::load)
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
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