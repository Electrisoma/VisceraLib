package net.electrisoma.visceralib.platform.registration.v1.services;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;
import net.electrisoma.visceralib.api.registration.v1.registry.register.custom.VisceralRegistrySettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface INewRegistryHelper {

    INewRegistryHelper INSTANCE = ServiceHelper.load(INewRegistryHelper.class);

    <T> Registry<T> createCustomRegistry(ResourceKey<Registry<T>> key, VisceralRegistrySettings settings);

    void onNewRegistry(Object event);
}