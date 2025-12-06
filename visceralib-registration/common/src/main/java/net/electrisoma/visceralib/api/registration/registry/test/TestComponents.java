//package net.electrisoma.visceralib.registry.test;
//
//import net.electrisoma.visceralib.impl.core.Constants;
//import net.electrisoma.visceralib.registry.holder.DataComponentTypeHolder;
//import net.electrisoma.visceralib.registry.holder.RegistryObject;
//import net.electrisoma.visceralib.registry.test.stuff.TestComponent;
//import net.minecraft.core.component.DataComponentType;
//
//public class TestComponents {
//
//    public static void init() {}
//
//    public static final DataComponentTypeHolder<TestComponent> TEST_COMPONENT = Constants.REGISTRY
//            .<TestComponent>dataComponentType("test_component", b -> b
//                    .persistent(TestComponent.CODEC))
//            .register();
//}