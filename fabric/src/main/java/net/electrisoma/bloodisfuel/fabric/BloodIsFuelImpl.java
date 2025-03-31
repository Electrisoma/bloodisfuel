package net.electrisoma.bloodisfuel.fabric;

import net.electrisoma.bloodisfuel.BloodIsFuel;

//import net.electrisoma.bloodisfuel.registry.fabric.BFluidsImpl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;


public class BloodIsFuelImpl implements ModInitializer {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void onInitialize() {

        BloodIsFuel.init();
        //BFluidsImpl.register();

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
}