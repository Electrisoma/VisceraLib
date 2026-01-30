package net.electrisoma.visceralib.impl.registration.v1;

import net.electrisoma.visceralib.api.registration.v1.registry.register.AbstractRegistrationHelper;
import net.electrisoma.visceralib.api.registration.v1.registry.register.VisceralRegistrationHelper;
import net.electrisoma.visceralib.impl.registration.v1.test.TestRegistry;
import net.electrisoma.visceralib.impl.registration.v1.test.helper.TestRegistrationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class Constants {

    public static final String MOD_ID = "visceralib_registration_api_v1";
    public static final String NAME = "VisceraLib Registration API (v1)";
    public static final Logger LOG = LoggerFactory.getLogger(NAME);

    public static final VisceralRegistrationHelper VISCERAL_REGISTRY =
            VisceralRegistrationHelper.of("visceralib");

    public static final TestRegistrationHelper TEST_REGISTRY =
            TestRegistrationHelper.of("visceralib");

    @Internal
    public static VisceralRegistrationHelper registry() {
        return VISCERAL_REGISTRY;
    }

    @Internal
    public static TestRegistrationHelper testRegistry() {
        return TEST_REGISTRY;
    }
}