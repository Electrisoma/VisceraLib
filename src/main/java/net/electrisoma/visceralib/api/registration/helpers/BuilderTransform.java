package net.electrisoma.visceralib.api.registration.helpers;

import net.electrisoma.visceralib.api.registration.builders.AbstractBuilder;

import java.util.function.Function;

@FunctionalInterface
public interface BuilderTransform<B extends AbstractBuilder<?, ?, B>> extends Function<B, B> {

    static <B extends AbstractBuilder<?, ?, B>> BuilderTransform<B> transform(Function<B, B> fn) {
        return fn::apply;
    }
}
