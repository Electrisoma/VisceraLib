package net.electrisoma.visceralib.data.providers;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public abstract class VisceralibBlockstatesCommon {
    protected final String modId;

    public VisceralibBlockstatesCommon(String modId) {
        this.modId = modId;
    }

    public void registerAll() {
        for (var entry : BuiltInRegistries.BLOCK.entrySet()) {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(entry.getValue());
            if (!id.getNamespace().equals(modId)) continue;

            Block block = entry.getValue();
            String path = id.getPath();

            if (block.getClass().getSimpleName().contains("LiquidBlock") ||
                    block.getClass().getSimpleName().contains("Fluid")) {
                registerFluidBlock(block, path);
            } else {
                registerSimpleBlock(block);
            }
        }
    }

    protected abstract void registerSimpleBlock(Block block);
    protected abstract void registerFluidBlock(Block block, String blockName);
}
