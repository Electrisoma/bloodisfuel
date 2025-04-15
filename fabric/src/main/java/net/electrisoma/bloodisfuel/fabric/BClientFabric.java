package net.electrisoma.bloodisfuel.fabric;

import net.electrisoma.bloodisfuel.BClientCommon;

import io.github.fabricators_of_create.porting_lib.event.client.FogEvents;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.Camera;


public class BClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        FogEvents.SET_COLOR.register(BClientFabric::setFogColor);
        FogEvents.SET_DENSITY.register(BClientFabric::getFogDensity);
    }

    public static float getFogDensity(Camera Camera, float currentDensity) {
        float density = BClientCommon.getFogDensity(Camera, currentDensity);
        return density == -1 ? currentDensity : density;
    }

    public static void setFogColor(FogEvents.ColorData data, float partialTicks) {
        BClientCommon.setFogColor(data.getCamera(), (r, g, b) -> {
            data.setRed(r);
            data.setGreen(g);
            data.setBlue(b);
        });
    }
}