package net.electrisoma.visceralib.api.registration.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;


public class RTrigger extends RCriterionTrigger<RTrigger.Instance> {
    public RTrigger(String id) {
        super(id);
    }

    public void trigger(ServerPlayer player) {
        super.trigger(player, null);
    }

    public Instance instance() {
        return new Instance();
    }

    @Override
    public Codec<Instance> codec() {
        return Instance.CODEC;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static class Instance extends RCriterionTrigger.Instance {
        private static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player)
        ).apply(instance, Instance::new));

        private final Optional<ContextAwarePredicate> player;

        public Instance() {
            player = Optional.empty();
        }

        public Instance(Optional<ContextAwarePredicate> player) {
            this.player = player;
        }

        @Override
        protected boolean test(@Nullable List<Supplier<Object>> suppliers) {
            return true;
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return player;
        }
    }
}