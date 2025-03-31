package net.electrisoma.bloodisfuel.forge;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.forge.BModTabImpl;

import net.minecraftforge.common.util.MavenVersionStringHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.List;


@Mod(BloodIsFuel.MOD_ID)
@Mod.EventBusSubscriber
public class BloodIsFuelImpl {
    static IEventBus eventBus;

    public BloodIsFuelImpl() {
        eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BModTabImpl.register(eventBus);
        //BFluidsForge.register();

        BloodIsFuel.init();

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

    public static void finalizeRegistrate() {
        BloodIsFuel.registrate().registerEventListeners(eventBus);
    }
}
