package net.electrisoma.bloodisfuel.registry;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.items.syringe_blade.SyringeBladeItem;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;

import com.tterrag.registrate.util.entry.ItemEntry;

import net.electrisoma.bloodisfuel.registry.items.syringe_gun.SyringeGunItem;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.effect.MobEffectInstance;


@SuppressWarnings({"all"})
public class BItems {

    private static final CreateRegistrate REGISTRATE = BloodIsFuel.registrate();

    static {REGISTRATE.setCreativeTab(BModTabs.BASE_CREATIVE_TAB);}

    public static void register() {
        BloodIsFuel.LOGGER.info("Registering items for " + BloodIsFuel.NAME);
    }

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
                            new SyringeBladeItem(Tiers.IRON,3,-2.4f,p))
                    .model(AssetLookup.itemModelWithPartials())
                    .tag(BTags.BItemTags.SYRINGE_BLADE.tag)
                    .register();

    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_SYRINGE_BLADE =
            REGISTRATE.item("incomplete_syringe_blade", SequencedAssemblyItem::new)
                    .model(AssetLookup.itemModelWithPartials())
                    .lang("Incomplete Syringe Blade")
                    .register();

    public static final ItemEntry<SyringeGunItem> SYRINGE_GUN =
            REGISTRATE.item("syringe_gun", SyringeGunItem::new)
                    .model(AssetLookup.itemModelWithPartials())
                    .tag(BTags.BItemTags.SYRINGE_GUN.tag)
                    .register();

//    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_SYRINGE_GUN =
//            REGISTRATE.item("incomplete_syringe_gun", SequencedAssemblyItem::new)
//                    .model(AssetLookup.itemModelWithPartials())
//                    .lang("Incomplete Syringe Gun")
//                    .register();

}