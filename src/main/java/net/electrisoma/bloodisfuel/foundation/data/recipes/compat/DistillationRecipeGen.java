//package net.electrisoma.bloodisfuel.foundation.data.recipes.compat;
//
//import com.jesz.createdieselgenerators.CDGRecipes; //boo, i dont want any mod dependencies :(
//
//import net.electrisoma.bloodisfuel.foundation.data.recipes.BProcessingRecipeGen;
//import net.electrisoma.bloodisfuel.infrastructure.utils.Mods;
//import net.electrisoma.bloodisfuel.infrastructure.utils.Utils;
//import net.electrisoma.bloodisfuel.registry.fluids.BFluids;
//
//
//import com.simibubi.create.content.processing.recipe.HeatCondition;
//import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
//
//import net.minecraft.data.PackOutput;
//
//import java.util.Objects;
//
//
////weird as shit, idk lol
//@SuppressWarnings("unused")
//public class DistillationRecipeGen extends BProcessingRecipeGen {
//    GeneratedRecipe
//            BLOOD_DISTILLATION =
//            create(Objects.requireNonNull(Utils.location("bloodisfuel:blood_distillation")), b -> b
//                    .require(BFluids.OIL_ENRICHED_BLOOD.get(), 100)
//                    .requiresHeat(HeatCondition.SUPERHEATED)
//                    .duration(100)
//                    .output(BFluids.DIESEL_INFUSED_BLOOD.get(),50)
//                    .output(BFluids.GASOLINE_INFUSED_BLOOD.get(), 50)
//                    .whenModLoaded(Mods.CDG.getId()))
//            ;
//
//
//    public DistillationRecipeGen(PackOutput generator) {
//        super(generator);
//    }
//
//    @Override
//    protected IRecipeTypeInfo getRecipeType() {
//        return CDGRecipes.DISTILLATION;
//    }
//}