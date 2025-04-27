package net.electrisoma.bloodisfuel.content.kinetics.juicer;

import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;

import io.github.fabricators_of_create.porting_lib.transfer.item.RecipeWrapper;


public abstract class AbstractJuicingRecipe extends ProcessingRecipe<RecipeWrapper> {

    public AbstractJuicingRecipe(IRecipeTypeInfo recipeType, ProcessingRecipeParams params) {
        super(recipeType, params);
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }
}
