package net.electrisoma.visceralib.impl.registration;

import net.electrisoma.visceralib.api.registration.registry.VisceralRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

    public static final String MOD_ID = "visceralib_registration";
    public static final String NAME = "VisceraLib-Registration";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static VisceralRegistry REGISTRY = new VisceralRegistry(MOD_ID);
}