package net.electrisoma.bloodisfuel.registry.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;


@SuppressWarnings("all")
public class FuelItems extends Item {
    private int burnTime = 0;

    public FuelItems(Properties pProperties, int burnTime) {
        super(pProperties);
        this.burnTime = burnTime;
    }

    @Override
    public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType){
        return this.burnTime;
    }
}
