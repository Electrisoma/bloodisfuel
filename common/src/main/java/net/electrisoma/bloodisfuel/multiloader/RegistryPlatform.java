package net.electrisoma.bloodisfuel.multiloader;

import net.electrisoma.bloodisfuel.registry.fluid_utils.FluidBuilder;
import net.electrisoma.bloodisfuel.registry.fluid_utils.BFlowingFluid;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.minecraft.resources.ResourceLocation;

import dev.architectury.injectables.annotations.ExpectPlatform;


public class RegistryPlatform {

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
}