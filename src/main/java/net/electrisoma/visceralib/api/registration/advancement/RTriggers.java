package net.electrisoma.visceralib.api.registration.advancement;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.List;
import java.util.LinkedList;


public class RTriggers {
    private static final List<RCriterionTrigger<?>> triggers = new LinkedList<>();
    public static void register() {
        triggers.forEach(trigger -> Registry.register(BuiltInRegistries.TRIGGER_TYPES, trigger.getId(), trigger));
    }

    public static RTrigger addTrigger(String id) {
        return add(new RTrigger(id));
    }
    private static <T extends RCriterionTrigger<?>> T add(T instance) {
        triggers.add(instance);
        return instance;
    }
}