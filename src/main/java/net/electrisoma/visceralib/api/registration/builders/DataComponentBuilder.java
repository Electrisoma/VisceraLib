package net.electrisoma.visceralib.api.registration.builders;

/*? >=1.21.1 {*/
/*import com.mojang.serialization.Codec;
import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.entry.DataComponentEntry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;

public class DataComponentBuilder<T, R extends AbstractVisceralRegistrar<R>>
        extends AbstractBuilder<DataComponentType<T>, R, DataComponentBuilder<T, R>> {

    private final VisceralDeferredRegister<DataComponentType<?>> register;
    private VisceralRegistrySupplier<DataComponentType<T>> registeredSupplier;

    private final Codec<T> codec;
    private StreamCodec<? super FriendlyByteBuf, T> streamCodec;
    private boolean networkSynced;

    public DataComponentBuilder(R registrar, String name, Codec<T> codec) {
        this(registrar, name, codec, null);
    }

    public DataComponentBuilder(R registrar, String name, Codec<T> codec, StreamCodec<? super FriendlyByteBuf, T> streamCodec) {
        super(registrar, name);
        this.codec = codec;
        this.streamCodec = streamCodec;
        this.register = registrar.deferredRegister(Registries.DATA_COMPONENT_TYPE);
        this.networkSynced = streamCodec != null;
    }

    public DataComponentBuilder<T, R> networkSynchronized(StreamCodec<? super FriendlyByteBuf, T> streamCodec) {
        this.streamCodec = streamCodec;
        this.networkSynced = true;
        return this;
    }

    @SuppressWarnings("unchecked")
    public DataComponentEntry<T> register() {
        var componentTypeBuilder = DataComponentType.<T>builder()
                .persistent(codec);

        if (networkSynced && streamCodec != null) {
            componentTypeBuilder.networkSynchronized(streamCodec);
        }

        VisceralRegistrySupplier<DataComponentType<?>> raw = register.register(name, componentTypeBuilder::build);

        VisceralRegistrySupplier<DataComponentType<T>> typed = new VisceralRegistrySupplier<>(
                (ResourceKey<DataComponentType<T>>) (ResourceKey<?>) raw.getKey(),
                () -> (DataComponentType<T>) raw.get()
        );

        typed.listen(type -> postRegisterTasks.forEach(task -> task.accept(type)));
        this.registeredSupplier = typed;

        return new DataComponentEntry<>(typed);
    }

    @Override
    public Optional<VisceralRegistrySupplier<DataComponentType<T>>> getRegisteredSupplier() {
        return Optional.ofNullable(registeredSupplier);
    }
}
*//*?}*/
