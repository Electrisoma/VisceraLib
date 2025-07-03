package net.electrisoma.visceralib.data.providers.forge;

import net.electrisoma.visceralib.data.providers.VisceralibBlockstatesCommon;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class VisceralibBlockstateProvider extends BlockStateProvider {
    private final VisceralibBlockstatesCommon common;
    private final String modId;

    public VisceralibBlockstateProvider(PackOutput output, ExistingFileHelper fileHelper, String modId) {
        super(output, modId, fileHelper);
        this.modId = modId;

        this.common = new VisceralibBlockstatesCommon(modId) {
            @Override
            protected void registerSimpleBlock(Block block) {
                VisceralibBlockstateProvider.this.simpleBlock(block);
            }

            @Override
            protected void registerFluidBlock(Block block, String blockName) {
                BlockModelBuilder model = VisceralibBlockstateProvider.this.models().getBuilder(blockName)
                        .texture("particle", modLoc("fluid/" + blockName + "_still"));
                VisceralibBlockstateProvider.this.simpleBlock(block, model);
            }
        };
    }

    @Override
    protected void registerStatesAndModels() {
        common.registerAll();
    }
}
