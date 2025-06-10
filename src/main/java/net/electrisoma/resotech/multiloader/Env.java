package net.electrisoma.resotech.multiloader;

import com.google.common.base.Supplier;
import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings({"unused", "UnimplementedExpectPlatform"})
public enum Env {
    CLIENT, SERVER;

    public static final Env CURRENT = getCurrent();

    public boolean isCurrent() {
        return this == CURRENT;
    }

    public void runIfCurrent(Supplier<Runnable> run) {
        if (isCurrent())
            run.get().run();
    }

    public static <T> T unsafeRunForDist(Supplier<Supplier<T>> clientTarget, Supplier<Supplier<T>> serverTarget) {
        return switch (Env.CURRENT) {
            case CLIENT -> clientTarget.get().get();
            case SERVER -> serverTarget.get().get();
        };
    }

    @ApiStatus.Internal
    @ExpectPlatform
    private static Env getCurrent() {
        throw new AssertionError();
    }
}