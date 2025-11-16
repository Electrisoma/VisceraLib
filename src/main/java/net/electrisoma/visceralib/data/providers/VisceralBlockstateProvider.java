package net.electrisoma.visceralib.data.providers;

import dev.architectury.injectables.annotations.ExpectPlatform;

import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistries;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.builders.BlockBuilder;
import net.electrisoma.visceralib.data.util.ICustomBlockstateGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.List;
import java.util.Optional;
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
            Object provider,
            BiConsumer<Block, String> simpleBlock,
            BiConsumer<Block, String> fluidBlock) {

        for (BlockBuilder<?, ?> builder : BlockBuilder.getAllBuilders()) {
            Optional<? extends VisceralRegistrySupplier<?>> maybeSupplier = builder.getRegisteredSupplier();
            if (maybeSupplier.isEmpty())
                continue;

            Block block = (Block) maybeSupplier.get().get();
            ResourceKey<Block> key = (ResourceKey<Block>) maybeSupplier.get().getKey();
            ResourceLocation id = key.location();

            if (!id.getNamespace().equals(modId))
                continue;

            Optional<ICustomBlockstateGenerator> marker = builder.getBlockStateMarker();

            if (marker.isPresent()) {
                if (marker.get() == BlockBuilder.NO_GENERATION_MARKER) {
                    continue;
                }

                ICustomBlockstateGenerator customGenerator = marker.get();

                @SuppressWarnings("unchecked")
                VisceralRegistrySupplier<Block> typedSupplier = (VisceralRegistrySupplier<Block>) maybeSupplier.get();

                BlockBuilder.Context<Block> context = new BlockBuilder.Context<>(
                        typedSupplier.get(),
                        typedSupplier.getKey().location(),
                        provider
                );

                customGenerator.accept(context, provider);

                continue;
            }

            String blockName = id.getPath();

            if (isFluidBlock(block)) {
                fluidBlock.accept(block, blockName);
            } else {
                simpleBlock.accept(block, blockName);
            }
        }
    }

    private static boolean isFluidBlock(Block block) {
        String className = block.getClass().getSimpleName();
        return className.contains("LiquidBlock") || className.contains("Fluid");
    }
}
