package net.electrisoma.visceralib.impl.datagen.v1.test;

import net.electrisoma.visceralib.api.datagen.v1.providers.client.VisceralLangProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class TestLangProvider extends VisceralLangProvider {

    public TestLangProvider(PackOutput output, String modid, String locale, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, modid, locale, lookupProvider);
    }

    @Override
    protected void generateTranslations(HolderLookup.@NotNull Provider lookup, TranslationBuilder builder) {
        builder.add(ModItems.BEST_ITEM.get(), "thing_item");
    }
}
