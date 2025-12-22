package net.electrisoma.visceralib.api.registration.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import com.google.common.collect.Multimaps;
import net.electrisoma.visceralib.api.registration.registry.builder.BlockBuilder;
import net.electrisoma.visceralib.api.registration.registry.builder.ItemBuilder;
import net.electrisoma.visceralib.api.registration.registry.holder.BaseHolder;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.function.Consumer;
import java.util.function.Function;

public class VisceralRegistry {

    public final String modId;

    private static final Multimap<Registry<?>, Registration<?, ?, ?>> REGISTRATIONS = HashMultimap.create();
    public static final Multimap<Registry<?>, Registration<?, ?, ?>> REGISTRATIONS_VIEW = Multimaps.unmodifiableMultimap(REGISTRATIONS);

    public VisceralRegistry(String modId) {
        this.modId = modId;
    }

    @Internal
    public static <R, T extends R, H extends BaseHolder<T>> void addToRegistrationMap(Registry<R> registry, Registration<R, T, H> registration) {
        REGISTRATIONS.put(registry, registration);
    }

    public <R extends Block> BlockBuilder<R> block(String id, Function<BlockBehaviour.Properties, R> factory) {
        return new BlockBuilder<>(this, id, factory);
    }

    public <R extends Item> ItemBuilder<R> item(String id, Function<Item.Properties, R> factory) {
        return new ItemBuilder<>(this, id, factory);
    }

//    public <P extends ParticleType<?>> ParticleTypeBuilder<P> particle(
//            String id,
//            Supplier<P> factory
//    ) {
//        return new ParticleTypeBuilder<>(this, id, factory);
//    }
}