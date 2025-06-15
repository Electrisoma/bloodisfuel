package net.electrisoma.bloodisfuel.compat.jei;

import mezz.jei.api.recipe.RecipeType;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public record BloodExtractorInfo(List<FluidStack> fluidOutputs, HolderSet<EntityType<?>> matchingMobs) {
    public static final RecipeType<BloodExtractorInfo> TYPE =
            RecipeType.create("bloodisfuel", "blood_extractor", BloodExtractorInfo.class);
}
