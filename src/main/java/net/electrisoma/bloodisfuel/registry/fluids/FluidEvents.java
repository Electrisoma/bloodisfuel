package net.electrisoma.bloodisfuel.registry.fluids;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.electrisoma.bloodisfuel.BloodIsFuel;

import net.electrisoma.bloodisfuel.registry.BFluids;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;


@SuppressWarnings({"unused","OptionalGetWithoutIsPresent"})
@Mod.EventBusSubscriber(modid = BloodIsFuel.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FluidEvents {

    public static final int VISCERA = 8000;
    public static final int BLOOD = (int) (VISCERA*1.5);
    public static final int ENRICHED_TYPES = VISCERA*2;
    public static final int INFUSED_TYPES = ENRICHED_TYPES*2;

    @SubscribeEvent
    static void onFurnaceFuel(FurnaceFuelBurnTimeEvent event) {

        if (event.getItemStack().getItem() == BFluids.VISCERA.get().getBucket()) {
            event.setBurnTime(VISCERA);
        }

        if (event.getItemStack().getItem() == BFluids.BLOOD.get().getBucket()) {
            event.setBurnTime(BLOOD);
        }

        for (RegistryEntry<ForgeFlowingFluid.Flowing> enrichedTypesEntry :
                Arrays.asList(
                        BFluids.ENRICHED_BLOOD,
                        BFluids.OIL_ENRICHED_BLOOD)) {
            if (event.getItemStack().getItem() == enrichedTypesEntry.get().getBucket()) {
                event.setBurnTime(ENRICHED_TYPES);
            }
        }

        for (RegistryEntry<ForgeFlowingFluid.Flowing> infusedTypesEntry :
                Arrays.asList(
                        BFluids.DIESEL_INFUSED_BLOOD,
                        BFluids.GASOLINE_INFUSED_BLOOD)) {
            if (event.getItemStack().getItem() == infusedTypesEntry.get().getBucket()) {
                event.setBurnTime(INFUSED_TYPES);
            }
        }
    }
}