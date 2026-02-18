package net.electrisoma.visceralib.event.rendering.v1.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.Supplier;

public final class ModelRegistrationEvents {

    @FunctionalInterface
    public interface Layer {

        void register(ModelLayerLocation location, Supplier<LayerDefinition> definition);
    }
}
