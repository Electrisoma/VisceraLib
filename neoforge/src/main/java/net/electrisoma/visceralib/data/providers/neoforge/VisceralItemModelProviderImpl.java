package net.electrisoma.visceralib.data.providers.neoforge;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.data.providers.VisceralItemModelProvider;
import net.electrisoma.visceralib.data.util.VisceralAssetLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class VisceralItemModelProviderImpl extends ItemModelProvider {
    private final String modId;

    public VisceralItemModelProviderImpl(String modId, PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, modId, existingFileHelper);
        this.modId = modId;
    }

    @Override
    protected void registerModels() {
        VisceralItemModelProvider.generateItemModels(
                modId,
                VisceralAssetLookup.simpleBlockItemModel(this::registerBlockItemModel, modId),
                VisceralAssetLookup.generatedItemModel(this::registerGeneratedItemModel, modId),
                VisceralAssetLookup.spawnEggModel(this::registerSpawnEggModel, modId)
        );
    }

    private void registerSpawnEggModel(Item item, ResourceLocation modelPath) {
        withExistingParent(itemName(item), mcLoc("item/template_spawn_egg"));
    }

    private void registerBlockItemModel(Item item, ResourceLocation modelPath) {
        withExistingParent(itemName(item), modLoc(modelPath.getPath()));
    }

    private void registerGeneratedItemModel(Item item, ResourceLocation texturePath) {
        if ("minecraft".equals(texturePath.getNamespace()) && "item/template_spawn_egg".equals(texturePath.getPath())) {
            withExistingParent(itemName(item), VisceraLib.path("minecraft", "item/template_spawn_egg"));
        } else {
            try {
                ModelFile parentModel = getExistingFile(modLoc(texturePath.getPath() + "/item"));
                getBuilder(itemName(item)).parent(parentModel);
            } catch (IllegalStateException e) {
                getBuilder(itemName(item))
                        .parent(getExistingFile(mcLoc("item/generated")))
                        .texture("layer0", modLoc(texturePath.getPath()));
            }
        }
    }

    private String itemName(Item item) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
        return key.getPath();
    }

    @Override
    public String getName() {
        return modId + " Item Models";
    }
}
