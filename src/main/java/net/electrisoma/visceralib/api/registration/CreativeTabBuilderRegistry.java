package net.electrisoma.visceralib.api.registration;

import net.electrisoma.visceralib.api.registration.helpers.ICreativeTabOutputs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class CreativeTabBuilderRegistry {
    private static final List<Supplier<Collection<? extends ICreativeTabOutputs>>> PROVIDERS = new ArrayList<>();

    public static void registerBuilderProvider(Supplier<Collection<? extends ICreativeTabOutputs>> provider) {
        PROVIDERS.add(provider);
    }

    public static Collection<ICreativeTabOutputs> getAllBuilders() {
        List<ICreativeTabOutputs> all = new ArrayList<>();
        for (Supplier<Collection<? extends ICreativeTabOutputs>> provider : PROVIDERS) {
            all.addAll(provider.get());
        }
        return Collections.unmodifiableList(all);
    }
}
