package net.electrisoma.bloodisfuel.api.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.items.syringe_blade.SyringeFluidType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class BRegistries {

    public static final ResourceKey<Registry<SyringeFluidType>> SYRINGE_BLADE_FLUID_TYPE =
            key("syringe_blade_fluid/type");


    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(BloodIsFuel.asResource(name));
    }
}
