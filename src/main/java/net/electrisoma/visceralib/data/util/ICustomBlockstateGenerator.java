package net.electrisoma.visceralib.data.util;

import net.electrisoma.visceralib.api.registration.builders.BlockBuilder;

@FunctionalInterface
public interface ICustomBlockstateGenerator {
    void accept(BlockBuilder.Context<?> context, Object provider);
}
