package net.electrisoma.visceralib.multiloader;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class VisceralInitHook {
    static {
        bootstrap();
    }

    @ExpectPlatform
    public static void bootstrap() {
        throw new AssertionError("Platform-specific bootstrap not implemented.");
    }
}
