//package net.electrisoma.bloodisfuel.infrastructure.data.lang;
//
//import com.tterrag.registrate.providers.RegistrateLangProvider;
//
//import java.util.Map;
//import java.util.function.BiConsumer;
//
//public class BLangGen {
//    public static void generate(RegistrateLangProvider provider) {
//        BiConsumer<String, String> langConsumer = provider::add;
//
//        providePartialLang(langConsumer);
//    }
//
//    private static void providePartialLang(BiConsumer<String, String> consumer) {
//        consume(consumer, "itemGroup.bloodisfuel.base", "Create: Blood is Fuel!");
//
//        consume(consumer, "item.bloodisfuel.syringe_blade.tooltip.behaviour1", "Extract blood");
//        consume(consumer, "item.bloodisfuel.syringe_blade.tooltip.condition1", "L-Click at Mob");
//        consume(consumer, "item.bloodisfuel.syringe_blade.tooltip.summary", "Extract _blood_ from your _friends_ or _enemies_!");
//        consume(consumer, "bloodisfuel.tooltip.empty", "Empty :3");
//    }
//
//    private static void consume(BiConsumer<String, String> consumer, String key, String enUS) {
//        consumer.accept(key, enUS);
//    }
//}
