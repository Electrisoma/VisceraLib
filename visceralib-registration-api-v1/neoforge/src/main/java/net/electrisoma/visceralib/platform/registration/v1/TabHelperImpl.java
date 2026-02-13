package net.electrisoma.visceralib.platform.registration.v1;

import net.electrisoma.visceralib.platform.registration.v1.services.ITabHelper;

import net.minecraft.world.item.CreativeModeTab;

import com.google.auto.service.AutoService;

import java.util.function.Consumer;

@AutoService(ITabHelper.class)
public class TabHelperImpl implements ITabHelper {

	@Override
	public CreativeModeTab create(Consumer<CreativeModeTab.Builder> builderConfig) {
		CreativeModeTab.Builder builder = CreativeModeTab.builder();
		builderConfig.accept(builder);
		return builder.build();
	}
}
