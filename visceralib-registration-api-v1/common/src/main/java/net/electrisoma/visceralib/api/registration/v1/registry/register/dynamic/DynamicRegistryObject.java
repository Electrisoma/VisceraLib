package net.electrisoma.visceralib.api.registration.v1.registry.register.dynamic;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;

import java.util.Optional;

public record DynamicRegistryObject<T>(ResourceKey<Registry<T>> key) {

	public Optional<Registry<T>> getRegistry(LevelReader level) {
		return level.registryAccess().registry(key);
	}

	public Optional<T> get(LevelReader level, ResourceLocation id) {
		return getRegistry(level).map(reg -> reg.get(id));
	}

	public Optional<T> get(LevelReader level, ResourceKey<T> entryKey) {
		return getRegistry(level).flatMap(reg -> reg.getOptional(entryKey));
	}

	public boolean exists(LevelReader level, ResourceLocation id) {
		return getRegistry(level).map(reg -> reg.containsKey(id)).orElse(false);
	}

	public Optional<Holder<T>> getHolder(LevelReader level, ResourceKey<T> entryKey) {
		return getRegistry(level).flatMap(reg -> reg.getHolder(entryKey));
	}
}
