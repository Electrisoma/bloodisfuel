package net.electrisoma.bloodisfuel.forge;

import net.electrisoma.bloodisfuel.BClientCommon;

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
        if (!event.isCancelable())
            return;
        float density = BClientCommon.getFogDensity(event.getCamera(), event.getFarPlaneDistance());
        if (density != -1) {
            event.setFarPlaneDistance(density);
            event.setNearPlaneDistance(density);
            event.setCanceled(true);
        }
    }
}
