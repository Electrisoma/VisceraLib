package net.electrisoma.visceralib.data.util;

import java.io.IOException;

@FunctionalInterface
public interface ThrowingBiConsumer<T, U> {
    void accept(T t, U u) throws IOException;

    static <T, U> java.util.function.BiConsumer<T, U> unchecked(ThrowingBiConsumer<T, U> throwingConsumer) {
        return (t, u) -> {
            try {
                throwingConsumer.accept(t, u);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
