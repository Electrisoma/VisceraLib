package net.electrisoma.visceralib.api.registration.registry.test.stuff;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;

import java.util.UUID;

public record TestComponent(UUID id) {
    public static final Codec<TestComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    UUIDUtil.CODEC.fieldOf("id").forGetter(TestComponent::id)
            ).apply(instance, TestComponent::new)
    );
}
