package net.electrisoma.bloodisfuel.infrastructure.data.recipes;

import net.electrisoma.bloodisfuel.registry.fluids.BIF_Fluids;
import net.electrisoma.bloodisfuel.registry.items.BIF_Items;
import net.electrisoma.bloodisfuel.registry.BIF_Tags;

import com.simibubi.create.AllRecipeTypes;

import net.minecraft.data.PackOutput;


@SuppressWarnings("unused")
public class EmptyingRecipeGen extends BloodProcessingRecipeGen{

    GeneratedRecipe

            VISCERA_FROM_MEATS =
            create("viscera_from_meats", b -> b
                    .require(BIF_Tags.AllItemTags.MEATS.tag)
                    .output(.4f, BIF_Items.DRAINED_MEAT)
                    .output(BIF_Fluids.VISCERA.get(), 100)),

            VISCERA_FROM_FISHES =
            create("viscera_from_fishes", b -> b
                    .require(BIF_Tags.AllItemTags.FISHES.tag)
                    .output(.4f, BIF_Items.DRAINED_MEAT)
                    .output(BIF_Fluids.VISCERA.get(), 25));

    public EmptyingRecipeGen(PackOutput generator) {
        super(generator);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.EMPTYING;
    }
}