//package net.electrisoma.visceralib.platform.core.services;
//
//import net.electrisoma.visceralib.api.core.LazyServiceLoader;
//import net.minecraft.resources.ResourceLocation;
//
//import java.util.function.Supplier;
//
//public interface IResourceLocationHelper {
//
//    Supplier<IResourceLocationHelper> INSTANCE = new LazyServiceLoader<>(IResourceLocationHelper.class);
//
//    ResourceLocation path(String namespace, String path);
//}