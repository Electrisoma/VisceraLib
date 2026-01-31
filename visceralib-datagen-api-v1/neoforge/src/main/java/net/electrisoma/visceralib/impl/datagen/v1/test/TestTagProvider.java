package net.electrisoma.visceralib.impl.datagen.v1.test;

import net.electrisoma.visceralib.api.datagen.v1.providers.VisceralTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
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
