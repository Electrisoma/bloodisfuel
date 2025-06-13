package net.electrisoma.bloodisfuel.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.minecraft.core.Registry;
import net.minecraft.server.MinecraftServer;


public class BloodIsFuelKubeJS extends KubeJSPlugin {
    public static EventGroup GROUP = EventGroup.of("BloodIsFuelEvents");
    public static EventHandler SYRINGE_FLUIDS = GROUP.server("syringeFluidTypes", () -> SyringeFluidTypeEventJS.class);

    @Override
    public void registerEvents() {
        GROUP.register();
    }

    public static void addSyringeFluids(Registry<SyringeFluidType> registry) {
        SyringeFluidTypeEventJS event = new SyringeFluidTypeEventJS();
        SYRINGE_FLUIDS.post(event);

        event.getTypes().forEach((id, syringeFluidType) -> {
            if (!registry.containsKey(id)) {
                Registry.register(registry, id, syringeFluidType);
            } else {
                BloodIsFuel.LOGGER.warn("Syringe fluid {} already registered, skipping", id);
            }
        });
    }
}
