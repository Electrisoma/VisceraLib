//package net.electrisoma.visceralib.api.registration.advancement;
//
//import java.util.*;
//import java.util.function.Supplier;
//
//import com.google.common.collect.Maps;
//
//import net.electrisoma.visceralib.VisceraLib;
//import net.minecraft.MethodsReturnNonnullByDefault;
//import net.minecraft.advancements.CriterionTrigger;
//import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.PlayerAdvancements;
//import net.minecraft.server.level.ServerPlayer;
//import org.jetbrains.annotations.Nullable;
//
//
//@MethodsReturnNonnullByDefault
//public abstract class RCriterionTrigger<T extends RCriterionTrigger.Instance> implements CriterionTrigger<T> {
//    public RCriterionTrigger(String id) {
//        this.id = VisceraLib.path(id);
//    }
//
//    private final ResourceLocation id;
//    public ResourceLocation getId() {
//        return id;
//    }
//
//    protected final Map<PlayerAdvancements, Set<Listener<T>>> listeners = Maps.newHashMap();
//
//    @Override public void addPlayerListener(PlayerAdvancements playerAdvancementsIn, Listener<T> listener) {
//        Set<Listener<T>> playerListeners = this.listeners.computeIfAbsent(playerAdvancementsIn, k -> new HashSet<>());
//
//        playerListeners.add(listener);
//    }
//    @Override
//    public void removePlayerListener(PlayerAdvancements playerAdvancementsIn, Listener<T> listener) {
//        Set<Listener<T>> playerListeners = this.listeners.get(playerAdvancementsIn);
//        if (playerListeners != null) {
//            playerListeners.remove(listener);
//            if (playerListeners.isEmpty()) {
//                this.listeners.remove(playerAdvancementsIn);
//            }
//        }
//    }
//    @Override
//    public void removePlayerListeners(PlayerAdvancements playerAdvancementsIn) {
//        this.listeners.remove(playerAdvancementsIn);
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    protected void trigger(ServerPlayer player, @Nullable List<Supplier<Object>> suppliers) {
//        PlayerAdvancements playerAdvancements = player.getAdvancements();
//        Set<Listener<T>> playerListeners = this.listeners.get(playerAdvancements);
//        if (playerListeners != null) {
//            List<Listener<T>> list = new LinkedList<>();
//
//            for (Listener<T> listener : playerListeners) {
//                if (listener.trigger().test(suppliers)) {
//                    list.add(listener);
//                }
//            }
//
//            list.forEach(listener -> listener.run(playerAdvancements));
//
//        }
//    }
//
//    public abstract static class Instance implements SimpleCriterionTrigger.SimpleInstance {
//        protected abstract boolean test(@Nullable List<Supplier<Object>> suppliers);
//    }
//}