package net.electrisoma.visceralib.impl.registration.v1.test.helper.builders;

import net.electrisoma.visceralib.api.registration.v1.registry.AbstractRegistrationHelper;
import net.electrisoma.visceralib.api.registration.v1.registry.register.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class BlockEntityBuilder<T extends BlockEntity, H extends AbstractRegistrationHelper<?>> {

    private final H helper;
    private final String name;
    private final BlockEntityType.BlockEntitySupplier<? extends T> factory;
    private final Set<Supplier<? extends Block>> validBlocks = new HashSet<>();

    public BlockEntityBuilder(H helper, String name, BlockEntityType.BlockEntitySupplier<? extends T> factory) {
        this.helper = helper;
        this.name = name;
        this.factory = factory;
    }

    public BlockEntityBuilder<T, H> validBlock(Supplier<? extends Block> block) {
        this.validBlocks.add(block);
        return this;
    }

    @SafeVarargs
    public final BlockEntityBuilder<T, H> validBlocks(Supplier<? extends Block>... blocks) {
        this.validBlocks.addAll(Arrays.asList(blocks));
        return this;
    }

    public RegistryObject<BlockEntityType<T>> register() {
        return helper.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, name, () -> {
            Block[] blockArray = validBlocks.stream()
                    .map(Supplier::get)
                    .toArray(Block[]::new);

            return BlockEntityType.Builder.<T>of(factory, blockArray).build(null);
        });
    }
}