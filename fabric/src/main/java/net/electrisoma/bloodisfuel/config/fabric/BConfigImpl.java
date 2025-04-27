package net.electrisoma.bloodisfuel.config.fabric;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.config.BConfig;

import net.createmod.catnip.config.ConfigBase;

import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;

import net.minecraftforge.fml.config.ModConfig;

import java.util.Map;


public class BConfigImpl {

    public static void register() {
        BConfig.registerCommon();

        for (Map.Entry<ModConfig.Type, ConfigBase> pair : BConfig.CONFIGS.entrySet())
            ForgeConfigRegistry.INSTANCE.register(BloodIsFuel.MOD_ID, pair.getKey(), pair.getValue().specification);

        ModConfigEvents.loading(BloodIsFuel.MOD_ID).register(BConfig::onLoad);
        ModConfigEvents.reloading(BloodIsFuel.MOD_ID).register(BConfig::onReload);
    }
}