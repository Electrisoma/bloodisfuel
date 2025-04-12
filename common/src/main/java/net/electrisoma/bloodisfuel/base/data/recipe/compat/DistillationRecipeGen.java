//package net.electrisoma.bloodisfuel.base.data.recipe.compat;
//
//import com.simibubi.create.content.processing.recipe.HeatCondition;
//import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
//import net.electrisoma.bloodisfuel.base.data.recipe.BRecipeProvider;
//import net.electrisoma.bloodisfuel.registry.BFluids;
//import net.electrisoma.bloodisfuel.registry.BTags;
//import net.minecraft.data.PackOutput;
//
//import static net.electrisoma.bloodisfuel.registry.fluid_utils.FluidBuilder.create;
//
//public class DistillationRecipeGen extends CompatRecipeGen {
//    GeneratedRecipe
//            BLOOD_DISTILLATION =
//            create("blood_distillation", b -> b
//                    .require(BFluids.OIL_ENRICHED_BLOOD.get(), 100)
//                    .requiresHeat(HeatCondition.SUPERHEATED)
//                    .duration(100)
//                    .output(BFluids.DIESEL_INFUSED_BLOOD.get(),50)
//                    .output(BFluids.GASOLINE_INFUSED_BLOOD.get(), 50)
//                    );
//
//
//    public DistillationRecipeGen(PackOutput pOutput) {
//        super(pOutput);
//    }
//
//    protected String getRecipeType() {
//        return "createdieselgenerators:distillation";
//    }
//}
