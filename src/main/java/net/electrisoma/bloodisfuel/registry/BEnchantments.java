package net.electrisoma.bloodisfuel.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.enchantments.ChargesEnchantment;

import com.simibubi.create.foundation.data.CreateRegistrate;

import com.tterrag.registrate.util.entry.RegistryEntry;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;


public class BEnchantments {
    private static final CreateRegistrate REGISTRATE = BloodIsFuel.registrate();

    public static RegistryEntry<ChargesEnchantment> EXTRA_VIALS;

    public static void register() {
        BloodIsFuel.LOGGER.info("Registering enchantments for " + BloodIsFuel.NAME);

        EXTRA_VIALS = REGISTRATE
                .object("extra_vials")
                .enchantment(EnchantmentCategory.WEAPON, ChargesEnchantment::new)
                .addSlots(EquipmentSlot.MAINHAND)
                .lang("Extra Vials")
                .rarity(Enchantment.Rarity.UNCOMMON)
                .register();
    }
}
