package net.electrisoma.bloodisfuel.multiloader.fabric;

import net.electrisoma.bloodisfuel.registry.fluid_utils.FluidBuilder;
import net.electrisoma.bloodisfuel.registry.fluid_utils.BFlowingFluid;
import net.electrisoma.bloodisfuel.registry.fabric.fluids_utils.FluidBuilderImpl;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Supplier;


public class RegistryPlatformImpl {

    // fluid builder
    public static <T extends BFlowingFluid, P> FluidBuilder<T, P>
    createFluidBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
                       ResourceLocation stillTexture, ResourceLocation flowingTexture,
                       NonNullFunction<BFlowingFluid.Properties, T> factory) {
        return new FluidBuilderImpl<>(owner, parent, name, callback, stillTexture, flowingTexture, factory);
    }

    public static <T extends BFlowingFluid, P> FluidBuilder<T, P>
    doFluidBuilderTransforms(FluidBuilder<T, P> builder) {
        FluidBuilderImpl<T, P> builderc = (FluidBuilderImpl<T, P>) builder;
        builderc.handleClientStuff();
        return builderc;
    }

    // recipe registration
//    public static Supplier<RecipeSerializer<?>>
//    registerRecipeSerializer(ResourceLocation id, NonNullSupplier<RecipeSerializer<?>> sup) {
//        RecipeSerializer<?> ret = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, sup.get());
//        return () -> ret;
//    }
//
//    public static void registerRecipeType(ResourceLocation id, Supplier<RecipeType<?>> type) {
//        Registry.register(BuiltInRegistries.RECIPE_TYPE, id, type.get());
//    }
}
