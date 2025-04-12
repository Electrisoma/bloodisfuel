package net.electrisoma.bloodisfuel.registry.items;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;

public class BloodBucketItem extends BucketItem {
    public BloodBucketItem(Fluid p_40689_, Properties p_40690_) {
        super(p_40689_, p_40690_);
    }
    public BloodBucketItem(java.util.function.Supplier<? extends Fluid> supplier, Item.Properties builder) {
        super((Fluid) supplier,builder);
    }

    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return 200;
    }
}
