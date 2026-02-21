import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.api.fabricapi.FabricApiExtension
import net.fabricmc.loom.api.mappings.layered.spec.LayeredMappingSpecBuilder
import net.fabricmc.loom.api.mappings.layered.spec.MojangMappingsSpecBuilder
import net.fabricmc.loom.api.mappings.layered.spec.ParchmentMappingsSpecBuilder

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.maven

import java.io.File
import java.util.*

val Project.mod     get() = ModData(this)
val Project.fapi    get() = FabricHelpers(this)
val Project.props   get() = PropertyManager(this)
val Project.repos   get() = MavenHelpers(this)
val Project.finder  get() = ProjectFinder(this)
val Project.mapping get() = MappingManager(this)

class ModData(private val project: Project) {

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

class PropertyManager(private val project: Project) {

    fun load(path: String): Properties = Properties().apply {
        project.file(path).takeIf { it.exists() }?.inputStream()?.use(::load)
    }

    fun fromRoot(path: String): Properties = Properties().apply {
        project.rootProject.file(path).takeIf { it.exists() }?.inputStream()?.use(::load)
    }

    fun getFileProp(path: String, key: String): String? = load(path).getProperty(key)

    fun local(key: String): String? = fromRoot("local.properties").getProperty(key)

    val loader: String? get() = getFileProp("loader.properties", "loader")
}

class FabricHelpers(private val project: Project) {

    fun embed(name: String) {
        val dep = api.module(name, fapiVersion)
        project.dependencies.add("modApi", dep)
        project.dependencies.add("include", dep)
    }

    fun runtime(name: String) {
        val dep = api.module(name, fapiVersion)
        project.dependencies.add("modRuntimeOnly", dep)
    }

    private val fapiVersion get() = "${project.mod.ver("fabric_api")}+${project.mod.mc}"
    private val api get() = project.extensions.getByName("fabricApi") as FabricApiExtension
}

class MavenHelpers(private val project: Project) {

    fun modrinth(name: String, version: String) = "maven.modrinth:$name:$version"
    fun curseforge(name: String, pId: String, fId: String) = "curse.maven:$name-$pId:$fId"

    fun strictMaven(url: String, vararg coords: String) {
        project.repositories.exclusiveContent {
            forRepository { project.repositories.maven(url) }
            filter {
                coords.forEach {
                    if (":" in it) {
                        val (g, m) = it.split(":", limit = 2)
                        includeModule(g, m)
                    } else includeGroup(it)
                }
            }
        }
    }
}

class ProjectFinder(private val project: Project) {

    val common   get() = vProjects.filter { it.name.endsWith("-common") }
    val fabric   get() = vProjects.filter { it.name.endsWith("-fabric") }
    val neoforge get() = vProjects.filter { it.name.endsWith("-neoforge") }

    fun dependOn(projects: List<Project>): List<Project> {
        projects.forEach { project.evaluationDependsOn(it.path) }
        return projects
    }

    private val vProjects = project.rootProject.childProjects.values
        .filter { it.name.startsWith("visceralib-") && it != project }
}

class MappingManager(private val project: Project) {

    fun layered(configure: MappingBuilder.() -> Unit): Dependency {
        val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")

        @Suppress("UnstableApiUsage")
        return loom.layered {
            val builder = MappingBuilder(this)
            builder.configure()
        }
    }
}

@Suppress("UnstableApiUsage")
class MappingBuilder(private val internal: LayeredMappingSpecBuilder) {

    fun officialMojangMappings() {
        internal.officialMojangMappings()
    }

    fun officialMojangMappings(action: Action<MojangMappingsSpecBuilder>) {
        internal.officialMojangMappings(action)
    }

    fun parchment(notation: Any) {
        internal.parchment(notation)
    }

    fun parchment(notation: Any, action: Action<ParchmentMappingsSpecBuilder>) {
        internal.parchment(notation, action)
    }

    fun signatureFix(notation: Any) {
        internal.signatureFix(notation)
    }

    fun mappings(file: Any) {
        internal.mappings(file)
    }
}
