package net.electrisoma.bloodisfuel.foundation.events;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BFluids;
import com.tterrag.registrate.util.entry.RegistryEntry;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.List;


@Mod.EventBusSubscriber(modid = BloodIsFuel.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FluidEvents {
    private static final int BURN_VISCERA = 8000;
    private static final int BURN_BLOOD = Math.round(BURN_VISCERA * 1.5f);
    private static final int BURN_ENRICHED = BURN_VISCERA * 2;
    private static final int BURN_INFUSED = BURN_ENRICHED * 2;

    private static final List<RegistryEntry<ForgeFlowingFluid.Flowing>> ENRICHED_TYPES = List.of(
            BFluids.ENRICHED_BLOOD,
            BFluids.OIL_ENRICHED_BLOOD
    );

    private static final List<RegistryEntry<ForgeFlowingFluid.Flowing>> INFUSED_TYPES = List.of(
            BFluids.DIESEL_INFUSED_BLOOD,
            BFluids.GASOLINE_INFUSED_BLOOD
    );

    @SubscribeEvent
    public static void onFurnaceFuel(FurnaceFuelBurnTimeEvent event) {
        var item = event.getItemStack().getItem();
        if (item == BFluids.VISCERA.get().getBucket()) {
            event.setBurnTime(BURN_VISCERA);
            return;
        }
        if (item == BFluids.BLOOD.get().getBucket()) {
            event.setBurnTime(BURN_BLOOD);
            return;
        }
        if (matchesBucket(item, ENRICHED_TYPES)) {
            event.setBurnTime(BURN_ENRICHED);
            return;
        }
        if (matchesBucket(item, INFUSED_TYPES)) {
            event.setBurnTime(BURN_INFUSED);
        }
    }

    private static boolean matchesBucket(Object item, List<RegistryEntry<ForgeFlowingFluid.Flowing>> fluids) {
        return fluids.stream()
                .anyMatch(entry -> item == entry.get().getBucket());
    }
}
