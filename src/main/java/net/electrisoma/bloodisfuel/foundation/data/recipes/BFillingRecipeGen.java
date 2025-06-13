package net.electrisoma.bloodisfuel.foundation.data.recipes;

import com.simibubi.create.api.data.recipe.FillingRecipeGen;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BTags;
import net.electrisoma.bloodisfuel.registry.BItems;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;


@SuppressWarnings("unused")
public class BFillingRecipeGen extends FillingRecipeGen {
    public BFillingRecipeGen(PackOutput output) {
        super(output, BloodIsFuel.MOD_ID);
    }

    GeneratedRecipe

    BLOOD_BOTTLE = create("blood_bottle", b -> b
            .require(BTags.BFluidTags.BLOOD.tag, 250)
            .require(Items.GLASS_BOTTLE)
            .output(BItems.BLOOD_BOTTLE))
    ;
}
