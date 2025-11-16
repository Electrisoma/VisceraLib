package net.electrisoma.visceralib.data.providers;

import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistries;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.builders.ItemBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import java.util.Optional;
import java.util.function.BiConsumer;

public class VisceralItemModelProvider {
    public static Optional<ResourceLocation> getCustomParentModel(String modId, Item item) {
        return ItemBuilder.getAllBuilders().stream()
                .filter(builder -> builder.getRegisteredSupplier().isPresent()
                        && builder.getRegisteredSupplier().get().get() == item
                        && item.asItem().asItem() == builder.getRegisteredSupplier().get().get())
                .filter(builder -> {
                    ResourceLocation id = BuiltInRegistries.ITEM.getKey(builder.getRegisteredSupplier().get().get());
                    return id.getNamespace().equals(modId);
                })
                .findFirst()
                .flatMap(ItemBuilder::getCustomParentModel);
    }

    public static void generateItemModels(
            String modId,
            BiConsumer<Item, String> blockItemConsumer,
            BiConsumer<Item, String> generatedItemConsumer,
            BiConsumer<Item, String> spawnEggConsumer) {
        for (var deferred : VisceralRegistries.getAllForMod(modId)) {
            if (deferred.getRegistryKey().equals(Registries.ITEM)) {
                @SuppressWarnings("unchecked")
                VisceralDeferredRegister<Item> itemRegister = (VisceralDeferredRegister<Item>) deferred;

                for (VisceralRegistrySupplier<? extends Item> supplier : itemRegister.getEntries()) {
                    Item item = supplier.get();
                    String itemName = supplier.getKey().location().getPath();

                    if (item instanceof BlockItem) {
                        blockItemConsumer.accept(item, itemName);
                    } else if (item instanceof SpawnEggItem) {
                        spawnEggConsumer.accept(item, itemName);
                    } else {
                        generatedItemConsumer.accept(item, itemName);
                    }
                }
            }
        }
    }
}
