import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

plugins {
    id("java")
    id("java-library")
    id("com.diffplug.spotless")
    `maven-publish`
    idea
}

val moduleParts = listOfNotNull(
    mod.id,
    mod.module,
    mod.suffix,
    mod.moduleVer
).filter { it.isNotBlank() }
val moduleName = moduleParts.joinToString("_")

group = "${mod.group}.${mod.id}"
version = "${mod.version}+mc${mod.mc}"
base.archivesName = "${mod.moduleBase}-${props.loader}"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(mod.java))

    withSourcesJar()
    withJavadocJar()
}

spotless {
    java {
        target("src/*/*/net/electrisoma/visceralib/**/*.java")

        endWithNewline()
        trimTrailingWhitespace()
        removeUnusedImports()
        importOrder("net.electrisoma", "net.fabricmc", "net.neoforged", "net.minecraft", "com.mojang", "", "java", "javax")
        leadingSpacesToTabs(4)

        replaceRegex("newline after class opening", "((?:class|interface|enum)\\b[^\\{]*\\{\\n)(?!\\n)", "$1\n")
        replaceRegex("class-level javadoc indentation fix", "^\\*", " *")
        replaceRegex("method-level javadoc indentation fix", "\t\\*", "\t *")
    }
}

repositories {
    mavenCentral()

    repos.strictMaven("https://maven.parchmentmc.org", "org.parchmentmc.data")
    repos.strictMaven("https://api.modrinth.com/maven", "maven.modrinth")
    repos.strictMaven("https://cursemaven.com", "curse.maven")
    repos.strictMaven("https://repo.spongepowered.org/repository/maven-public", "org.spongepowered")
    repos.strictMaven("https://maven.terraformersmc.com/releases/", "com.terraformersmc")
    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
}

dependencies {
    annotationProcessor(libs.autoservice)
    compileOnly(libs.autoservice.annotations)
    api(libs.findbugs)
}

tasks {
    val expandProps = mapOf(
        "java"               to mod.java,
        "compatibilityLevel" to "JAVA_${mod.java}",
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
        "minMinecraft"       to mod.minMc,
        "fabric"             to mod.ver("fabric_loader"),
        "fapi"               to mod.ver("fabric_api"),
        "neoForge"           to mod.ver("neoforge")
    )

    val jsonExpandProps: Map<String, String> = expandProps.mapValues { (_, v) -> v.replace("\n", "\\\\n") }

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
                into("assets/$moduleName")
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
            attributes(mapOf(
                "Fabric-Loom-Remap"      to "true",
                "Specification-Title"    to mod.name,
                "Specification-Vendor"   to mod.authors,
                "Specification-Version"  to mod.version,
                "Implementation-Title"   to name,
                "Implementation-Version" to mod.version,
                "Implementation-Vendor"  to mod.authors,
                "Built-On-Minecraft"     to mod.mc
            ))
        }
    }

    javadoc {
        exclude("**/package-info.java")
        exclude("net/electrisoma/visceralib/impl/**")
        exclude("net/electrisoma/visceralib/mixin/**")
        exclude("net/electrisoma/visceralib/platform/**")
    }

    withType<JavaCompile> {
        dependsOn("spotlessApply")
        options.compilerArgs.add("-Xlint:unchecked")
    }
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
//                username = System.getenv("GITHUB_ACTOR") ?: props.local("ghpUsername")
//                password = System.getenv("GITHUB_TOKEN") ?: props.local("ghpToken")
//            }
//        }
        mavenLocal()
    }
}