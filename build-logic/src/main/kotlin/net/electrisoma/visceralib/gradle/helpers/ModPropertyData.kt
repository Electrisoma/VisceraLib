package net.electrisoma.visceralib.gradle.helpers

import org.gradle.api.Project
import java.io.File

open class ModPropertyData(private val project: Project) {

    val id:           String get() = prop("mod_id")
    val name:         String get() = prop("mod_name")
    val version:      String get() = prop("mod_version")
    val group:        String get() = prop("mod_group")

    private val moduleList: List<String> get() = moduleProps.getProperty("module")
        ?.split(Regex("[\\s,_]+"))
        ?.filter { it.isNotBlank() }
        ?: emptyList()

    val module:       String get() = moduleList.joinToString("-").lowercase()
    val suffix:       String get() = moduleProps.getProperty("suffix") ?: ""
    val moduleVer:    String get() = moduleProps.getProperty("moduleVer") ?: ""

    val authors:      String get() = prop("mod_authors")
    val contributors: String get() = prop("mod_contributors")
    val description:  String get() = prop("mod_description")
    val license:      String get() = prop("mod_license")
    val github:       String get() = prop("mod_github")

    val mc:           String get() = prop("minecraft_version")
    val minMc:        String get() = prop("min_minecraft_version")
    val java:         String get() = prop("java_version")

    val moduleBase get() = moduleParts.joinToString("-")
    val modulePath get() = moduleParts.joinToString("_")

    val displayName: String get() {
        val formattedModules = moduleList.joinToString(" ") { it.replaceFirstChar(Char::titlecase) }
        val formattedSuffix = if (suffix.lowercase() == "api") "API" else suffix.replaceFirstChar(Char::titlecase)

        val mainName = listOfNotNull(name, formattedModules, formattedSuffix)
            .filter { it.isNotBlank() }
            .joinToString(" ")

        return if (moduleVer.isNotBlank()) "$mainName ($moduleVer)" else mainName
    }

    val resDir:    File get() = project.projectDir.resolve("src/main/resources")
    val commonRes: File get() = project.project(":$moduleBase-common").projectDir.resolve("src/main/resources")
    val commonAW:  File get() = commonResource("accesswideners/$mc-$moduleBase.accesswidener")

    fun resource(path: String):       File = resDir.resolve(path)
    fun commonResource(path: String): File = commonRes.resolve(path)

    fun ver(key: String): String = project.providers.gradleProperty(key).getOrElse("")
        .takeIf { it.isNotBlank() }
        ?: throw IllegalStateException("Version key '$key' is missing in gradle.properties")

    private fun prop(key: String): String = project.providers.gradleProperty(key).getOrElse("")
        .takeIf { it.isNotBlank() }
        ?: throw IllegalStateException("Property '$key' is missing in gradle.properties")

    private val moduleProps by lazy { project.props.load("../module.properties") }

    private val moduleParts: List<String> get() = buildList {
        add(id)
        addAll(moduleList)
        if (suffix.isNotBlank()) add(suffix)
        if (moduleVer.isNotBlank()) add(moduleVer)
    }.map { it.lowercase() }
}
