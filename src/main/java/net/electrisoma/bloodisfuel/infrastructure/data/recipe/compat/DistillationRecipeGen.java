package net.electrisoma.bloodisfuel.infrastructure.data.recipe.compat;

import com.jesz.createdieselgenerators.recipes.RecipeRegistry; //boo, i dont want any mod dependencies :(
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.electrisoma.bloodisfuel.infrastructure.data.recipe.BloodProcessingRecipeGen;
import net.electrisoma.bloodisfuel.registry.BIF_Fluids;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;


//weird as shit, idk lol
@SuppressWarnings("unused")
public class DistillationRecipeGen extends BloodProcessingRecipeGen {
    GeneratedRecipe
            BLOOD_DISTILLATION =
            create(ResourceLocation.tryParse("blood_distillation"), b -> b
                    .require(BIF_Fluids.OIL_ENRICHED_BLOOD.get(), 100)
                    .requiresHeat(HeatCondition.SUPERHEATED)
                    .duration(100)
                    .output(BIF_Fluids.DIESEL_INFUSED_BLOOD.get(),50)
                    .output(BIF_Fluids.GASOLINE_INFUSED_BLOOD.get(), 50));

    public DistillationRecipeGen(PackOutput generator) {
        super(generator);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return RecipeRegistry.DISTILLATION;
    }
}