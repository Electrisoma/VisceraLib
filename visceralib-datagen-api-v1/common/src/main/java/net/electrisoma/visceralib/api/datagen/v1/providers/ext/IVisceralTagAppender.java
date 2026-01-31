//package net.electrisoma.visceralib.api.datagen.v1.providers.ext;
//
//import net.minecraft.data.tags.TagsProvider;
//import net.minecraft.resources.ResourceKey;
//
//import java.util.function.Function;
//
//public interface IVisceralTagAppender<T> {
//
//    @SuppressWarnings("unchecked")
//    default TagsProvider.TagAppender<T> add(Function<T, ResourceKey<T>> lookup, T... elements) {
//        TagsProvider.TagAppender<T> self = (TagsProvider.TagAppender<T>) this;
//        for (T element : elements) {
//            self.add(lookup.apply(element));
//        }
//        return self;
//    }
//
//    default Object viscera$self() {
//        return this;
//    }
//}