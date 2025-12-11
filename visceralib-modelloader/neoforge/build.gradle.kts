plugins {
    `multiloader-loader`
    id("net.neoforged.moddev")
    id("dev.kikugie.fletching-table.neoforge") version "0.1.0-alpha.22"
}

val visceraLibCorePathCommon: String = ":visceralib-core:common:${currentMod.mc}"
val visceraLibCorePathLoader: String = ":visceralib-core:neoforge:${currentMod.mc}"

val main = sourceSets.getByName("main")
val testmod = sourceSets.create("testmod") {
    compileClasspath += main.compileClasspath
    runtimeClasspath += main.runtimeClasspath
}

val commonProjectPath: String = project.parent!!.parent!!.path + ":common:"
val versionedCommonProjectPath: String = commonProjectPath + currentMod.mc

val dependencyProjects: List<Project> = listOf(
    project(visceraLibCorePathCommon),
    project(visceraLibCorePathLoader),
)

dependencyProjects.forEach {
    project.evaluationDependsOn(it.path)
}

configurations.getByName("testmodImplementation").extendsFrom(configurations.getByName("implementation"))
configurations.getByName("testmodRuntimeClasspath").extendsFrom(configurations.getByName("runtimeClasspath"))

fletchingTable {
    j52j.register("main") {
        extension("json", "**/*.json5")
    }

    accessConverter.register("main") {
        add("accesswideners/${currentMod.mc}-visceralib_modelloader.accesswidener")
    }
}

neoForge {
    enable {
        version = currentMod.dep("neoforge")
    }
}

dependencies {
    dependencyProjects.forEach {
        implementation(it)
    }

    afterEvaluate {
        "testmodImplementation"(main.output)
        "testmodImplementation"(project(versionedCommonProjectPath).sourceSets.getByName("testmod").output)
    }
}

neoForge {
//    val at = project.file("build/resources/main/META-INF/accesstransformer.cfg");
//
//    accessTransformers.from(at.absolutePath)
//    validateAccessTransformers = true

    runs {
        register("client") {
            client()
            ideName = "NeoForge Client (${project.path})"
        }
        register("server") {
            server()
            ideName = "NeoForge Server (${project.path})"
        }
        register("testmodClient") {
            client()
            sourceSet = sourceSets.getByName("testmod")
            ideName = "TestMod NeoForge Client (${project.path})"
        }
    }

    parchment {
        currentMod.depOrNull("parchment")?.let {
            mappingsVersion = it
            minecraftVersion = currentMod.mc
        }
    }

    mods {
        register("visceralib_modelloader") {
            sourceSet(sourceSets.main.get())
        }
//        register("testmod") {
//            sourceSet(sourceSets.getByName("testmod"))
//        }
    }
}

sourceSets.main {
    resources.srcDir("src/generated/resources")
}

tasks {
    processResources {
        exclude("visceralib_modelloader.accesswidener")
    }
}

//tasks.named("createMinecraftArtifacts") {
//    dependsOn(":neoforge:${currentMod.propOrNull("minecraft_version")}:processResources")
//}