package net.electrisoma.visceralib.api.registration.builders;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.entry.EntityEntry;
import net.electrisoma.visceralib.data.providers.VisceralLangProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class EntityBuilder<T extends Entity, R extends AbstractVisceralRegistrar<R>>
            extends AbstractBuilder<EntityType<T>, R, net.electrisoma.visceralib.api.registration.builders.EntityBuilder<T, R>> {

        private static final List<net.electrisoma.visceralib.api.registration.builders.EntityBuilder<?, ?>> ALL_BUILDERS = new ArrayList<>();

        private final VisceralDeferredRegister<EntityType<?>> register;
        private VisceralRegistrySupplier<EntityType<T>> registeredSupplier;

        private EntityType.Builder<T> builder;
        private String langEntry;

        public EntityBuilder(R registrar, String name, EntityType.Builder<T> builder) {
            super(registrar, name);
            this.builder = builder;
            this.register = registrar.deferredRegister(Registries.ENTITY_TYPE);
            ALL_BUILDERS.add(this);
        }

        public EntityBuilder<T, R> size(float width, float height) {
            this.builder = this.builder.sized(width, height);
            return self();
        }

        public EntityBuilder<T, R> trackingRange(int range) {
            this.builder = this.builder.clientTrackingRange(range);
            return self();
        }

        public EntityBuilder<T, R> updateInterval(int ticks) {
            this.builder = this.builder.updateInterval(ticks);
            return self();
        }

        public EntityBuilder<T, R> configure(Function<EntityType.Builder<T>, EntityType.Builder<T>> modifier) {
            this.builder = modifier.apply(this.builder);
            return self();
        }

        public EntityBuilder<T, R> lang(String lang) {
            this.langEntry = lang;
            return self();
        }

        @SuppressWarnings("unchecked")
        public EntityEntry<T> register() {
            if (creativeTabs.isEmpty())
                registrar.getDefaultTabEntry().ifPresent(entry -> creativeTabs.add(entry::getKey));

            VisceralRegistrySupplier<EntityType<?>> raw = register.register(name, () ->
                    builder.build(VisceraLib.path(registrar.getModId(), name).toString()));

            VisceralRegistrySupplier<EntityType<T>> typed = new VisceralRegistrySupplier<>(
                    (ResourceKey<EntityType<T>>) (ResourceKey<?>) raw.getKey(),
                    () -> (EntityType<T>) raw.get()
            );

            typed.listen(entityType -> postRegisterTasks.forEach(task -> task.accept(entityType)));
            this.registeredSupplier = typed;

            return new EntityEntry<>(typed);
        }

        @Override
        public Optional<VisceralRegistrySupplier<EntityType<T>>> getRegisteredSupplier() {
            return Optional.ofNullable(registeredSupplier);
        }
        public Optional<String> getLangEntry() {
            return Optional.ofNullable(langEntry);
        }
        public static List<net.electrisoma.visceralib.api.registration.builders.EntityBuilder<?, ?>> getAllBuilders() {
            return Collections.unmodifiableList(ALL_BUILDERS);
        }

        public static void provideLang(VisceralLangProvider provider) {
            for (net.electrisoma.visceralib.api.registration.builders.EntityBuilder<?, ?> builder : getAllBuilders()) {
                builder.getRegisteredSupplier().ifPresent(supplier -> {
                    EntityType<?> entityType = supplier.get();
                    var id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);

                    if (!id.getNamespace().equals(provider.getModId()))
                        return;

                    String langKey = "entity." + id.getNamespace() + "." + id.getPath();
                    String langValue = builder.getLangEntry().orElse(VisceralLangProvider.toEnglishName(id.getPath()));

                    provider.add(langKey, langValue);
                });
            }
        }

}
