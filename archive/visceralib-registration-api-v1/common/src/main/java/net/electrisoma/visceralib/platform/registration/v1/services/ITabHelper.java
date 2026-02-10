package net.electrisoma.visceralib.platform.registration.v1.services;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Consumer;

public interface ITabHelper {

    ITabHelper INSTANCE = ServiceHelper.load(ITabHelper.class);

    CreativeModeTab create(Consumer<CreativeModeTab.Builder> builderConfig);
}