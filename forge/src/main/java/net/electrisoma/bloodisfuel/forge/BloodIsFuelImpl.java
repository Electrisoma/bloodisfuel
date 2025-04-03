package net.electrisoma.bloodisfuel.forge;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.config.forge.BConfigImpl;
import net.electrisoma.bloodisfuel.registry.forge.BModTabImpl;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.MavenVersionStringHelper;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.List;


@Mod(BloodIsFuel.MOD_ID)
@Mod.EventBusSubscriber
public class BloodIsFuelImpl {
    static IEventBus eventBus;
    static IEventBus forgeBus;

    public BloodIsFuelImpl() {
        eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        forgeBus = MinecraftForge.EVENT_BUS;

        BModTabImpl.register(eventBus);

        BloodIsFuel.init();

        BConfigImpl.register(ModLoadingContext.get());

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> BClientForge.prepareClient(eventBus, forgeBus));
    }

    public static String findVersion() {
        String versionString = "UNKNOWN";

        List<IModInfo> infoList = ModList.get().getModFileById(BloodIsFuel.MOD_ID).getMods();
        if (infoList.size() > 1) {
            BloodIsFuel.LOGGER.error("Multiple mods for MOD_ID: " + BloodIsFuel.MOD_ID);
        }
        for (IModInfo info : infoList) {
            if (info.getModId().equals(BloodIsFuel.MOD_ID)) {
                versionString = MavenVersionStringHelper.artifactVersionToString(info.getVersion());
                break;
            }
        }
        return versionString;
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        BloodIsFuel.LOGGER.info(BloodIsFuel.SERVER_START);
    }

    public static void finalizeRegistrate() {
        BloodIsFuel.registrate().registerEventListeners(eventBus);
    }
}
