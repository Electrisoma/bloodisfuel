package net.electrisoma.bloodisfuel.foundation.data.recipes;

import net.electrisoma.bloodisfuel.registry.fluids.BFluids;
import net.electrisoma.bloodisfuel.registry.BTags;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;

import net.minecraft.data.PackOutput;


@SuppressWarnings({"all"})
public class MixingRecipeGen extends BProcessingRecipeGen {

    GeneratedRecipe

            VISCERA_TO_BLOOD =
            create("viscera_to_blood", b -> b
                    .require(BTags.BFluidTags.VISCERA.tag,1000)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(BFluids.BLOOD.get(), 500)),

            OIL_ENRICHED_BLOOD =
            create("oil_enriched_blood", b -> b
                    .require(BTags.BFluidTags.CRUDE_OIL.tag,500)
                    .require(BTags.BFluidTags.ENRICHED_BLOOD.tag,500)
                    .requiresHeat(HeatCondition.SUPERHEATED)
                    .output(BFluids.OIL_ENRICHED_BLOOD.get(), 250)),

            ENRICHED_BLOOD_FROM_SUGARS =
            create("enriched_blood_from_carbohydrates", b -> b
                    .require(BTags.BFluidTags.BLOOD.tag,1000)
                    .require(BTags.BItemTags.CARBOHYDRATES.tag)
                    .requiresHeat(HeatCondition.SUPERHEATED)
                    .output(BFluids.ENRICHED_BLOOD.get(), 500)),

            ENRICHED_BLOOD_FROM_LIQUID_SUGARS =
            create("enriched_blood_from_liquid_carbohydrates", b -> b
                    .require(BTags.BFluidTags.BLOOD.tag,500)
                    .require(BTags.BFluidTags.LIQUID_CARBOHYDRATES.tag,500)
                    .requiresHeat(HeatCondition.SUPERHEATED)
                    .output(BFluids.ENRICHED_BLOOD.get(), 250));

    public MixingRecipeGen(PackOutput generator) {
        super(generator);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.MIXING;
    }
}