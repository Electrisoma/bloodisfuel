package net.electrisoma.bloodisfuel.foundation.data.recipes;

import net.electrisoma.bloodisfuel.registry.BTags;
import net.electrisoma.bloodisfuel.registry.BItems;

import com.simibubi.create.AllRecipeTypes;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;


public class FillingRecipeGen extends BProcessingRecipeGen {
    GeneratedRecipe

    BLOOD_BOTTLE = create("blood_bottle", b -> b
            .require(BTags.BFluidTags.BLOOD.tag, 250)
            .require(Items.GLASS_BOTTLE)
            .output(BItems.BLOOD_BOTTLE))

    ;

    public FillingRecipeGen(PackOutput generator) {
        super(generator);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.FILLING;
    }
}
