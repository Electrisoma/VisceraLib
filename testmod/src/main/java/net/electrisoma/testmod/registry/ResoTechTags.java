package net.electrisoma.testmod.registry;//package net.electrisoma.visceralib.registry;
//
//import net.createmod.catnip.lang.Lang;
//
//import net.electrisoma.visceralib.VisceraLib;
//
//import net.minecraft.core.Registry;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.tags.TagKey;
//import net.minecraft.world.item.BlockItem;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraft.world.level.material.FluidState;
//
//import static net.electrisoma.visceralib.registry.ResoTechTags.NameSpace.*;
//
//public class ResoTechTags {
//    public static void init() {
//        AllBlockTags.register();
//        AllItemTags.register();
//        AllFluidTags.register();
//        VisceraLib.LOGGER.info("Registering Tags for " + VisceraLib.NAME);
//    }
//
//    public enum NameSpace {
//        MOD(VisceraLib.MOD_ID, false, true),
//        COMMON("c")
//
//        ;
//
//        public final String id;
//        public final boolean optionalDefault;
//        public final boolean alwaysDatagenDefault;
//
//        NameSpace(String id) {
//            this(id, true, false);
//        }
//        NameSpace(String id, boolean optionalDefault, boolean alwaysDatagenDefault) {
//            this.id = id;
//            this.optionalDefault = optionalDefault;
//            this.alwaysDatagenDefault = alwaysDatagenDefault;
//        }
//    }
//
//    public enum AllBlockTags {
//
//        ;
//
//        public final TagKey<Block> tag;
//        public final boolean alwaysDatagen;
//
//        AllBlockTags() {
//            this(NameSpace.MOD);
//        }
//        AllBlockTags(NameSpace namespace) {
//            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
//        }
//        AllBlockTags(NameSpace namespace, String path) {
//            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
//        }
//        AllBlockTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
//            this(namespace, null, optional, alwaysDatagen);
//        }
//        AllBlockTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
//            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? Lang.asId(name()) : path);
//            if (optional) {
//                tag = optionalTag(BuiltInRegistries.BLOCK, id);
//            } else {
//                tag = TagKey.create(Registries.BLOCK, id);
//            }
//            this.alwaysDatagen = alwaysDatagen;
//        }
//
//        @SuppressWarnings("deprecation")
//        public boolean matches(Block block) {
//            return block.builtInRegistryHolder()
//                    .is(tag);
//        }
//        public boolean matches(ItemStack stack) {
//            return stack.getItem() instanceof BlockItem blockItem && matches(blockItem.getBlock());
//        }
//        public boolean matches(BlockState state) {
//            return state.is(tag);
//        }
//
//        private static void register() {}
//    }
//
//    public enum AllItemTags {
//
//        ;
//
//        public final TagKey<Item> tag;
//        public final boolean alwaysDatagen;
//
//        AllItemTags() {
//            this(NameSpace.MOD);
//        }
//        AllItemTags(NameSpace namespace) {
//            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
//        }
//        AllItemTags(NameSpace namespace, String path) {
//            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
//        }
//        AllItemTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
//            this(namespace, null, optional, alwaysDatagen);
//        }
//        AllItemTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
//            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? Lang.asId(name()) : path);
//            if (optional) {
//                tag = optionalTag(BuiltInRegistries.ITEM, id);
//            } else {
//                tag = TagKey.create(Registries.ITEM, id);
//            }
//            this.alwaysDatagen = alwaysDatagen;
//        }
//
//        @SuppressWarnings("deprecation")
//        public boolean matches(Item item) {
//            return item.builtInRegistryHolder()
//                    .is(tag);
//        }
//        public boolean matches(ItemStack stack) {
//            return stack.is(tag);
//        }
//
//        public static void register() { }
//    }
//
//    public enum AllFluidTags {
//        TEST(COMMON)
//
//        ;
//
//        public final TagKey<Fluid> tag;
//        public final boolean alwaysDatagen;
//
//        AllFluidTags() {
//            this(NameSpace.MOD);
//        }
//        AllFluidTags(NameSpace namespace) {
//            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
//        }
//        AllFluidTags(NameSpace namespace, String path) {
//            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
//        }
//        AllFluidTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
//            this(namespace, null, optional, alwaysDatagen);
//        }
//        AllFluidTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
//            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? Lang.asId(name()) : path);
//            if (optional) {
//                tag = optionalTag(BuiltInRegistries.FLUID, id);
//            } else {
//                tag = TagKey.create(Registries.FLUID, id);
//            }
//            this.alwaysDatagen = alwaysDatagen;
//        }
//
//        @SuppressWarnings("deprecation")
//        public boolean matches(Fluid fluid) {
//            return fluid.is(tag);
//        }
//        public boolean matches(FluidState state) {
//            return state.is(tag);
//        }
//
//        private static void register() {}
//    }
//
//    public static <T> TagKey<T> optionalTag(Registry<T> registry, ResourceLocation id) {
//        return TagKey.create(registry.key(), id);
//    }
//}
