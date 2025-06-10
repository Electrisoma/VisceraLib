package net.electrisoma.resotech;

import net.electrisoma.resotech.providers.*;
import net.electrisoma.resotech.registry.ResoTechAdvancements;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

import static net.electrisoma.resotech.ResoTech.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ResoTechData {
    private ResoTechData() {}

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        boolean includeServer = event.includeServer();
        boolean includeClient = event.includeClient();

        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        gen.addProvider(includeClient, new LangProvider(output, MOD_ID));
        if (includeServer) {
            tagGen(event, output, lookupProvider, MOD_ID, existingFileHelper);
            gen.addProvider(true, new ResoTechAdvancements(output, lookupProvider));
            gen.addProvider(true, new BlockStateGen(output, MOD_ID, existingFileHelper));
            gen.addProvider(true, new ItemModelGen(output, MOD_ID, existingFileHelper));
            gen.addProvider(true, new LootTableGen(output, lookupProvider));
        }
    }

    private static void tagGen(GatherDataEvent event, PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modid, ExistingFileHelper helper) {
        if (event.includeServer()) {
            event.getGenerator().addProvider(true, new TagGen.BlockTagGen(output, lookupProvider, modid, helper));
            event.getGenerator().addProvider(true, new TagGen.ItemTagGen(output, lookupProvider, modid, helper));
            event.getGenerator().addProvider(true, new TagGen.FluidTagGen(output, lookupProvider, modid, helper));
        }
    }
}
