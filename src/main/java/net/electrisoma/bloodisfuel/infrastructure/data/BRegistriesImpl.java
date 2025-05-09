package net.electrisoma.bloodisfuel.infrastructure.data;

import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.registry.items.syringe_blade.SyringeFluidType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(bus = Bus.MOD)
public class BRegistriesImpl {
    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                BRegistries.SYRINGE_BLADE_FLUIDS,
                SyringeFluidType.CODEC,
                SyringeFluidType.CODEC
        );
    }
}
