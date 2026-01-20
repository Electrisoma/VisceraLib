package net.electrisoma.visceralib.impl.registration;

import net.electrisoma.visceralib.api.registration.registry.register.VisceralRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class Constants {

    public static final String MOD_ID = "visceralib_registration";
    public static final String NAME = "VisceraLib-Registration";
    public static final Logger LOG = LoggerFactory.getLogger(NAME);
    public static final VisceralRegistry VISCERAL_REGISTRY = new VisceralRegistry("visceralib");

    @Internal
    public static VisceralRegistry registry() {
        return VISCERAL_REGISTRY;
    }
}