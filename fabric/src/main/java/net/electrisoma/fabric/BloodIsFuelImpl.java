package net.electrisoma.fabric;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.fabric.config.BConfigsImpl;
import net.fabricmc.api.ModInitializer;


public class BloodIsFuelImpl implements ModInitializer {
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void onInitialize() {
        BloodIsFuel.init();
        BConfigsImpl.register();
    }

    public static void finalizeRegistrate() {
        BloodIsFuel.registrate().register();
    }
}
