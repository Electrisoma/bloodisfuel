package net.electrisoma.bloodisfuel.forge;

import com.mojang.blaze3d.shaders.FogShape;
import net.electrisoma.bloodisfuel.BClientCommon;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.client.event.ViewportEvent;


@SuppressWarnings("unused")
public class BClientForge {

    public static void prepareClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        forgeEventBus.addListener(BClientForge::getFogColor);
        forgeEventBus.addListener(BClientForge::getFogDensity);
    }

    public static void getFogColor(ViewportEvent.ComputeFogColor event) {
        BClientCommon.setFogColor(event.getCamera(), (r, g, b) -> {
            event.setRed(r);
            event.setGreen(g);
            event.setBlue(b);
        });
    }

    public static void getFogDensity(ViewportEvent.RenderFog event) {
        if (!event.isCancelable()) return;

        Fluid fluid = BClientCommon.getCamera(event.getCamera());

        float density = BClientCommon.getFogDensity(event.getCamera(), event.getFarPlaneDistance());

        if (!BClientCommon.isCustomFluid(fluid)) return;

        if (density != -1f) {
            event.setCanceled(true);
            event.setNearPlaneDistance(0.0F);
            event.setFarPlaneDistance(density);
            event.setFogShape(FogShape.CYLINDER);
        }
    }
}