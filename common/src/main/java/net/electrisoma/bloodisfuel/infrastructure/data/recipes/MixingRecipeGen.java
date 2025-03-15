package net.electrisoma.bloodisfuel.infrastructure.data.recipes;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.electrisoma.bloodisfuel.registry.fluids.BFluids;
import net.electrisoma.bloodisfuel.registry.BTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

public class MixingRecipeGen extends BProcessingRecipeGen {
//    GeneratedRecipe

//            VISCERA_TO_BLOOD =
//            create("viscera_to_blood", b -> b
//                    .require(BTags.AllFluidTags.VISCERA.tag,1000)
//                    .requiresHeat(HeatCondition.HEATED)
//                    .output(BFluids.BLOOD.get(), 500)),
//
//            OIL_ENRICHED_BLOOD =
//            create("oil_enriched_blood", b -> b
//                    .require(BTags.AllFluidTags.CRUDE_OIL.tag,500)
//                    .require(BTags.AllFluidTags.ENRICHED_BLOOD.tag,500)
//                    .requiresHeat(HeatCondition.SUPERHEATED)
//                    .output(BFluids.OIL_ENRICHED_BLOOD.get(), 250)),
//
//            ENRICHED_BLOOD_FROM_SUGARS =
//            create("enriched_blood_from_carbohydrates", b -> b
//                    .require(BTags.AllFluidTags.BLOOD.tag,1000)
//                    .require(BTags.AllItemTags.CARBOHYDRATES.tag)
//                    .requiresHeat(HeatCondition.SUPERHEATED)
//                    .output(BFluids.ENRICHED_BLOOD.get(), 500)),
//
//            ENRICHED_BLOOD_FROM_LIQUID_SUGARS =
//            create("enriched_blood_from_liquid_carbohydrates", b -> b
//                    .require(BTags.AllFluidTags.BLOOD.tag,500)
//                    .require(BTags.AllFluidTags.LIQUID_CARBOHYDRATES.tag,500)
//                    .requiresHeat(HeatCondition.SUPERHEATED)
//                    .output(BFluids.ENRICHED_BLOOD.get(), 250));

    public MixingRecipeGen(FabricDataOutput generator) {
        super(generator);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.MIXING;
    }
}
