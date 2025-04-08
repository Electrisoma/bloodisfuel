package net.electrisoma.bloodisfuel.base.data.recipe;

import net.electrisoma.bloodisfuel.registry.BTags;
import net.electrisoma.bloodisfuel.registry.BFluids;
import net.electrisoma.bloodisfuel.registry.items.BItems;

import com.simibubi.create.AllRecipeTypes;

import net.minecraft.data.PackOutput;

@SuppressWarnings("unused")
public class EmptyingRecipeGen extends BProcessingRecipeGen {

    GeneratedRecipe

            VISCERA_FROM_MEATS =
            create("viscera_from_meats", b -> b
                    .require(BTags.AllItemTags.MEATS.tag)
                    .output(.4f, BItems.DRAINED_MEAT)
                    .output(BFluids.VISCERA.get(), 100)),

            VISCERA_FROM_FISHES =
            create("viscera_from_fishes", b -> b
                    .require(BTags.AllItemTags.FISHES.tag)
                    .output(.4f, BItems.DRAINED_MEAT)
                    .output(BFluids.VISCERA.get(), 25));

    public EmptyingRecipeGen(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.EMPTYING;
    }
}