package net.electrisoma.bloodisfuel.registry.fluids;

import net.electrisoma.bloodisfuel.BloodIsFuel;

import com.tterrag.registrate.util.entry.FluidEntry;

import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;


@SuppressWarnings("all")
@Mod.EventBusSubscriber(modid = BloodIsFuel.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FluidEvents {
    @SubscribeEvent
    static void onFurnaceFuel(FurnaceFuelBurnTimeEvent event) {
        for (FluidEntry<ForgeFlowingFluid.Flowing> flowingFluidEntry :
                Arrays.asList(
                        BFluids.VISCERA,
                        BFluids.BLOOD,
                        BFluids.ENRICHED_BLOOD,
                        BFluids.OIL_ENRICHED_BLOOD,
                        BFluids.DIESEL_INFUSED_BLOOD,
                        BFluids.GASOLINE_INFUSED_BLOOD)) {
            if (event.getItemStack().getItem() ==
                    flowingFluidEntry.getBucket().get()) {
                event.setBurnTime(30000);}
        }
    }
}