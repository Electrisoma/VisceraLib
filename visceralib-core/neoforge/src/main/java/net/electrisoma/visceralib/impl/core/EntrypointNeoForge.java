package net.electrisoma.visceralib.impl.core;

import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class EntrypointNeoForge {

    public EntrypointNeoForge() {
        Entrypoint.init();
    }
}
