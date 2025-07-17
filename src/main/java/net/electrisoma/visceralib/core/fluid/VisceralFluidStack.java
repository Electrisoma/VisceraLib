//package net.electrisoma.visceralib.core.fluid;
//
//import net.electrisoma.visceralib.multiloader.FluidStackBridge;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.level.material.Fluid;
//
//import java.util.Objects;
//
//public class VisceralFluidStack {
//    private final Fluid fluid;
//    private int amount;
//    private CompoundTag tag;
//
//    public VisceralFluidStack(Fluid fluid, int amount) {
//        this(fluid, amount, new CompoundTag());
//    }
//
//    public VisceralFluidStack(Fluid fluid, int amount, CompoundTag tag) {
//        this.fluid = fluid;
//        this.amount = amount;
//        this.tag = tag == null ? new CompoundTag() : tag;
//    }
//
//    public Fluid getFluid() {
//        return fluid;
//    }
//
//    public int getAmount() {
//        return amount;
//    }
//
//    public CompoundTag getTag() {
//        return tag;
//    }
//
//    public void setAmount(int amount) {
//        this.amount = amount;
//    }
//
//    public boolean isEmpty() {
//        return fluid == null || amount <= 0;
//    }
//
//    public VisceralFluidStack copy() {
//        return new VisceralFluidStack(fluid, amount, tag.copy());
//    }
//
//    public VisceralFluidStack copyWithAmount(int newAmount) {
//        return new VisceralFluidStack(fluid, newAmount, tag.copy());
//    }
//
//    public CompoundTag toTag() {
//        return FluidStackBridge.write(this);
//    }
//
//    public static VisceralFluidStack fromTag(CompoundTag tag) {
//        return FluidStackBridge.read(tag);
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof VisceralFluidStack other)) return false;
//        return amount == other.amount &&
//                Objects.equals(fluid, other.fluid) &&
//                Objects.equals(tag, other.tag);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(fluid, amount, tag);
//    }
//}
