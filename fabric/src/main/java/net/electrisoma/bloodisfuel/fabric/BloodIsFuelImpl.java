package net.electrisoma.bloodisfuel.fabric;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.config.fabric.BConfigImpl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;


public class BloodIsFuelImpl implements ModInitializer {

    @SuppressWarnings({"unused"})
    @Override
    public void onInitialize() {
        BloodIsFuel.init();
        BConfigImpl.register();

    }

    public static String findVersion() {
        return FabricLoader.getInstance()
                .getModContainer(BloodIsFuel.MOD_ID)
                .orElseThrow()
                .getMetadata()
                .getVersion()
                .getFriendlyString();
    }

    public static void finalizeRegistrate() {
        BloodIsFuel.registrate().register();
    }

    public static void onServerStarting(){
        ServerLifecycleEvents.SERVER_STARTED.register(server ->
                BloodIsFuel.LOGGER.info(BloodIsFuel.SERVER_START)
        );
    }
}