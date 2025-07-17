//package net.electrisoma.visceralib.multiloader.neoforge;
//
//import net.electrisoma.visceralib.core.fluid.VisceralFluidStack;
//import net.minecraft.core.Holder;
//import net.minecraft.core.component.DataComponentMap;
//import net.minecraft.core.component.DataComponentPatch;
//import net.minecraft.core.component.PatchedDataComponentMap;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.level.material.Fluid;
//import net.neoforged.neoforge.fluids.FluidStack;
//
//public class FluidStackBridgeImpl {
//    public static CompoundTag write(VisceralFluidStack stack) {
//        CompoundTag tag = new CompoundTag();
//        tag.putString("fluid", BuiltInRegistries.FLUID.getKey(stack.getFluid()).toString());
//        tag.putInt("amount", stack.getAmount());
//        if (!stack.getTag().isEmpty()) tag.put("tag", stack.getTag());
//        return tag;
//    }
//
//    public static VisceralFluidStack read(CompoundTag tag) {
//        if (!tag.contains("fluid")) return new VisceralFluidStack(null, 0);
//        ResourceLocation id = ResourceLocation.parse(tag.getString("fluid"));
//        Fluid fluid = BuiltInRegistries.FLUID.get(id);
//        int amt = tag.getInt("amount");
//        CompoundTag t = tag.contains("tag") ? tag.getCompound("tag") : new CompoundTag();
//        return new VisceralFluidStack(fluid, amt, t);
//    }
//
//    public static Object toPlatformStack(VisceralFluidStack stack) {
//        if (stack == null || stack.isEmpty()) return FluidStack.EMPTY;
//        Holder<Fluid> holder = stack.getFluid().builtInRegistryHolder();
//        DataComponentPatch patch = PatchedDataComponentMap.fromPatch(
//                DataComponentMap.EMPTY, DataComponentPatch.EMPTY
//        ).asPatch();
//        return new FluidStack(holder, stack.getAmount(), patch);
//    }
//
//    public static VisceralFluidStack fromPlatformStack(Object o) {
//        if (!(o instanceof FluidStack fs))
//            throw new IllegalArgumentException("Expected NeoForge FluidStack");
//        Fluid fluid = fs.getFluid();
//        int amt = fs.getAmount();
//        CompoundTag t = new CompoundTag();
//        return new VisceralFluidStack(fluid, amt, t);
//    }
//}
//
