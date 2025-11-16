package net.electrisoma.visceralib.data.providers.neoforge;

import net.electrisoma.visceralib.api.registration.builders.BlockBuilder;
import net.electrisoma.visceralib.data.providers.VisceralBlockstateProvider;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class VisceralBlockstateProviderImpl {
    public static class NeoForgeBlockStates extends BlockStateProvider {
        private final String modId;

        public NeoForgeBlockStates(String modId, PackOutput output, ExistingFileHelper fileHelper) {
            super(output, modId, fileHelper);
            this.modId = modId;
        }

        @Override
        protected void registerStatesAndModels() {
            VisceralBlockstateProvider.generateBlockStates(
                    modId,
                    this,
                    (block, name) -> simpleBlock(block),
                    (block, name) -> {
                        BlockModelBuilder model = models().getBuilder(name)
                                .texture("particle", modLoc("fluid/" + name + "_still"));
                        simpleBlock(block, model);
                    }
            );
        }

        @Override @NotNull
        public String getName() {
            return modId + " Blockstates";
        }
    }
}
