package net.electrisoma.visceralib.gradle.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.language.jvm.tasks.ProcessResources
import org.gradle.kotlin.dsl.*
import net.electrisoma.visceralib.gradle.extensions.mod

class VisceraMetadataPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            val expandProps = mapOf(
                "version"            to mod.version,
                "id"                 to mod.id,
                "name"               to mod.name,
                "group"              to mod.group,
                "description"        to mod.description,
                "contributors"       to mod.contributors,
                "authors"            to mod.authors,
                "license"            to mod.license,
                "github"             to mod.github,
                "minecraft"          to mod.mc,
                "minMinecraft"       to mod.minMc,
                "neoForge"           to mod.ver("neoforge"),
                "fabric"             to mod.ver("fabric_loader"),
                "fapi"               to mod.ver("fabric_api"),
                "java"               to mod.java,
                "compatibilityLevel" to "JAVA_${mod.java}"
            )

            val jsonExpandProps: Map<String, String> = expandProps.mapValues { (_, v) -> v.replace("\n", "\\\\n") }

            val jsonFiles = listOf("**/*.json", "**/*.json5")
            val tomlFiles = listOf("META-INF/neoforge.mods.toml")

            val logoFile = rootProject.layout.projectDirectory.file("branding/logo.png")

            tasks.withType<ProcessResources>().configureEach {
                duplicatesStrategy = DuplicatesStrategy.EXCLUDE

                if (logoFile.asFile.exists()) from(logoFile) { into("assets/${mod.modulePath}") }
                
                filesMatching(jsonFiles) { expand(jsonExpandProps) }
                filesMatching(tomlFiles) { expand(expandProps) }

                inputs.properties(expandProps)
            }
        }
    }
}
