package net.electrisoma.bloodisfuel.registry.fabric.fluids_utils;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;

import net.minecraft.resources.ResourceLocation;


public interface RenderHandlerFactory {

    FluidRenderHandler create(ResourceLocation stillTexture, ResourceLocation flowingTexture);
}
