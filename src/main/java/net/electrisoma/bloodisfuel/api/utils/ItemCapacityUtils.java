package net.electrisoma.bloodisfuel.api.utils;

import net.electrisoma.bloodisfuel.registry.BEnchantments;

import com.simibubi.create.AllEnchantments;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;


@SuppressWarnings("unused")
public interface ItemCapacityUtils {

    default int getBaseCapacity(ItemStack stack) { return 1000; }
    default int getCapacityEnchantmentAddition(ItemStack stack) { return 1000; }
    default int getCapacity(ItemStack stack) {
        int enchantLevel = stack.getEnchantmentLevel(AllEnchantments.CAPACITY.get());
        return getBaseCapacity(stack) + getCapacityEnchantmentAddition(stack) * enchantLevel;
    }

    default int getChargeCount(ItemStack stack) {
        int baseCharges = 4;
        int extraLevel = stack.getEnchantmentLevel(BEnchantments.EXTRA_VIALS.get());
        return baseCharges + (extraLevel * 2);
    }
    default int getUseAmount(int capacity, int charges) {
        return (int) Math.ceil((double) capacity / charges);
    }
    default int getUseAmount(ItemStack stack) {
        return getUseAmount(getCapacity(stack), getChargeCount(stack));
    }

    class FuelItems extends Item {
        private final int burnTime;

        public FuelItems(Properties properties, int burnTime) {
            super(properties);
            this.burnTime = burnTime;
        }

        @Override
        public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
            return burnTime;
        }
    }
}
