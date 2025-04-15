package net.electrisoma.bloodisfuel.multiloader.forge;

import net.electrisoma.bloodisfuel.forge.BloodIsFuelImpl;
import net.electrisoma.bloodisfuel.registry.fluid_utils.FluidBuilder;
import net.electrisoma.bloodisfuel.registry.fluid_utils.BFlowingFluid;
import net.electrisoma.bloodisfuel.registry.forge.fluid_utils.FluidBuilderImpl;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Supplier;


public class RegistryPlatformImpl {


    // fluid builder
    public static <T extends BFlowingFluid, P> FluidBuilder<T, P>
    createFluidBuilder(AbstractRegistrate<?> owner, P parent,
                       String name, BuilderCallback callback,
                       ResourceLocation stillTexture, ResourceLocation flowingTexture,
                       NonNullFunction<BFlowingFluid.Properties, T> factory) {
        return new FluidBuilderImpl<>(owner, parent, name, callback, stillTexture, flowingTexture, factory);
    }

    public static <T extends BFlowingFluid, P> FluidBuilder<T, P>
    doFluidBuilderTransforms(FluidBuilder<T, P> builder) {
        return builder;
    }

    // recipe registration
//    public static Supplier<RecipeSerializer<?>>
//    registerRecipeSerializer(ResourceLocation id, NonNullSupplier<RecipeSerializer<?>> sup) {
//        return BloodIsFuelImpl.RECIPE_SERIALIZER_REGISTER.register(id.getPath(), sup);
//    }
//
//    public static void registerRecipeType(ResourceLocation id, Supplier<RecipeType<?>> type) {
//        BloodIsFuelImpl.RECIPE_TYPE_REGISTER.register(id.getPath(), type);
//    }
}
