plugins {
    id("multiloader-common")
}

val commonJava: Configuration by configurations.creating {
    isCanBeResolved = true
}
val commonResources: Configuration by configurations.creating {
    isCanBeResolved = true
}

dependencies {
    val commonPath = commonNode.hierarchy.toString()
    compileOnly(project(path = commonPath))
    commonJava(project(path = commonPath, configuration = "commonJava"))
    commonResources(project(path = commonPath, configuration = "commonResources"))
}

tasks {
    jar {
        exclude("accesswideners", "accesswideners/**")
    }

    named<JavaCompile>("compileJava") {
        dependsOn(commonJava)
        source(commonJava)
    }

    named<ProcessResources>("processResources") {
        dependsOn(commonResources)
        from(commonResources)
    }
}

afterEvaluate {
    val folder = project.mod.module

    extensions.findByType<net.neoforged.moddevgradle.dsl.NeoForgeExtension>()?.apply {
        runs.all { ideFolderName.set(folder) }
    }

    extensions.findByType<net.fabricmc.loom.api.LoomGradleExtensionAPI>()?.apply {
        runs {
            all { ideConfigFolder.set(folder) }
            getByName("client") {
                programArgs("--username", "dev")
            }
        }
    }
}