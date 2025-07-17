package net.electrisoma.visceralib.data.providers.fabric;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.data.providers.VisceralBlockstateProvider;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;

public class VisceralBlockstateProviderImpl {
    public static void registerStatesAndModels(String modId, PackOutput output) {
        new FabricBlockstateGenerator(modId, output).run();
    }

    private record FabricBlockstateGenerator(String modId, PackOutput output) {
        public void run() {
            VisceralBlockstateProvider.generateBlockStates(
                    modId,
                    (block, name) -> {
                        try {
                            generateSimpleBlockstate(block, name);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    (block, name) -> {
                        try {
                            generateFluidBlockstate(block, name);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        }

        private void generateSimpleBlockstate(Block block, String name) throws IOException {
            Object blockstateJson = createSimpleBlockstateJson(name);
            writeJson(blockstateJson, "blockstates/" + name + ".json");

            Object modelJson = createSimpleModelJson(name);
            writeJson(modelJson, "models/block/" + name + ".json");
        }

        private void generateFluidBlockstate(Block block, String name) throws IOException {
            Object blockstateJson = createSimpleBlockstateJson(name);
            writeJson(blockstateJson, "blockstates/" + name + ".json");

            Object modelJson = createFluidModelJson(name);
            writeJson(modelJson, "models/block/" + name + ".json");
        }

        private Object createSimpleBlockstateJson(String blockName) {
            return new Object() {
                final String variants = "{}";
            };
        }

        private Object createSimpleModelJson(String blockName) {
            return new Object() {
                final String parent = "block/cube_all";
                final Object textures = new Object() {
                    final String all = modId + ":block/" + blockName;
                };
            };
        }

        private Object createFluidModelJson(String blockName) {
            return new Object() {
                final String parent = "block/generated";
                final Object textures = new Object() {
                    final String particle = modId + ":fluid/" + blockName + "_still";
                };
            };
        }

        private void writeJson(Object json, String relativePath) throws IOException {
            Path outputPath = output.getOutputFolder().resolve(relativePath);
            Files.createDirectories(outputPath.getParent());
            try (var writer = Files.newBufferedWriter(outputPath)) {
                VisceraLib.GSON.toJson(json, writer);
            }
        }
    }
}
