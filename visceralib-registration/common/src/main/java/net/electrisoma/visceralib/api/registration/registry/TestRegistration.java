package net.electrisoma.visceralib.api.registration.registry;

import net.electrisoma.visceralib.api.registration.registry.holder.BlockHolder;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class TestRegistration {

    public static VisceralRegistry REGISTRY = new VisceralRegistry("visceralib");

    public static BlockHolder<RedstoneLampBlock> BLOCK = REGISTRY
            .block("test", RedstoneLampBlock::new)
            .properties(Properties::air)
            .register();

    public static void init() {}
}