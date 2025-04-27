//package net.electrisoma.bloodisfuel.content.kinetics.juicer;
//
//import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
//import net.electrisoma.bloodisfuel.registry.BRecipeTypes;
//
//import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
//
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.level.Level;
//
//import javax.annotation.ParametersAreNonnullByDefault;
//
//
//@ParametersAreNonnullByDefault
//public class JuicingRecipe extends ProcessingRecipe<Inventory> {
//
//    public JuicingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
//        super(BRecipeTypes.JUICING, params);
//    }
//
//    @Override
//    protected int getMaxInputCount() {
//        return 0;
//    }
//
//    @Override
//    protected int getMaxOutputCount() {
//        return 0;
//    }
//
//    @Override
//    protected int getMaxFluidInputCount() {
//        return 1;
//    }
//
//    @Override
//    protected int getMaxFluidOutputCount() {
//        return 1;
//    }
//
//    @Override
//    public boolean matches(Inventory container, Level level) {
//        return false;
//    }
//}