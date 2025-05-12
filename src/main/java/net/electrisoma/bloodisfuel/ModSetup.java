package net.electrisoma.bloodisfuel;

import net.electrisoma.bloodisfuel.registry.*;
import net.electrisoma.bloodisfuel.config.BConfigs;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


public class ModSetup {

    static ModLoadingContext modLoadingContext = ModLoadingContext.get();
    static IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    public static void register() {

        BModTabs.register(modEventBus);
        BParticles.register(modEventBus);

        BConfigs.register(modLoadingContext);

        //BBlocks.register();
        BEntityTypes.register();
        BItems.register();
        BFluids.register();
        BTags.register();
        BEnchantments.register();
    }
}
