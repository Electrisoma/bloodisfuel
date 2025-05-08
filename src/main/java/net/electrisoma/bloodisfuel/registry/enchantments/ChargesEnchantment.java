package net.electrisoma.bloodisfuel.registry.enchantments;

import com.simibubi.create.content.equipment.armor.CapacityEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;


public class ChargesEnchantment extends Enchantment {

    public ChargesEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] slots) {
        super(rarity, category, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() instanceof CapacityEnchantment.ICapacityEnchantable;
    }

    public interface ICapacityEnchantable { }
}
