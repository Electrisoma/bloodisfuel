package net.electrisoma.bloodisfuel.registry.items;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BTags;
import net.electrisoma.bloodisfuel.registry.items.syringe_blade.SyringeBladeItem;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;

import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;


public class BItems {
    private static final CreateRegistrate REGISTRATE = BloodIsFuel.registrate();

    public static final ItemEntry<Item> DRAINED_MEAT =
            REGISTRATE.item("drained_meat", Item::new)
                    .properties(p -> p.rarity(Rarity.COMMON)
                            .food(new FoodProperties.Builder()
                                    .nutrition(4)
                                    .meat()
                                    .saturationMod(0.1f)
                                    .effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0),0.8f)
                                    .effect(new MobEffectInstance(MobEffects.POISON, 300, 2),0.8f)
                                    .build()))
                    .register();

    public static final ItemEntry<SyringeBladeItem> SYRINGE_BLADE =
            REGISTRATE.item("syringe_blade", p ->
                            new SyringeBladeItem(Tiers.IRON, 3, -2.4F, p))
                    .model(AssetLookup.itemModelWithPartials())
                    .tag(BTags.AllItemTags.SYRINGE_BLADE.tag)
                    .lang("Syringe Blade")
                    .register();

    public static void register(){
        // load the class and register everything
        BloodIsFuel.LOGGER.info("Registering items for " + BloodIsFuel.NAME);
    }
}
