package net.electrisoma.visceralib.forge;

import net.electrisoma.visceralib.VisceraLib;

import net.electrisoma.visceralib.api.forge.registration.VisceralBootstrapForge;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.builders.BlockBuilder;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(VisceraLib.MOD_ID)
public class VisceraLibImpl {
	static IEventBus eventBus;
	static IEventBus forgeBus;

	public VisceraLibImpl() {
		eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		forgeBus = MinecraftForge.EVENT_BUS;

		VisceraLib.init();

		VisceralBootstrapForge.init(eventBus);
	}

	@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) {
		for (BlockBuilder<?, ?> builder : BlockBuilder.getAllBuilders())
            builder.getRegisteredSupplier().ifPresent(VisceralRegistrySupplier::notifyListeners);
//		for (TabBuilder<?> builder : TabBuilder.getAllBuilders())
//			builder.getRegisteredSupplier().ifPresent(VisceralRegistrySupplier::notifyListeners);
	}
}
