import net.electrisoma.visceralib.gradle.helpers.*

plugins.apply("net.electrisoma.visceralib.gradle")

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

    withType<ProcessResources> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        rootProject.file("branding/logo.png").takeIf { it.exists() }?.let { logo ->
            from(logo) { into("assets/${mod.modulePath}") }
        }

        filesMatching(listOf("META-INF/neoforge.mods.toml")) {
            expand(expandProps)
        }

        filesMatching(jsonFiles) {
            expand(jsonExpandProps)
        }

        inputs.properties(expandProps)
    }
}