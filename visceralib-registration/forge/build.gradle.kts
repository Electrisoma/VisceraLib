plugins {
    `multiloader-loader`
    id("net.neoforged.moddev.legacyforge")
    id("dev.kikugie.fletching-table.lexforge") version "0.1.0-alpha.22"
}

fletchingTable {
    j52j.register(sourceSets.main) {
        extension("json", "**/*.json5")
    }

    accessConverter.register(sourceSets.main) {
        add("accesswideners/${commonMod.mc}-${commonMod.id}.accesswidener")
    }
}

legacyForge {
    enable {
        forgeVersion = "${mod.mc}-${commonMod.dep("forge")}"
    }
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")
    annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT:processor")
    implementation("me.xdrop:fuzzywuzzy:1.4.0")
    jarJar("me.xdrop:fuzzywuzzy:1.4.0")
    runtimeOnly("me.xdrop:fuzzywuzzy:1.4.0")
}

legacyForge {
    runs {
        register("client") {
            client()
            ideName = "Forge Client (${project.path})"
        }
        register("server") {
            server()
            ideName = "Forge Server (${project.path})"
        }
    }

    parchment {
        commonMod.depOrNull("parchment")?.let {
            mappingsVersion = it
            minecraftVersion = commonMod.mc
        }
    }

    mods {
        register(commonMod.id) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main {
    resources.srcDir("src/generated/resources")
}

tasks {
    processResources {
        exclude("accesswideners/${commonMod.mc}-${commonMod.id}.accesswidener")
    }
}

tasks.named("createMinecraftArtifacts") {
    dependsOn(":forge:${commonMod.propOrNull("minecraft_version")}:processResources")
}