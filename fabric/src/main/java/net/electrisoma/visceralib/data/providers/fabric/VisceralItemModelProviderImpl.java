package net.electrisoma.visceralib.data.providers.fabric;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.data.providers.VisceralItemModelProvider;
import net.electrisoma.visceralib.data.util.ThrowingBiConsumer;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
            writeJson(new ModelJson("block/" + modelPath.getPath()), "models/item/" + modelPath.getPath() + ".json");
        }

        private void writeGeneratedModelJson(Item item, ResourceLocation texturePath) throws IOException {
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
