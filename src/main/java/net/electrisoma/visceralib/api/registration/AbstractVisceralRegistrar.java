package net.electrisoma.visceralib.api.registration;

import com.mojang.serialization.Codec;
import net.electrisoma.visceralib.api.registration.builders.*;
import net.electrisoma.visceralib.api.registration.entry.TabEntry;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/*? >=1.21.1 {*/
/*import net.minecraft.network.codec.StreamCodec;
*//*?}*/

public abstract class AbstractVisceralRegistrar<T extends AbstractVisceralRegistrar<T>> {
    protected final String modId;
    private TabEntry<CreativeModeTab> defaultTabEntry = null;
    private Supplier<TabEntry<CreativeModeTab>> defaultTabEntrySupplier = null;

    protected AbstractVisceralRegistrar(String modId) {
        this.modId = modId;
    }

    public <R> VisceralDeferredRegister<R> deferredRegister(
            ResourceKey<? extends Registry<R>> registryKey) {
        return VisceralRegistries.getOrCreate(registryKey, modId);
    }

    public T defaultCreativeTab(TabEntry<CreativeModeTab> tabEntry) {
        this.defaultTabEntry = tabEntry;
        this.defaultTabEntrySupplier = null;
        return self();
    }

    public T defaultCreativeTab(Supplier<TabEntry<CreativeModeTab>> tabEntrySupplier) {
        this.defaultTabEntrySupplier = tabEntrySupplier;
        this.defaultTabEntry = null;
        return self();
    }

    public Optional<TabEntry<CreativeModeTab>> getDefaultTabEntry() {
        if (defaultTabEntrySupplier != null) {
            return Optional.ofNullable(defaultTabEntrySupplier.get());
        }
        return Optional.ofNullable(defaultTabEntry);
    }

    public <I extends Item> ItemBuilder<I, T> item(
            String name, Function<Item.Properties, I> constructor) {
        return new ItemBuilder<>(self(), name, constructor);
    }

    public <B extends Block> BlockBuilder<B, T> block(
            String name, Function<BlockBehaviour.Properties, B> constructor) {
        return new BlockBuilder<>(self(), name, constructor);
    }

    public <S extends SoundEvent> SoundEventBuilder<T> sound(String name) {
        return new SoundEventBuilder<>(self(), name);
    }

    public <E extends Entity> EntityBuilder<E, T> entity(
            String name, EntityType.Builder<E> builder) {
        return new EntityBuilder<>(self(), name, builder);
    }

    public <E extends Entity> EntityBuilder<E, T> entity(
            String name,
            EntityType.EntityFactory<E> factory,
            MobCategory category
    ) {
        EntityType.Builder<E> builder = EntityType.Builder.of(factory, category);
        return new EntityBuilder<>(self(), name, builder);
    }


    public <B extends BlockEntity> BlockEntityBuilder<B, T> blockEntity(
            String name, BlockEntityType.BlockEntitySupplier<B> constructor) {
        return new BlockEntityBuilder<>(self(), name, constructor);
    }

    public FluidBuilder<T> fluid(String name) {
        return new FluidBuilder<>(self(), name);
    }

    public <P extends ParticleType<?>> ParticleBuilder<P, T> particle(
            String name, Supplier<P> supplier) {
        return new ParticleBuilder<>(self(), name, supplier);
    }

    public TabBuilder tab(String name) {
        return new TabBuilder(this, name);
    }

    /*? >=1.21.1 {*/
    /*public <D> DataComponentBuilder<D, T> dataComponent(String name, Codec<D> codec) {
        return new DataComponentBuilder<>(self(), name, codec);
    }
    public <D> DataComponentBuilder<D, T> dataComponent(String name, Codec<D> codec, StreamCodec<? super FriendlyByteBuf, D> streamCodec) {
        return new DataComponentBuilder<>(self(), name, codec, streamCodec);
    }
    *//*?}*/

    public String getModId() {
        return modId;
    }

    @SuppressWarnings("unchecked")
    protected final T self() {
        return (T) this;
    }
}