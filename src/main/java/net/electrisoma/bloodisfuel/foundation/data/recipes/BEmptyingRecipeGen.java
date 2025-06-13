package net.electrisoma.bloodisfuel.foundation.data.recipes;

import com.simibubi.create.api.data.recipe.EmptyingRecipeGen;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.*;

import com.simibubi.create.AllRecipeTypes;

import net.minecraft.data.PackOutput;


@SuppressWarnings("unused")
public class BEmptyingRecipeGen extends EmptyingRecipeGen {
    public BEmptyingRecipeGen(PackOutput output) {
        super(output, BloodIsFuel.MOD_ID);
    }

    GeneratedRecipe

    VISCERA_FROM_MEATS = create("viscera_from_meats", b -> b
            .require(BTags.BItemTags.MEATS.tag)
            .output(.4f, BItems.DRAINED_MEAT)
            .output(BFluids.VISCERA.get(), 100)),
    VISCERA_FROM_FISHES = create("viscera_from_fishes", b -> b
            .require(BTags.BItemTags.FISHES.tag)
            .output(.4f, BItems.DRAINED_MEAT)
            .output(BFluids.VISCERA.get(), 25))

    ;

}