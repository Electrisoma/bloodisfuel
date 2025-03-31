package net.electrisoma.bloodisfuel.base.data.lang;

import net.electrisoma.bloodisfuel.registry.items.BItems;
import net.minecraft.world.level.ItemLike;

import java.util.function.BiConsumer;


public class defaultLang {
    static String tab = "itemGroup";

    public static void provideLang(BiConsumer<String, String> consumer) {
        var syringe = BItems.SYRINGE_BLADE;
        var meat = BItems.DRAINED_MEAT;

        //interfaces
        consume(consumer, tab,"base", "Create: Blood is Fuel!");


        //tooltips
        tooltipCondition(consumer, syringe, "L-Click at Mob", 1);
        tooltipBehaviour(consumer, syringe, "Extract _blood_", 1);
        tooltipSummary(consumer, syringe, "Extract _blood_ from your _friends_ or _enemies_!");

        tooltipCondition(consumer, meat, "Put in a basin with press or a drain", 1);
        tooltipBehaviour(consumer, meat, "Extract _blood_", 1);
        tooltipSummary(consumer, meat, "Poor fella");

        consume(consumer, "bloodisfuel.tooltip.empty", "Empty :3");
    }

    //capitalism!! (consumers)
    private static void consume(BiConsumer<String, String> consumer, String type, String key, String enUS) {
        boolean flag = type.isEmpty();
        consumer.accept((flag ? "bloodisfuel." : type + ".bloodisfuel.") + key, enUS);
    }

    private static void consume(BiConsumer<String, String> consumer, String key, String enUS) {
        consumer.accept(key, enUS);
    }

    //tooltip methods
    private static String ItemName(ItemLike item) {
        return item.asItem().getDescriptionId();
    }
    private static void tooltipBehaviour(BiConsumer<String, String> consumer, ItemLike item, String desc, int line) {
        consume(consumer, ItemName(item) + ".tooltip.behaviour" + line, desc);
    }
    private static void tooltipCondition(BiConsumer<String, String> consumer, ItemLike item, String desc, int line) {
        consume(consumer, ItemName(item) + ".tooltip.condition" + line, desc);
    }
    private static void tooltipSummary(BiConsumer<String, String> consumer, ItemLike item, String desc) {
        consume(consumer, ItemName(item) + ".tooltip.summary", desc);
    }
}
