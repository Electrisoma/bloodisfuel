package net.electrisoma.bloodisfuel.api.utils;

import com.simibubi.create.AllEnchantments;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public interface ItemCapacityUtils {
    default int getBaseCapacity(ItemStack stack) {
        return 1000;
    }
    default int getCapacityEnchantmentAddition(ItemStack stack) {
        return 1000;
    }
    default int getCapacity(ItemStack stack) {
        int level = stack.getEnchantmentLevel(AllEnchantments.CAPACITY.get());
        return getBaseCapacity(stack) + getCapacityEnchantmentAddition(stack) * level;
    }

    default int getChargeCount(ItemStack stack) {
        int capacity = getCapacity(stack);
        return Math.min(capacity / 250, 10);
    }
    default int getUseAmount(ItemStack stack) {
        int charges = getChargeCount(stack);
        int capacity = getCapacity(stack);
        return capacity / charges;
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
