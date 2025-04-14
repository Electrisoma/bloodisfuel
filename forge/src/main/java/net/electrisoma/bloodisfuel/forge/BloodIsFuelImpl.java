package net.electrisoma.bloodisfuel.forge;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.config.forge.BConfigImpl;
import net.electrisoma.bloodisfuel.registry.forge.BModTabImpl;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.common.util.MavenVersionStringHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;


@SuppressWarnings("all")
@Mod(BloodIsFuel.MOD_ID)
public class BloodIsFuelImpl {

    static IEventBus eventBus;
    static IEventBus forgeBus;

    public BloodIsFuelImpl() {
        eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        forgeBus = MinecraftForge.EVENT_BUS;

        BloodIsFuel.init();

        BModTabImpl.register(eventBus);
        BConfigImpl.register(ModLoadingContext.get());

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> BClientForge::new);

        forgeBus.addListener(this::onServerStarting);
    }

    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(BloodIsFuel::postRegistrationInit);
    }

    // Finds the version for the forge file
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

    public static void finalizeRegistrate() {
        BloodIsFuel.registrate().registerEventListeners(eventBus);
    }

    public void onServerStarting(ServerStartedEvent event) {
        BloodIsFuel.LOGGER.info(BloodIsFuel.SERVER_START);
    }
}
