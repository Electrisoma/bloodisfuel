package net.electrisoma.bloodisfuel.foundation.data.recipes;

import net.electrisoma.bloodisfuel.registry.fluids.BFluids;
import net.electrisoma.bloodisfuel.registry.items.BItems;
import net.electrisoma.bloodisfuel.registry.BTags;

import com.simibubi.create.AllRecipeTypes;

import net.minecraft.data.PackOutput;


@SuppressWarnings("unused")
public class CompactingRecipeGen extends BProcessingRecipeGen {

    GeneratedRecipe

            VISCERA_FROM_MEATS =
            create("viscera_from_meats", b -> b
                    .require(BTags.BItemTags.MEATS.tag)
                    .output(BFluids.VISCERA.get(), 100)
                    .output(.4f, BItems.DRAINED_MEAT)),

            VISCERA_FROM_FISHES =
            create("viscera_from_fishes", b -> b
                    .require(BTags.BItemTags.FISHES.tag)
                    .output(BFluids.VISCERA.get(), 25));

    public CompactingRecipeGen(PackOutput generator) {
        super(generator);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {return AllRecipeTypes.COMPACTING;}
}