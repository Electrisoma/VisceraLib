//package net.electrisoma.visceralib.registry;
//
//import net.electrisoma.visceralib.api.registration.builders.AdvancementBuilder;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.BiConsumer;
//import java.util.function.UnaryOperator;
//
//import static net.electrisoma.visceralib.api.registration.builders.AdvancementBuilder.TaskType.*;
//
//@SuppressWarnings("unused")
//public class ResoTechAdvancements {
//    public static final List<AdvancementBuilder> ENTRIES = new ArrayList<>();
//
//    public static final AdvancementBuilder ROOT =
//            register("root", b -> b
//                    .title("ResoTech")
//                    .description("Here Be Resonance")
//                    .icon(VisceraLibItems.TEST_ITEM.get())
//                    .awardedForFree()
//                    .type(SILENT)
//            );
//
//    private static AdvancementBuilder register(String id, UnaryOperator<AdvancementBuilder.Builder> builder) {
//        AdvancementBuilder advancement = new AdvancementBuilder(id, builder);
//        ENTRIES.add(advancement);
//        return advancement;
//    }
//
//    public static void provideLang(BiConsumer<String, String> consumer) {
//        for (AdvancementBuilder advancement : ENTRIES)
//            advancement.provideLang(consumer);
//    }
//}
