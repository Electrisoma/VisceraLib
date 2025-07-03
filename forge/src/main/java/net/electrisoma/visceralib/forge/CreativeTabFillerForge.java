package net.electrisoma.visceralib.forge;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.helpers.CreativeTabBuilderRegistry;
import net.electrisoma.visceralib.api.registration.helpers.ICreativeTabOutputs;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VisceraLib.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeTabFillerForge {
    @SubscribeEvent
    public static void onBuildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> tabKey = event.getTabKey();

        for (ICreativeTabOutputs builder : CreativeTabBuilderRegistry.getAllBuilders()) {
            if (builder.getTabs().contains(tabKey))
                for (ItemStack stack : builder.getTabContents())
                    event.accept(stack);
        }
    }
}
