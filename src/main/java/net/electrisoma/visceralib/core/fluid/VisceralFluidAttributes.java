package net.electrisoma.visceralib.core.fluid;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class VisceralFluidAttributes {
    private final Supplier<VisceralFlowingFluid> stillFluid;
    private final Supplier<VisceralFlowingFluid> flowingFluid;
    private final Supplier<?> block;
    private final Supplier<?> bucket;

    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;

    private final int color;
    private final int luminosity;
    private final int density;
    private final int temperature;
    private final int viscosity;
    private final float slopeFindDistance;
    private final float dropOff;
    private final int tickDelay;
    private final float explosionResistance;
    private final boolean lighterThanAir;
    private final boolean convertToSource;

    protected VisceralFluidAttributes(Builder builder) {
        this.stillFluid = builder.stillFluid;
        this.flowingFluid = builder.flowingFluid;
        this.block = builder.block;
        this.bucket = builder.bucket;
        this.stillTexture = builder.stillTexture;
        this.flowingTexture = builder.flowingTexture;
        this.color = builder.color;
        this.luminosity = builder.luminosity;
        this.density = builder.density;
        this.temperature = builder.temperature;
        this.viscosity = builder.viscosity;
        this.slopeFindDistance = builder.slopeFindDistance;
        this.dropOff = builder.dropOff;
        this.tickDelay = builder.tickDelay;
        this.explosionResistance = builder.explosionResistance;
        this.lighterThanAir = builder.lighterThanAir;
        this.convertToSource = builder.convertToSource;
    }

    public Supplier<VisceralFlowingFluid> getStillFluid() {
        return stillFluid;
    }
    public Supplier<VisceralFlowingFluid> getFlowingFluid() {
        return flowingFluid;
    }
    public ResourceLocation getStillTexture() {
        return stillTexture;
    }

    public static Builder builder(Supplier<VisceralFlowingFluid> still, Supplier<VisceralFlowingFluid> flowing) {
        return new Builder(still, flowing);
    }

    public static class Builder {
        private final Supplier<VisceralFlowingFluid> stillFluid;
        private final Supplier<VisceralFlowingFluid> flowingFluid;

        private Supplier<?> block;
        private Supplier<?> bucket;

        private ResourceLocation stillTexture;
        private ResourceLocation flowingTexture;

        private int color = 0xFFFFFFFF;
        private int luminosity = 0;
        private int density = 1000;
        private int temperature = 300;
        private int viscosity = 1000;
        private float slopeFindDistance = 4.0f;
        private float dropOff = 1.0f;
        private int tickDelay = 5;
        private float explosionResistance = 100.0f;
        private boolean lighterThanAir = false;
        private boolean convertToSource = false;

        public Builder(Supplier<VisceralFlowingFluid> still, Supplier<VisceralFlowingFluid> flowing) {
            this.stillFluid = still;
            this.flowingFluid = flowing;
        }

        public Builder blockSupplier(Supplier<?> block) {
            this.block = block;
            return this;
        }

        public Builder bucketSupplier(Supplier<?> bucket) {
            this.bucket = bucket;
            return this;
        }

        public Builder textures(ResourceLocation still, ResourceLocation flow) {
            this.stillTexture = still;
            this.flowingTexture = flow;
            return this;
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder luminosity(int lum) {
            this.luminosity = lum;
            return this;
        }

        public Builder density(int d) {
            this.density = d;
            return this;
        }

        public Builder temperature(int t) {
            this.temperature = t;
            return this;
        }

        public Builder viscosity(int v) {
            this.viscosity = v;
            return this;
        }

        public Builder slopeFindDistance(float f) {
            this.slopeFindDistance = f;
            return this;
        }

        public Builder dropOff(float f) {
            this.dropOff = f;
            return this;
        }

        public Builder tickDelay(int t) {
            this.tickDelay = t;
            return this;
        }

        public Builder explosionResistance(float r) {
            this.explosionResistance = r;
            return this;
        }

        public Builder lighterThanAir(boolean b) {
            this.lighterThanAir = b;
            return this;
        }

        public Builder convertToSource(boolean b) {
            this.convertToSource = b;
            return this;
        }

        public VisceralFluidAttributes build() {
            return new VisceralFluidAttributes(this);
        }
    }
}
