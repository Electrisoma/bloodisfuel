package net.electrisoma.bloodisfuel.forge;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.forge.config.BConfigsImpl;
import net.electrisoma.bloodisfuel.forge.registry.BCreativeTabsImpl;
import net.electrisoma.bloodisfuel.infrastructure.utils.Env;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(BloodIsFuel.MOD_ID)
@Mod.EventBusSubscriber
public class BloodIsFuelImpl {
    static IEventBus bus;

    public BloodIsFuelImpl() {
        bus = FMLJavaModLoadingContext.get().getModEventBus();
        BCreativeTabsImpl.register(BloodIsFuelImpl.bus);
        BloodIsFuel.init();

        BConfigsImpl.register(ModLoadingContext.get());
    }
}
