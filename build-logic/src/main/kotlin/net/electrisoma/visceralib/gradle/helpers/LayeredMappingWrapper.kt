package net.electrisoma.visceralib.gradle.helpers

import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.api.mappings.layered.spec.LayeredMappingSpecBuilder
import net.fabricmc.loom.api.mappings.layered.spec.MojangMappingsSpecBuilder
import net.fabricmc.loom.api.mappings.layered.spec.ParchmentMappingsSpecBuilder
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.kotlin.dsl.getByName

open class LayeredMappingWrapper(private val project: Project) {

    fun layered(configure: MappingBuilder.() -> Unit): Dependency {
        val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")

        @Suppress("UnstableApiUsage")
        return loom.layered {
            val builder = MappingBuilder(this)
            builder.configure()
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
}