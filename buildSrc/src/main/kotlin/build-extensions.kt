import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven
import java.util.Properties

val Project.mod get() = ModData(this)
val Project.loader: String? get() = propFromFile("loader.properties", "loader")

class ModData(private val project: Project) {

    val id:           String get() = prop("mod_id")
    val name:         String get() = prop("mod_name")
    val version:      String get() = prop("mod_version")
    val group:        String get() = prop("mod_group")

    val module:       String get() = moduleProps.getProperty("module") ?: ""
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

    fun ver(key: String): String = project.providers.gradleProperty(key).getOrElse("")
        .takeIf { it.isNotBlank() }
        ?: throw IllegalStateException("Version key '$key' is missing in gradle.properties")

    private fun prop(key: String): String = project.providers.gradleProperty(key).getOrElse("")
        .takeIf { it.isNotBlank() }
        ?: throw IllegalStateException("Property '$key' is missing in gradle.properties")

    private val moduleProps: Properties by lazy { project.loadProps("../module.properties") }
}

private fun Project.loadProps(path: String): Properties = Properties().apply {
    file(path).takeIf { it.exists() }?.inputStream()?.use(::load)
}

private fun Project.propFromFile(path: String, key: String): String? =
    loadProps(path).getProperty(key)

fun modrinth(name: String, version: String) = "maven.modrinth:$name:$version"
fun curseforge(name: String, projectId: String, fileId: String) = "curse.maven:$name-$projectId:$fileId"

fun RepositoryHandler.strictMaven(url: String, vararg coords: String) {
    exclusiveContent {
        forRepository { maven(url) }
        filter {
            coords.forEach { coordinate ->
                if (":" in coordinate) {
                    val (group, module) = coordinate.split(":", limit = 2)
                    includeModule(group, module)
                } else {
                    includeGroup(coordinate)
                }
            }
        }
    }
}