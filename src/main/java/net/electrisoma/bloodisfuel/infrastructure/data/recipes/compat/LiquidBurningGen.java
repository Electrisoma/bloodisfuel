package net.electrisoma.bloodisfuel.infrastructure.data.recipes.compat;

import com.mrh0.createaddition.index.CARecipes;
import com.simibubi.create.AllRecipeTypes;
import net.electrisoma.bloodisfuel.infrastructure.data.recipes.BloodProcessingRecipeGen;
import net.electrisoma.bloodisfuel.infrastructure.utils.Utils;
import net.electrisoma.bloodisfuel.registry.fluids.BIF_Fluids;

import com.simibubi.create.content.processing.recipe.HeatCondition;

import net.minecraft.data.PackOutput;

import java.util.Objects;


//@SuppressWarnings("unused")
//public class LiquidBurningGen extends BloodProcessingRecipeGen {
//    GeneratedRecipe
//            VISCERA =
//            create(Objects.requireNonNull(Utils.location("viscera")), b -> b
//                    .require(BIF_Fluids.OIL_ENRICHED_BLOOD.get(), 100)
//                    .requiresHeat(HeatCondition.HEATED)
//                    .duration(100)
//                    .output(BIF_Fluids.DIESEL_INFUSED_BLOOD.get(),50)
//                    .output(BIF_Fluids.GASOLINE_INFUSED_BLOOD.get(), 50));
//
//    public LiquidBurningGen(PackOutput generator) {
//        super(generator);
//    }
//
//    @Override
//    protected AllRecipeTypes getRecipeType() {
//        return CARecipes.LIQUID_BURNING;
//    }
//}