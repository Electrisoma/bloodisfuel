package net.electrisoma.bloodisfuel;

import net.electrisoma.bloodisfuel.registry.*;
import net.electrisoma.bloodisfuel.config.BConfigs;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


public class ModSetup {
    public static void register(IEventBus modEventBus, ModLoadingContext modLoadingContext) {
        BTags.register();
        BModTabs.register(modEventBus);
        BBlocks.register();
        BItems.register();
        BFluids.register();
        BMenuTypes.register();
        BEntityTypes.register();
        BBlockEntityTypes.register();
        BParticles.register(modEventBus);
        BConfigs.register(modLoadingContext);

        BPackets.registerPackets();
    }
}
