package net.electrisoma.visceralib.neoforge;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.builders.BlockBuilder;
import net.electrisoma.visceralib.api.registration.builders.ItemBuilder;
import net.electrisoma.visceralib.api.registration.builders.TabBuilder;

import net.minecraft.world.item.ItemStack;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.function.Supplier;

@EventBusSubscriber(modid = VisceraLib.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class CreativeTabFillerNeoForge {
    @SubscribeEvent
    public static void onBuildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        for (TabBuilder builder : TabBuilder.getAllBuilders()) {
            var entry = builder.getTabEntry();
            if (entry == null || !event.getTabKey().equals(entry.getKey())) continue;

            // maunal item allocation via tab
            for (Supplier<ItemStack> content : builder.getTabContents()) {
                event.accept(content.get());
            }

            // automatic item allocation
            for (ItemBuilder<?, ?> itemBuilder : ItemBuilder.getAllBuilders()) {
                if (itemBuilder.getTabs().contains(entry.getKey())) {
                    itemBuilder.getRegisteredSupplier()
                            .map(supplier -> new ItemStack(supplier.get()))
                            .ifPresent(event::accept);
                }
            }

            // automatic block allocation
            for (BlockBuilder<?, ?> blockBuilder : BlockBuilder.getAllBuilders()) {
                if (blockBuilder.getTabs().contains(entry.getKey())) {
                    blockBuilder.getRegisteredSupplier()
                            .map(supplier -> new ItemStack(supplier.get()))
                            .ifPresent(event::accept);
                }
            }
        }
    }
}
