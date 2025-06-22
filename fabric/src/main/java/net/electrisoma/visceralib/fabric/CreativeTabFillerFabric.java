package net.electrisoma.visceralib.fabric;

import net.electrisoma.visceralib.api.registration.builders.BlockBuilder;
import net.electrisoma.visceralib.api.registration.builders.ItemBuilder;
import net.electrisoma.visceralib.api.registration.builders.TabBuilder;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;
import java.util.function.Supplier;

public class CreativeTabFillerFabric {

    public static void register() {
        for (TabBuilder builder : TabBuilder.getAllBuilders()) {
            var tabEntry = builder.getTabEntry();
            if (tabEntry == null) continue;

            ResourceKey<CreativeModeTab> tabKey = tabEntry.getKey();
            CreativeModeTab tab = BuiltInRegistries.CREATIVE_MODE_TAB.get(tabKey);

            ItemGroupEvents.modifyEntriesEvent(tabKey).register(entries -> {
                // Add manual content items
                for (Supplier<ItemStack> content : builder.getTabContents()) {
                    entries.accept(content.get());
                    System.out.println("Added manual content: " + content.get());
                }

                // Add items from item builders if their tabs match
                for (ItemBuilder<?, ?> itemBuilder : ItemBuilder.getAllBuilders()) {
                    boolean matches = itemBuilder.getTabs().stream()
                            .anyMatch(key -> key.equals(tabKey));

                    if (matches) {
                        itemBuilder.getRegisteredSupplier()
                                .map(supplier -> new ItemStack(supplier.get()))
                                .ifPresent(entries::accept);
                    }
                }

                // Add blocks from block builders if their tabs match
                for (BlockBuilder<?, ?> blockBuilder : BlockBuilder.getAllBuilders()) {
                    boolean matches = blockBuilder.getTabs().stream()
                            .anyMatch(key -> key.equals(tabKey));

                    if (matches) {
                        blockBuilder.getRegisteredSupplier()
                                .map(supplier -> new ItemStack(supplier.get()))
                                .ifPresent(entries::accept);
                    }
                }
            });
        }
    }

    /**
     * Helper method to safely get the ResourceKey for a CreativeModeTab.
     * Returns null if the key cannot be found.
     */
    private ResourceKey<CreativeModeTab> getTabKey(CreativeModeTab tab) {
        return BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab).orElse(null);
    }
}
