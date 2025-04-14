package net.electrisoma.bloodisfuel.fabric.mixin_interfaces;

import net.electrisoma.bloodisfuel.registry.fabric.fluids_utils.RenderHandlerFactory;

import net.minecraft.client.renderer.RenderType;

import java.util.function.Supplier;


@SuppressWarnings("all")
public interface FabricFluidBuilderClient {

    FabricFluidBuilderClient layer(Supplier<Supplier<RenderType>> layer);
    FabricFluidBuilderClient renderHandler(Supplier<RenderHandlerFactory> handler);
    FabricFluidBuilderClient color(int color);
}