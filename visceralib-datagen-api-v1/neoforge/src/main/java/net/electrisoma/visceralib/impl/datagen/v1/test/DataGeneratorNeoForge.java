package net.electrisoma.visceralib.impl.datagen.v1.test;

import net.electrisoma.visceralib.impl.datagen.v1.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class DataGeneratorNeoForge {

    public static void gatherData(final GatherDataEvent event) {
        if (!event.getModContainer().getModId().contains(Constants.MOD_ID)) return;

        boolean includeServer = event.includeServer();
        boolean includeClient = event.includeClient();

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (includeClient) {
            generator.addProvider(true, new TestLangProvider(output, Constants.MOD_ID, "en_us", lookupProvider));
        }
    }
}
