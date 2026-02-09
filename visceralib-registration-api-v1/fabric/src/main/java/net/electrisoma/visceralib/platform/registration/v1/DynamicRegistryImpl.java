package net.electrisoma.visceralib.platform.registration.v1;

import com.google.auto.service.AutoService;
import com.mojang.serialization.Codec;
import net.electrisoma.visceralib.platform.registration.v1.services.IDynamicRegistryHelper;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

@AutoService(IDynamicRegistryHelper.class)
public class DynamicRegistryImpl implements IDynamicRegistryHelper {

    @Override
    public <T> void registerDataPackRegistry(
            ResourceKey<Registry<T>> key,
            Codec<T> codec,
            @Nullable Codec<T> networkCodec
    ) {
        if (networkCodec != null) {
            DynamicRegistries.registerSynced(key, codec, networkCodec);
        } else {
            DynamicRegistries.register(key, codec);
        }
    }

    @Override
    public void onNewDatapackRegistry(Object event) {}
}