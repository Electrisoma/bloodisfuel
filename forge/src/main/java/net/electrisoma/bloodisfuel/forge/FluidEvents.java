package net.electrisoma.bloodisfuel.forge;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BFluids;
import net.electrisoma.bloodisfuel.registry.fluid_utils.BFlowingFluid;

import com.tterrag.registrate.util.entry.RegistryEntry;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;

import java.util.Arrays;

import static net.electrisoma.bloodisfuel.base.utils.fuelBurnTimes.*;


@Mod.EventBusSubscriber(modid = BloodIsFuel.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FluidEvents {

    @SubscribeEvent
    static void onFurnaceFuel(FurnaceFuelBurnTimeEvent event) {

        if (event.getItemStack().getItem() ==
                BFluids.VISCERA.get().getBucket()) {
            event.setBurnTime(VISCERA);
        }

        if (event.getItemStack().getItem() ==
                BFluids.BLOOD.get().getBucket()) {
            event.setBurnTime(BLOOD);
        }

        for (RegistryEntry<BFlowingFluid.Flowing> enrichedTypesEntry :
                Arrays.asList(
                        BFluids.ENRICHED_BLOOD,
                        BFluids.OIL_ENRICHED_BLOOD)) {
            if (event.getItemStack().getItem() ==
                    enrichedTypesEntry.get().getBucket()) {
                event.setBurnTime(ENRICHED_TYPES);}
        }

        for (RegistryEntry<BFlowingFluid.Flowing> infusedTypesEntry :
                Arrays.asList(
                        BFluids.DIESEL_INFUSED_BLOOD,
                        BFluids.GASOLINE_INFUSED_BLOOD)) {
            if (event.getItemStack().getItem() ==
                    infusedTypesEntry.get().getBucket()) {
                event.setBurnTime(INFUSED_TYPES);}
        }
    }

}
