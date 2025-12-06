plugins {
    `multiloader-loader`
    id("net.neoforged.moddev")
    id("dev.kikugie.fletching-table.neoforge") version "0.1.0-alpha.22"
}

val main = sourceSets.getByName("main")
val testmod = sourceSets.create("testmod") {
    compileClasspath += main.compileClasspath
    runtimeClasspath += main.runtimeClasspath
}

val commonProjectPath: String = project.parent!!.parent!!.path + ":common:"
val versionedCommonProjectPath: String = commonProjectPath + currentMod.mc

configurations.getByName("testmodImplementation").extendsFrom(configurations.getByName("implementation"))
configurations.getByName("testmodRuntimeClasspath").extendsFrom(configurations.getByName("runtimeClasspath"))

fletchingTable {
    j52j.register("main") {
        extension("json", "**/*.json5")
    }

    accessConverter.register("main") {
        add("accesswideners/${currentMod.mc}-visceralib_core.accesswidener")
    }
}

neoForge {
    enable {
        version = currentMod.dep("neoforge")
    }
}

dependencies {
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
    }

    parchment {
        currentMod.depOrNull("parchment")?.let {
            mappingsVersion = it
            minecraftVersion = currentMod.mc
        }
    }

    mods {
        register("visceralib_core") {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main {
    resources.srcDir("src/generated/resources")
}

tasks {
    processResources {
        exclude("visceralib_core.accesswidener")
    }
}

//tasks.named("createMinecraftArtifacts") {
//    dependsOn(":neoforge:${currentMod.propOrNull("minecraft_version")}:processResources")
//}