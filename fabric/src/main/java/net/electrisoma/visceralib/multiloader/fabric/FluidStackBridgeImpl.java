//package net.electrisoma.visceralib.multiloader.fabric;
//
//import net.electrisoma.visceralib.core.fluid.VisceralFluidStack;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.level.material.Fluid;
//
//public class FluidStackBridgeImpl {
//    public static CompoundTag write(VisceralFluidStack stack) {
//        CompoundTag tag = new CompoundTag();
//        tag.putString("fluid", BuiltInRegistries.FLUID.getKey(stack.getFluid()).toString());
//        tag.putInt("amount", stack.getAmount());
//        if (!stack.getTag().isEmpty()) {
//            tag.put("tag", stack.getTag());
//        }
//        return tag;
//    }
//
//    public static VisceralFluidStack read(CompoundTag tag) {
//        if (!tag.contains("fluid")) {
//            return new VisceralFluidStack(null, 0);
//        }
//
//        ResourceLocation id = ResourceLocation.withDefaultNamespace(tag.getString("fluid"));
//        Fluid fluid = BuiltInRegistries.FLUID.get(id);
//        int amount = tag.getInt("amount");
//        CompoundTag fluidTag = tag.contains("tag") ? tag.getCompound("tag") : new CompoundTag();
//        return new VisceralFluidStack(fluid, amount, fluidTag);
//    }
//}
