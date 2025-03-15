package net.electrisoma.fabric.config;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.config.BConfigs;
import com.simibubi.create.foundation.config.ConfigBase;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
import net.minecraftforge.fml.config.ModConfig;
import java.util.Map;


public class BConfigsImpl {
    public static void register() {
        BConfigs.registerCommon();

        for (Map.Entry<ModConfig.Type, ConfigBase> pair : BConfigs.CONFIGS.entrySet())
            ForgeConfigRegistry.INSTANCE.register(BloodIsFuel.MOD_ID, pair.getKey(), pair.getValue().specification);

        ModConfigEvents.loading(BloodIsFuel.MOD_ID).register(BConfigs::onLoad);
        ModConfigEvents.reloading(BloodIsFuel.MOD_ID).register(BConfigs::onReload);
    }
}
