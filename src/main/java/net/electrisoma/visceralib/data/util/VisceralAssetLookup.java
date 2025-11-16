package net.electrisoma.visceralib.data.util;

import net.electrisoma.visceralib.VisceraLib;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import java.util.function.BiConsumer;

public class VisceralAssetLookup {
    public static BiConsumer<Item, String> simpleBlockItemModel(BiConsumer<Item, ResourceLocation> consumer, String modId) {
        return (item, name) -> consumer.accept(item, VisceraLib.path(modId, "block/" + name));
    }

    public static BiConsumer<Item, String> generatedItemModel(BiConsumer<Item, ResourceLocation> consumer, String modId) {
        return (item, name) -> consumer.accept(item, VisceraLib.path(modId, "item/" + name));
    }

    public static BiConsumer<Item, String> itemModelWithSuffix(BiConsumer<Item, ResourceLocation> consumer, String modId, String suffix) {
        return (item, name) -> consumer.accept(item, VisceraLib.path(modId, "item/" + name + suffix));
    }

    public static BiConsumer<Item, String> spawnEggModel(BiConsumer<Item, ResourceLocation> consumer) {
        return (item, name) -> consumer.accept(item, VisceraLib.path("minecraft", "item/template_spawn_egg"));
    }
}
