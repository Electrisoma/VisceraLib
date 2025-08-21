package net.electrisoma.visceralib.api.registration;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import com.google.common.collect.Multimaps;

import net.electrisoma.visceralib.api.registration.builders.BlockBuilder;
import net.electrisoma.visceralib.api.registration.builders.ItemBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.function.Function;
import java.util.function.Supplier;

public class VisceralRegistry {
    public final String modId;

    private static final Multimap<Registry<?>, Registration<?, ?>> REGISTRATIONS = HashMultimap.create();
    public static final Multimap<Registry<?>, Registration<?, ?>> REGISTRATIONS_VIEW = Multimaps.unmodifiableMultimap(REGISTRATIONS);

    public VisceralRegistry(String modId) {
        this.modId = modId;
    }

    @Internal
    public static <R, S extends R> void addToRegistrationMap(Registry<R> registry, Registration<R, S> registration) {
        REGISTRATIONS.put(registry, registration);
    }

    public <R extends Block> BlockBuilder<R> block(String id, Function<BlockBehaviour.Properties, R> func) {
        return new BlockBuilder<>(this, id, func);
    }

    public <T extends Item> ItemBuilder<T> item(String id, Supplier<T> supplier) {
        return new ItemBuilder<>(this, id, supplier);
    }
}