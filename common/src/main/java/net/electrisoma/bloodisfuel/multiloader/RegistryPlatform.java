package net.electrisoma.bloodisfuel.multiloader;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.electrisoma.bloodisfuel.registry.fluid_utils.FluidBuilder;
import net.electrisoma.bloodisfuel.registry.fluid_utils.BFlowingFluid;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeSerializer;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.util.function.Supplier;


public class RegistryPlatform {

    // fluid builder
    @ExpectPlatform
    public static <T extends BFlowingFluid, P> FluidBuilder<T, P>
    createFluidBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
                       ResourceLocation stillTexture, ResourceLocation flowingTexture,
                       NonNullFunction<BFlowingFluid.Properties, T> factory) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends BFlowingFluid, P> FluidBuilder<T, P>
    doFluidBuilderTransforms(FluidBuilder<T, P> builder) {
        throw new AssertionError();
    }

    // recipe registration
//    @ExpectPlatform
//    public static Supplier<RecipeSerializer<?>>
//    registerRecipeSerializer(ResourceLocation id, NonNullSupplier<RecipeSerializer<?>> sup) {
//        throw new AssertionError();
//    }
//
//    @ExpectPlatform
//    public static void registerRecipeType(ResourceLocation id, Supplier<RecipeType<?>> type) {
//        throw new AssertionError();
//    }
}