package net.electrisoma.bloodisfuel.fabric;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BFluids;
import net.electrisoma.bloodisfuel.config.fabric.BConfigImpl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import static net.electrisoma.bloodisfuel.base.utils.fuelBurnTimes.*;


@SuppressWarnings({"unused"})
public class BloodIsFuelImpl implements ModInitializer {

    @Override
    public void onInitialize() {
        BloodIsFuel.init();
        BConfigImpl.register();

        furnaceFuel();
        onServerStarting();
    }

    // Finds the version for the fabric file
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
        BloodIsFuel.postRegistrationInit();
    }

    public void onServerStarting(){
        ServerLifecycleEvents.SERVER_STARTED.register(server ->
                BloodIsFuel.LOGGER.info(BloodIsFuel.SERVER_START)
        );
    }

    public static void furnaceFuel(){

        FuelRegistry.INSTANCE.add(BFluids.VISCERA.get().getBucket(), VISCERA);

        FuelRegistry.INSTANCE.add(BFluids.BLOOD.get().getBucket(), BLOOD);

        FuelRegistry.INSTANCE.add(BFluids.ENRICHED_BLOOD.get().getBucket(), ENRICHED_TYPES);
        FuelRegistry.INSTANCE.add(BFluids.OIL_ENRICHED_BLOOD.get().getBucket(), ENRICHED_TYPES);

        FuelRegistry.INSTANCE.add(BFluids.DIESEL_INFUSED_BLOOD.get().getBucket(), INFUSED_TYPES);
        FuelRegistry.INSTANCE.add(BFluids.GASOLINE_INFUSED_BLOOD.get().getBucket(), INFUSED_TYPES);
    }
}