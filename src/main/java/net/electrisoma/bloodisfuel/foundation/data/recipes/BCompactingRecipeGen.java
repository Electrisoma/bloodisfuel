package net.electrisoma.bloodisfuel.foundation.data.recipes;

import com.simibubi.create.api.data.recipe.CompactingRecipeGen;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.*;

import com.simibubi.create.AllRecipeTypes;

import net.minecraft.data.PackOutput;


@SuppressWarnings("unused")
public class BCompactingRecipeGen extends CompactingRecipeGen {
    public BCompactingRecipeGen(PackOutput output) {
        super(output, BloodIsFuel.MOD_ID);
    }

    GeneratedRecipe

    VISCERA_FROM_MEATS = create("viscera_from_meats", b -> b
            .require(BTags.BItemTags.MEATS.tag)
            .output(BFluids.VISCERA.get(), 100)
            .output(.4f, BItems.DRAINED_MEAT)),
    VISCERA_FROM_FISHES = create("viscera_from_fishes", b -> b
            .require(BTags.BItemTags.FISHES.tag)
            .output(BFluids.VISCERA.get(), 25))

    ;
}