package net.electrisoma.visceralib.data.providers.forge;

import net.electrisoma.visceralib.data.providers.VisceralBlockstateProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class VisceralBlockstateProviderImpl {
    private static class ForgeBlockStates extends BlockStateProvider {
        private final String modId;

        public ForgeBlockStates(String modId, PackOutput output, ExistingFileHelper fileHelper) {
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

        @Override
        public String getName() {
            return modId + " Blockstates";
        }
    }
}
