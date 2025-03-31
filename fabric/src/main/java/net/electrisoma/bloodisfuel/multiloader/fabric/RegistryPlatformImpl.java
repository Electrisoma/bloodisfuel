package net.electrisoma.bloodisfuel.multiloader.fabric;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.electrisoma.bloodisfuel.registry.fabric.fluids_utils.FluidBuilderImpl;
import net.minecraft.resources.ResourceLocation;

import net.electrisoma.bloodisfuel.registry.fluid_utils.BFlowingFluid;
import net.electrisoma.bloodisfuel.registry.fluid_utils.FluidBuilder;


public class RegistryPlatformImpl {

    public static <T extends BFlowingFluid, P> FluidBuilder<T, P>
    createFluidBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
                       ResourceLocation stillTexture, ResourceLocation flowingTexture,
                       NonNullFunction<BFlowingFluid.Properties, T> factory) {
        return new FluidBuilderImpl<>(owner, parent, name, callback, stillTexture, flowingTexture, factory);
    }

    public static <T extends BFlowingFluid, P> FluidBuilder<T, P> doFluidBuilderTransforms(FluidBuilder<T, P> builder) {
        FluidBuilderImpl<T, P> builderc = (FluidBuilderImpl<T, P>) builder;
        builderc.handleClientStuff();
        return builderc;
    }
}
