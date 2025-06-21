package net.electrisoma.resotech.datagen;

import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.datagen.providers.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ResoTech.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ResoTechDataNeoForge {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onGatherDataEvent(GatherDataEvent event) {
        if (!event.getMods().contains(ResoTech.MOD_ID))
            return;

        ExistingFileHelper helper = event.getExistingFileHelper();

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();

        boolean server = event.includeServer();
        boolean client = event.includeClient();

        if (client) {
            generator.addProvider(true, new ResoLangProvider(output));
            generator.addProvider(true, new ResoBlockstateProvider(output, helper));
            generator.addProvider(true, new ResoItemModelProvider(output, helper));
        }

        if (server) {
            generator.addProvider(true, new ResoTagProvider.BlockTags(output, registries, helper));
            generator.addProvider(true, new ResoTagProvider.ItemTags(output, registries, helper));
            generator.addProvider(true, new ResoTagProvider.FluidTags(output, registries, helper));
            generator.addProvider(true, new ResoLootTableProvider(output, registries));
            generator.addProvider(true, new ResoAdvancementProvider(output, registries));
        }
    }

}
