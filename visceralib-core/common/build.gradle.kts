plugins {
    alias(libs.plugins.multiloader.common)
    alias(libs.plugins.loader.mdg)
    alias(libs.plugins.fletchingtable.neo)
}

fletchingTable {
    j52j.register("main") { extension("json", "**/*.json5") }

    accessConverter.register("main") {
        add("accesswideners/${mod.mc}-${mod.moduleBase}.accesswidener")
    }
}

dependencies {
    compileOnly("net.fabricmc:sponge-mixin:${mod.ver("sponge_mixin")}")

    val mixinExtras = "io.github.llamalad7:mixinextras-common:${mod.ver("mixin_extras")}"
    annotationProcessor(mixinExtras)
    compileOnly(mixinExtras)
}

val syncAT = tasks.register<Copy>("syncAT") {
    dependsOn(tasks.processResources)
    from(layout.buildDirectory.dir("resources/main/META-INF"))
    include("accesstransformer.cfg")
    into(layout.buildDirectory.dir("generated/at"))
}

neoForge {
    neoFormVersion = mod.ver("neoform")

    parchment {
        mod.ver("parchment").let {
            mappingsVersion = it
            minecraftVersion = mod.mc
        }
    }

    accessTransformers {
        from(syncAT.map { it.destinationDir.resolve("accesstransformer.cfg") })
        publish(syncAT.map { it.destinationDir.resolve("accesstransformer.cfg") })
    }

    interfaceInjectionData {
        mod.commonResource("interfaces.json").let {
            from(it)
            publish(it)
        }
    }
}

val commonJava: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

val commonResources: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

artifacts {
    sourceSets.main.get().let { main ->
        main.java.sourceDirectories.forEach { add(commonJava.name, it) }
        main.resources.sourceDirectories.forEach { add(commonResources.name, it) }
    }
}