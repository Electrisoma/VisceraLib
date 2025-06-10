package net.electrisoma.resotech.providers;

import net.electrisoma.resotech.ResoTech;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class ItemModelGen extends ItemModelProvider {
    public ItemModelGen(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        BuiltInRegistries.ITEM.entrySet().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(ResoTech.MOD_ID))
                .forEach(entry -> {
                    ItemLike item = entry.getValue();

                    if (item instanceof BlockItem) blockItem(() -> item);
                    else generated(() -> item);
                });
    }

    public String modid(Supplier<? extends ItemLike> item) {
        return BuiltInRegistries.ITEM.getKey(item.get().asItem()).getNamespace();
    }

    public String name(Supplier<? extends ItemLike> item) {
        return BuiltInRegistries.ITEM.getKey(item.get().asItem()).getPath();
    }

    public ResourceLocation itemTexture(Supplier<? extends ItemLike> item) {
        return modLoc("item/" + name(item));
    }

    public ItemModelBuilder blockItem(Supplier<? extends ItemLike> block) {
        return blockItem(block, "");
    }
    public ItemModelBuilder blockItem(Supplier<? extends ItemLike> block, String suffix) {
        return withExistingParent(name(block), modLoc("block/" + name(block) + suffix));
    }

    public ItemModelBuilder blockWithInventoryModel(Supplier<? extends ItemLike> block) {
        return withExistingParent(name(block), modLoc("block/" + name(block) + "_inventory"));
    }

    public ItemModelBuilder blockSprite(Supplier<? extends ItemLike> block) {
        return blockSprite(block, modLoc("block/" + name(block)));
    }
    public ItemModelBuilder blockSprite(Supplier<? extends ItemLike> block, ResourceLocation texture) {
        return generated(block, texture);
    }

    public ItemModelBuilder generated(Supplier<? extends ItemLike> item) {
        return generated(item, itemTexture(item));
    }
    public ItemModelBuilder generated(Supplier<? extends ItemLike> item, ResourceLocation... layers) {
        ItemModelBuilder builder = getBuilder(name(item)).parent(new ModelFile.UncheckedModelFile("item/generated"));
        for (int i = 0; i < layers.length; i++) {
            builder.texture("layer" + i, layers[i]);
        }
        return builder;
    }

    public ItemModelBuilder handheld(Supplier<? extends ItemLike> item) {
        return handheld(item, itemTexture(item));
    }
    public ItemModelBuilder handheld(Supplier<? extends ItemLike> item, ResourceLocation texture) {
        return withExistingParent(name(item), "item/handheld").texture("layer0", texture);
    }

    @Override
    public String getName() {
        return ResoTech.MOD_ID + " Item Models";
    }
}
