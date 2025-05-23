package net.electrisoma.bloodisfuel.infrastructure.data.entries;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.api.engine.EngineFluidType;
import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.registry.BFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;

public class BEngineFluidTypes {
    public static final ResourceKey<EngineFluidType> FALLBACK =
            ResourceKey.create(BRegistries.ENGINE_FLUIDS, BloodIsFuel.asResource("fallback"));

    public static void bootstrap(BootstapContext<EngineFluidType> ctx) {
        var fluidLookup = (HolderLookup.RegistryLookup<Fluid>) ctx.lookup(Registries.FLUID);

        register(ctx, "fallback", new EngineFluidType.Builder()
                .fluidTag("forge", "diesel", fluidLookup)
                .stats(96.0F, 6144.0F, 1F)
                .build()
        );
        register(ctx, "blood", new EngineFluidType.Builder()
                .fluids(BFluids.BLOOD.getSource())
                .stats(96.0F, 6144.0F, 10F)
                .build()
        );
    }

    private static void register(BootstapContext<EngineFluidType> ctx, String name, EngineFluidType type) {
        ctx.register(ResourceKey.create(BRegistries.ENGINE_FLUIDS, BloodIsFuel.asResource(name)), type);
    }
}
