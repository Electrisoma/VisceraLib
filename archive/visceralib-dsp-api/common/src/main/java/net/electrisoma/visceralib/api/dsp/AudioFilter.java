package net.electrisoma.visceralib.api.dsp;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import org.lwjgl.openal.EXTEfx;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class AudioFilter {

    private final ResourceLocation id;
    private final int type;
    private final Consumer<Integer> settings;
    private final Set<SoundSource> targetSources;
    private final Set<SoundSource> blacklistedSources;
    private final boolean isEffect;
    private final float wetness;
    private final float stereoWidth;
    private final boolean global;

    public AudioFilter(
            ResourceLocation id,
            int type,
            Consumer<Integer> settings,
            Set<SoundSource> targets,
            Set<SoundSource> blacklisted,
            boolean isEffect,
            float wetness,
            float stereoWidth,
            boolean global
    ) {
        this.id = id;
        this.type = type;
        this.settings = settings;
        this.targetSources = targets != null ? targets : EnumSet.allOf(SoundSource.class);
        this.blacklistedSources = blacklisted != null ? blacklisted : EnumSet.noneOf(SoundSource.class);
        this.isEffect = isEffect;
        this.wetness = wetness;
        this.stereoWidth = stereoWidth;
        this.global = global;
    }

    public ResourceLocation getId() {
        return id;
    }

    public int type() {
        return type;
    }

    public Consumer<Integer> settings() {
        return settings;
    }

    public boolean isEffect() {
        return isEffect;
    }

    public float getWetness() {
        return wetness;
    }

    public float getStereoWidth() {
        return stereoWidth;
    }

    public boolean canApplyTo(SoundSource source) {
        return targetSources.contains(source) && !blacklistedSources.contains(source);
    }

    public boolean isGlobal() { return global; }

    public static class Builder {

        private final String modId, name;
        private final int type;
        private final boolean isEffect;
        private final List<Consumer<Integer>> parameters = new ArrayList<>();
        private Set<SoundSource> whitelist = EnumSet.allOf(SoundSource.class);
        private Set<SoundSource> blacklist = EnumSet.noneOf(SoundSource.class);
        private int slot = 1000;
        private float wetness = 1.0f;
        private float stereoWidth = 1.0f;
        private BiPredicate<SoundInstance, LocalPlayer> condition = (s, p) -> true;
        private boolean global = false;

        public Builder(String modId, String name, int type, boolean isEffect) {
            this.modId = modId;
            this.name = name;
            this.type = type;
            this.isEffect = isEffect;
        }

        public Builder param(int paramKey, float value) {
            parameters.add(isEffect ? (id -> EXTEfx.alEffectf(id, paramKey, value)) : (id -> EXTEfx.alFilterf(id, paramKey, value)));
            return this;
        }

        public Builder param(int paramKey, int value) {
            parameters.add(isEffect ? (id -> EXTEfx.alEffecti(id, paramKey, value)) : (id -> EXTEfx.alFilteri(id, paramKey, value)));
            return this;
        }

        public Builder wetness(float wetness) {
            this.wetness = Math.max(0f, Math.min(1f, wetness));
            return this;
        }

        public Builder slot(int slot) {
            this.slot = slot;
            return this;
        }

        public Builder isolate(SoundSource... sources) {
            this.whitelist = EnumSet.copyOf(Arrays.asList(sources));
            return this;
        }

        public Builder deny(SoundSource... sources) {
            this.blacklist = EnumSet.copyOf(Arrays.asList(sources));
            return this;
        }

        public Builder when(BiPredicate<SoundInstance, LocalPlayer> condition) {
            this.condition = condition;
            return this;
        }

        public Builder width(float width) {
            this.stereoWidth = Math.max(0f, Math.min(1f, width));
            return this;
        }

        public Builder global() {
            this.global = true;
            return this;
        }

        public void build() {
            AudioFilterRegistry.register(this);
        }

        public boolean isEffect() { return isEffect; }
        protected String getModId() { return modId; }
        protected String getName() { return name; }
        protected int getType() { return type; }
        protected List<Consumer<Integer>> getParameters() { return parameters; }
        protected Set<SoundSource> getWhitelist() { return whitelist; }
        protected Set<SoundSource> getBlacklist() { return blacklist; }
        protected int getSlot() { return slot; }
        protected float getWetness() { return wetness; }
        protected float getStereoWidth() {
            return stereoWidth;
        }
        public boolean isGlobal() { return global; }
        protected BiPredicate<SoundInstance, LocalPlayer> getCondition() { return condition; }
    }
}