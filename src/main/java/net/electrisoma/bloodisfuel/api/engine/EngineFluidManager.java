package net.electrisoma.bloodisfuel.api.engine;

import net.electrisoma.bloodisfuel.api.registry.BRegistries;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class EngineFluidManager {
    public static final EngineFluidType EMPTY = new EngineFluidType.Builder()
            .stats(0.0f, 0.0f, 0.0f)
            .build();

    private static final Map<ResourceLocation, EngineFluidType> CACHE = new ConcurrentHashMap<>();

    /**
     * Gets the EngineFluidType for a given FluidStack.
     */
    public static EngineFluidType fromFluid(FluidStack stack, RegistryAccess access) {
        if (stack.isEmpty()) return EMPTY;
        Fluid fluid = stack.getFluid();
        ResourceLocation key = ForgeRegistries.FLUIDS.getKey(fluid);
        if (key == null) return EMPTY;
        if (CACHE.containsKey(key)) return CACHE.get(key);

        //noinspection deprecation
        Holder<Fluid> fluidHolder = fluid.builtInRegistryHolder();
        Registry<EngineFluidType> registry = access.registryOrThrow(BRegistries.ENGINE_FLUIDS);
        for (EngineFluidType type : registry) {
            for (HolderSet<Fluid> set : type.fluids()) {
                if (set.contains(fluidHolder)) {
                    CACHE.put(key, type);
                    return type;
                }
            }
        }
        CACHE.put(key, EMPTY);
        return EMPTY;
    }

    /**
     * Gets speed modifier for fuel.
     */
    public static float getSpeed(FluidStack stack, RegistryAccess access) {
        return fromFluid(stack, access).speed();
    }

    /**
     * Gets strength modifier for fuel.
     */
    public static float getStrength(FluidStack stack, RegistryAccess access) {
        return fromFluid(stack, access).strength();
    }

    /**
     * Gets burn rate for fuel.
     */
    public static float getBurnRate(FluidStack stack, RegistryAccess access) {
        return fromFluid(stack, access).burnRate();
    }

    /**
     * Clears the internal cache, e.g., when datapacks reload.
     */
    public static void clearCache() {
        CACHE.clear();
    }

    /**
     * Retrieves a default fluid from an EngineFluidType.
     */
    public static Fluid getFluidFor(EngineFluidType type) {
        return type.fluids().stream()
                .flatMap(HolderSet::stream)
                .map(Holder::value)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns all engine fluid types.
     */
    public static List<EngineFluidType> getAll(RegistryAccess access) {
        Registry<EngineFluidType> registry = access.registryOrThrow(BRegistries.ENGINE_FLUIDS);
        return registry.stream().toList();
    }
}
