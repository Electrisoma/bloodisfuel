package net.electrisoma.bloodisfuel.infrastructure.data;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BItems;

import net.minecraft.world.level.ItemLike;

import java.util.function.BiConsumer;


public class defaultLang {

    static String tab = "itemGroup";
    static String dm = "death.attack";

    public static void provideLang(BiConsumer<String, String> consumer) {

        var syringe = BItems.SYRINGE_BLADE;
        var meat = BItems.DRAINED_MEAT;

        // interfaces
        consume(consumer, tab,"base", "Create: Blood is Fuel!");

        consume(consumer, dm, "boiling", "%s died like a lobster");
        consume(consumer, dm, "syringe_blade", "%1$s was stabbed by %2$s's Syringe Blade");
        consume(consumer, dm, "syringe_blade.item", "%1$s was stabbed by %2$s using %3$s");

        // tooltips
        tooltipCondition(consumer, syringe, "L-Click at Entity", 1);
        tooltipBehaviour(consumer, syringe, "Extract _fluid_ while empty, Inject _fluid_ while full", 1);
        tooltipCondition(consumer, syringe, "Hold R-Click", 2);
        tooltipBehaviour(consumer, syringe, "Extract _fluid_ from self while empty, Inject _fluid_ in self while full", 2);
        tooltipCondition(consumer, syringe, "R-Click while Sneaking", 3);
        tooltipBehaviour(consumer, syringe, "Drain _fluid_", 3);
        tooltipSummary(consumer, syringe, "Extract and inject the _fluid_ of your _friends_ or _enemies_!");

        tooltipCondition(consumer, meat, "Put in a drain or a basin with press", 1);
        tooltipBehaviour(consumer, meat, "Extract _blood_", 1);
        tooltipSummary(consumer, meat, "Poor fella");

        tooltipMisc(consumer,"empty", "Empty :3");
        tooltipMisc(consumer,"effect", "Effect");
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
    private static void tooltipMisc(BiConsumer<String, String> consumer, String key, String desc) {
        consume(consumer, BloodIsFuel.MOD_ID + ".tooltip." + key, desc);
    }
}