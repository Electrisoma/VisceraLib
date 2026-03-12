package net.electrisoma.visceralib.platform.registration.v1;

import net.electrisoma.visceralib.platform.registration.v1.services.ITabHelper;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import com.google.auto.service.AutoService;

import java.util.function.Consumer;

@AutoService(ITabHelper.class)
public final class TabHelperImpl implements ITabHelper {

	@Override
	public CreativeModeTab create(Consumer<CreativeModeTab.Builder> builderConfig) {
		CreativeModeTab.Builder builder = CreativeModeTab.builder();
		builderConfig.accept(builder);
		return builder.build();
	}

	@Override
	public void setOrderBefore(CreativeModeTab.Builder builder, ResourceKey<CreativeModeTab> before) {
		builder.withTabsBefore(before);
	}

	@Override
	public void setOrderAfter(CreativeModeTab.Builder builder, ResourceKey<CreativeModeTab> after) {
		builder.withTabsAfter(after);
	}
}
