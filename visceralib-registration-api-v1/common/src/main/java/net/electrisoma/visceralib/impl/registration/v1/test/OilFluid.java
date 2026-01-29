package net.electrisoma.visceralib.impl.registration.v1.test;

import net.electrisoma.visceralib.api.registration.v1.registry.register.VisceralRegistrationHelper;
import net.electrisoma.visceralib.api.registration.v1.registry.register.fluid.VisceralFluid;
import net.electrisoma.visceralib.api.registration.v1.registry.register.fluid.VisceralFluidProperties;

public abstract class OilFluid extends VisceralFluid {

    protected OilFluid(VisceralFluidProperties props, VisceralRegistrationHelper helper) {
        super(props, helper);
    }

    public static class Flowing extends VisceralFluid.Flowing {

        public Flowing(VisceralFluidProperties props, VisceralRegistrationHelper helper) {
            super(props, helper);
        }
    }

    public static class Source extends VisceralFluid.Source {

        public Source(VisceralFluidProperties props, VisceralRegistrationHelper helper) {
            super(props, helper);
        }
    }
}