package net.electrisoma.visceralib.event.registration.v1.common;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public final class EntityEvents {

	private EntityEvents() {}

	@FunctionalInterface
	public interface Attributes {

		void onRegister(AttributeRegistrar registrar);
	}

	public interface AttributeRegistrar {

		void register(EntityType<? extends LivingEntity> type, AttributeSupplier.Builder builder);
	}
}
