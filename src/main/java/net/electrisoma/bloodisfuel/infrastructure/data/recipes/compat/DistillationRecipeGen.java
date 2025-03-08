package net.electrisoma.bloodisfuel.infrastructure.data.recipes.compat;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.electrisoma.bloodisfuel.infrastructure.data.recipes.BloodProcessingRecipeGen;
import net.electrisoma.bloodisfuel.infrastructure.utils.Utils;
import net.electrisoma.bloodisfuel.registry.fluids.BIF_Fluids;

import com.jesz.createdieselgenerators.recipes.RecipeRegistry; //boo, i dont want any mod dependencies :(

import com.simibubi.create.content.processing.recipe.HeatCondition;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;


//weird as shit, idk lol
@SuppressWarnings("unused")
public class DistillationRecipeGen extends BloodProcessingRecipeGen {
    GeneratedRecipe
            BLOOD_DISTILLATION =
            create(Objects.requireNonNull(Utils.location("distillation:blood_distillation")), b -> b
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