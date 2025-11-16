package net.electrisoma.visceralib.data.providers.fabric;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.builders.ItemBuilder;
import net.electrisoma.visceralib.data.providers.VisceralItemModelProvider;
import net.electrisoma.visceralib.data.util.ThrowingBiConsumer;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;

public class VisceralItemModelProviderImpl {
    public static void registerStatesAndModels(String modId, PackOutput output) {
        new FabricItemModelGenerator(modId, output).run();
    }

    private record FabricItemModelGenerator(String modId, PackOutput output) {
        public void run() {
            VisceralItemModelProvider.generateItemModels(
                    modId,
                    (item, name) -> ThrowingBiConsumer.unchecked(this::writeBlockModelJson)
                            .accept(item, VisceraLib.path(modId, name)),
                    (item, name) -> ThrowingBiConsumer.unchecked(this::writeGeneratedModelJson)
                            .accept(item, VisceraLib.path(modId, name)),
                    (item, name) -> ThrowingBiConsumer.unchecked(this::writeSpawnEggModelJson)
                            .accept(item, VisceraLib.path("minecraft", "item/template_spawn_egg"))
            );
        }

        private void writeBlockModelJson(Item item, ResourceLocation modelPath) throws IOException {
            Optional<ResourceLocation> customParent = VisceralItemModelProvider.getCustomParentModel(modId, item);
            if (customParent.isPresent()) {
                ResourceLocation parentLoc = customParent.get();
                writeJson(new ModelJson(parentLoc.toString()), "models/item/" + modelPath.getPath() + ".json");
                return;
            }

            String basePath = modelPath.getPath();
            String itemModelPath = "models/block/" + basePath + "/item.json";
            String fallbackParent = "block/" + basePath;

            Path itemModelFile = output.getOutputFolder().resolve(itemModelPath);

            String parent;
            if (Files.exists(itemModelFile)) {
                parent = fallbackParent + "/item";
            } else {
                parent = fallbackParent;
            }

            writeJson(new ModelJson(parent), "models/item/" + basePath + ".json");
        }

        private void writeGeneratedModelJson(Item item, ResourceLocation texturePath) throws IOException {
            Optional<ResourceLocation> customParent = VisceralItemModelProvider.getCustomParentModel(modId, item);
            if (customParent.isPresent()) {
                ResourceLocation parentLoc = customParent.get();
                writeJson(new ModelJson(parentLoc.toString()), "models/item/" + texturePath.getPath() + ".json");
                return;
            }

            ModelJson model = new ModelJson("item/generated", texturePath.toString());
            writeJson(model, "models/item/" + texturePath.getPath() + ".json");
        }

        private void writeSpawnEggModelJson(Item item, ResourceLocation modelPath) throws IOException {
            ModelJson model = new ModelJson("item/template_spawn_egg");
            writeJson(model, "models/item/template_spawn_egg.json");
        }

        private void writeJson(Object data, String path) throws IOException {
            Path file = output.getOutputFolder().resolve(path);
            Files.createDirectories(file.getParent());
            try (var writer = Files.newBufferedWriter(file)) {
                VisceraLib.GSON.toJson(data, writer);
            }
        }

        private record ModelJson(String parent, String... textures) { }
    }
}
