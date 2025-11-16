package net.electrisoma.visceralib.data.providers.neoforge;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.builders.ItemBuilder;
import net.electrisoma.visceralib.data.providers.VisceralItemModelProvider;
import net.electrisoma.visceralib.data.util.VisceralAssetLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

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
                VisceralAssetLookup.spawnEggModel(this::registerSpawnEggModel)
        );
    }

    private void registerSpawnEggModel(Item item, ResourceLocation modelPath) {
        withExistingParent(itemName(item), mcLoc("item/template_spawn_egg"));
    }

    private void registerBlockItemModel(Item item, ResourceLocation modelPath) {
        Optional<ResourceLocation> customParent = VisceralItemModelProvider.getCustomParentModel(modId, item);
        if (customParent.isPresent()) {
            withExistingParent(itemName(item), customParent.get());
            return;
        }

        String basePath = modelPath.getPath();
        String blockItemPath = basePath + "/item";

        try {
            ModelFile itemModel = getExistingFile(modLoc(blockItemPath));
            getBuilder(itemName(item)).parent(itemModel);
        } catch (IllegalStateException e) {
            getBuilder(itemName(item)).parent(getExistingFile(modLoc(basePath)));
        }
    }

    private void registerGeneratedItemModel(Item item, ResourceLocation texturePath) {
        Optional<ResourceLocation> customParent = VisceralItemModelProvider.getCustomParentModel(modId, item);
        if (customParent.isPresent()) {
            withExistingParent(itemName(item), customParent.get());
            return;
        }

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

    @Override @NotNull
    public String getName() {
        return modId + " Item Models";
    }
}
