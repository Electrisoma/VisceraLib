package net.electrisoma.visceralib.data.providers;

import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistries;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.BiConsumer;

public class VisceralItemModelProvider {
    public static void generateItemModels(String modId, BiConsumer<Item, String> blockItemConsumer, BiConsumer<Item, String> generatedItemConsumer) {
        for (var deferred : VisceralRegistries.getAllForMod(modId)) {
            if (deferred.getRegistryKey().equals(Registries.ITEM)) {
                @SuppressWarnings("unchecked")
                VisceralDeferredRegister<Item> itemRegister = (VisceralDeferredRegister<Item>) deferred;

                for (VisceralRegistrySupplier<Item> supplier : itemRegister.getEntries().values()) {
                    Item item = supplier.get();
                    String itemName = supplier.getKey().location().getPath();

                    if (item instanceof BlockItem) {
                        blockItemConsumer.accept(item, itemName);
                    } else {
                        generatedItemConsumer.accept(item, itemName);
                    }
                }
            }
        }
    }
}
