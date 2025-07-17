package net.electrisoma.visceralib.data.providers;

import dev.architectury.injectables.annotations.ExpectPlatform;

import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;

public class VisceralBlockstateProvider {
    @ExpectPlatform
    public static void registerStatesAndModels(String modId, PackOutput output) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerStatesAndModels(String modId, PackOutput output, Object fileHelper) {
        throw new AssertionError();
    }

    public static void generateBlockStates(
            String modId,
            BiConsumer<Block, String> simpleBlock,
            BiConsumer<Block, String> fluidBlock) {
        for (var deferred : VisceralRegistries.getAllForMod(modId))
            if (deferred.getRegistryKey().equals(Registries.BLOCK)) {
                VisceralDeferredRegister<Block> blockRegister = (VisceralDeferredRegister<Block>) deferred;

                blockRegister.getEntries().forEach((id, blockSupplier) -> {
                    Block block = blockSupplier.get();
                    ResourceKey<Block> key = blockSupplier.getKey();
                    String blockName = key.location().getPath();

                    if (isFluidBlock(block)) {
                        fluidBlock.accept(block, blockName);
                    } else {
                        simpleBlock.accept(block, blockName);
                    }
                });
            }
    }

    private static boolean isFluidBlock(Block block) {
        String className = block.getClass().getSimpleName();
        return className.contains("LiquidBlock") || className.contains("Fluid");
    }
}
