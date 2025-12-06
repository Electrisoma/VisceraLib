//package net.electrisoma.visceralib.registry.builder;
//
//import net.electrisoma.visceralib.registry.VisceralRegistry;
//import net.electrisoma.visceralib.registry.holder.DataComponentTypeHolder;
//import net.minecraft.core.HolderOwner;
//import net.minecraft.core.component.DataComponentType;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.resources.ResourceKey;
//
//import java.util.function.Function;
//import java.util.function.UnaryOperator;
//
//public class DataComponentTypeBuilder<DC> extends AbstractBuilder<DataComponentType<?>, DataComponentType<DC>, DataComponentTypeHolder<DC>> {
//
//    private final Function<? super DataComponentType.Builder<DC>, ? extends DataComponentType.Builder<DC>> modifiers;
//
//    public DataComponentTypeBuilder(VisceralRegistry owner, String name, UnaryOperator<DataComponentType.Builder<DC>> operator) {
//        super(owner, name, BuiltInRegistries.DATA_COMPONENT_TYPE);
//
//        this.modifiers = builder -> {
//            operator.apply(builder);
//            return builder;
//        };
//    }
//
//    @Override
//    DataComponentType<DC> build() {
//        DataComponentType.Builder<DC> builder = DataComponentType.<DC>builder().cacheEncoding();
//
//        builder = modifiers.apply(builder);
//
//        return builder.build();
//    }
//
//    @Override
//    DataComponentTypeHolder<DC> getHolder(HolderOwner<DataComponentType<?>> owner, ResourceKey<DataComponentType<?>> key) {
//        //noinspection unchecked, rawtypes
//        return (DataComponentTypeHolder<DC>) new DataComponentTypeHolder<>((HolderOwner) owner, (ResourceKey) key);
//    }
//}