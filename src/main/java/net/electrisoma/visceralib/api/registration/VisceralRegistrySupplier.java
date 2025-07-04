package net.electrisoma.visceralib.api.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceKey;

public class VisceralRegistrySupplier<T> {
    private final Supplier<T> supplier;
    private final ResourceKey<T> key;
    private final List<Consumer<T>> listeners = new ArrayList<>();
    private boolean notified = false;

    public VisceralRegistrySupplier(ResourceKey<T> key, Supplier<T> supplier) {
        this.key = key;
        this.supplier = supplier;
    }

    public T get() {
        return supplier.get();
    }

    public ResourceKey<T> getKey() {
        return key;
    }

    public void listen(Consumer<T> consumer) {
        if (notified)
            consumer.accept(get());
        else listeners.add(consumer);
    }

    public void notifyListeners() {
        if (notified) return;
        T value = get();
        for (Consumer<T> consumer : listeners)
            consumer.accept(value);
        notified = true;
        listeners.clear();
    }
}