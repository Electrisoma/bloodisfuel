package net.electrisoma.bloodisfuel.registry.items;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BCreativeTabs;
import net.electrisoma.bloodisfuel.registry.BTags;
import net.electrisoma.bloodisfuel.registry.items.syringe_blade.SyringeBladeItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;


@SuppressWarnings("unused")
public class BItems {
    private static final CreateRegistrate REGISTRATE = BloodIsFuel.registrate();

    public static final ItemEntry<Item> DRAINED_MEAT =
            REGISTRATE.item("drained_meat", Item::new)
                    .model((c, p) -> p
                            .withExistingParent(c.getId().getPath(),
                                    new ResourceLocation("item/generated")).texture("layer0",
                                    new ResourceLocation(BloodIsFuel.MOD_ID,"item/" + c.getId().getPath())))
                    .properties(p -> p
                            .rarity(Rarity.COMMON)
                            .food(new FoodProperties.Builder()
                                    .meat()
                                    .nutrition(4)
                                    .saturationMod(0.1f)
                                    .effect(new MobEffectInstance(MobEffects.HUNGER,
                                            600, 0),0.8f)
                                    .effect(new MobEffectInstance(MobEffects.POISON,
                                            300, 2),0.8f)
                                    .build()))
                    .lang("Drained Meat")
                    .register();

    public static final ItemEntry<SyringeBladeItem>
            SYRINGE_BLADE = REGISTRATE
            .item("syringe_blade",
                    p -> new SyringeBladeItem(Tiers.IRON,3,-2.4f,p))
            .model(AssetLookup.itemModelWithPartials())
            .tag(BTags.AllItemTags.SYRINGE_BLADE.tag)
            .register();

    public static void register() {
        BloodIsFuel.LOGGER.info("Registering items for " + BloodIsFuel.NAME);
    }
}
