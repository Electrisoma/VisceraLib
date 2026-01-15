package net.electrisoma.visceralib.impl.datagen.test;

import net.electrisoma.visceralib.api.datagen.providers.VisceralTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class TestTagProvider {

    public static class TestItemTagProvider extends VisceralTagProvider.ItemTagProvider {

        public TestItemTagProvider(
                PackOutput output,
                CompletableFuture<HolderLookup.Provider> lookupProvider
        ) {
            super(output, lookupProvider, "visceralib");
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider lookup) {
//            add(ItemTags.PIGLIN_LOVED,
//                    ModItems.BEST_ITEM.get()
//            );
        }
    }
}
