package net.electrisoma.resotech.multiloader;

import com.google.common.base.Supplier;
import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.ApiStatus;

public enum PlatformInfo {
    NEOFORGE, FABRIC;

    public static final PlatformInfo CURRENT = getCurrent();

    public boolean isCurrent() {
        return this == CURRENT;
    }

    public void runIfCurrent(Supplier<Runnable> run) {
        if (isCurrent())
            run.get().run();
    }

    @ApiStatus.Internal
    @ExpectPlatform
    public static PlatformInfo getCurrent() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static String findVersion() {
        throw new AssertionError();
    }
}