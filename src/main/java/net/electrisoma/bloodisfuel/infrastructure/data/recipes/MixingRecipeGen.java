package net.electrisoma.bloodisfuel.infrastructure.data.recipes;

import net.electrisoma.bloodisfuel.registry.fluids.BIF_Fluids;
import net.electrisoma.bloodisfuel.registry.BIF_Tags;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;

import net.minecraft.data.PackOutput;


@SuppressWarnings({"all"})
public class MixingRecipeGen extends BloodProcessingRecipeGen {

    GeneratedRecipe

            VISCERA_TO_BLOOD =
            create("viscera_to_blood", b -> b
                    .require(BIF_Tags.AllFluidTags.VISCERA.tag,1000)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(BIF_Fluids.BLOOD.get(), 500)),

            OIL_ENRICHED_BLOOD =
            create("oil_enriched_blood", b -> b
                    .require(BIF_Tags.AllFluidTags.CRUDE_OIL.tag,500)
                    .require(BIF_Tags.AllFluidTags.ENRICHED_BLOOD.tag,500)
                    .requiresHeat(HeatCondition.SUPERHEATED)
                    .output(BIF_Fluids.OIL_ENRICHED_BLOOD.get(), 250)),

            ENRICHED_BLOOD_FROM_SUGARS =
            create("enriched_blood_from_carbohydrates", b -> b
                    .require(BIF_Tags.AllFluidTags.BLOOD.tag,1000)
                    .require(BIF_Tags.AllItemTags.CARBOHYDRATES.tag)
                    .requiresHeat(HeatCondition.SUPERHEATED)
                    .output(BIF_Fluids.ENRICHED_BLOOD.get(), 500)),

            ENRICHED_BLOOD_FROM_LIQUID_SUGARS =
            create("enriched_blood_from_liquid_carbohydrates", b -> b
                    .require(BIF_Tags.AllFluidTags.BLOOD.tag,500)
                    .require(BIF_Tags.AllFluidTags.LIQUID_CARBOHYDRATES.tag,500)
                    .requiresHeat(HeatCondition.SUPERHEATED)
                    .output(BIF_Fluids.ENRICHED_BLOOD.get(), 250));

    public MixingRecipeGen(PackOutput generator) {
        super(generator);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.MIXING;
    }
}