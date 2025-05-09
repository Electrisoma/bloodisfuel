package net.electrisoma.bloodisfuel.api.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.items.syringe_blade.SyringeFluidType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class BRegistries {

    public static final ResourceKey<Registry<SyringeFluidType>> SYRINGE_BLADE_FLUIDS =
            key("syringe_blade_fluids");

    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(BloodIsFuel.asResource(name));
    }
}
