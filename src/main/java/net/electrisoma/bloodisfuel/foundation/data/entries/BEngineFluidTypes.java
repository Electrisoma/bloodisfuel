package net.electrisoma.bloodisfuel.foundation.data.entries;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BFluids;
import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.api.equipment.engine.EngineFluidType;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;


@SuppressWarnings("unused")
public class BEngineFluidTypes {
    public static final ResourceKey<EngineFluidType> FALLBACK =
            ResourceKey.create(BRegistries.ENGINE_FLUIDS, BloodIsFuel.path("fallback"));

    public static void bootstrap(BootstapContext<EngineFluidType> ctx) {
        var fluidLookup = (HolderLookup.RegistryLookup<Fluid>) ctx.lookup(Registries.FLUID);

        register(ctx, "fallback", new EngineFluidType.Builder()
                .fluidTag("forge", "diesel", fluidLookup)
                .stats(96.0F, 2048F, 1F)
                .build());
        register(ctx, "diesel", new EngineFluidType.Builder()
                .fluidTag("forge", "diesel", fluidLookup)
                .stats(96.0F, 2048F, 1F)
                .build());
        register(ctx, "gasoline", new EngineFluidType.Builder()
                .fluidTag("forge", "gasoline", fluidLookup)
                .stats(96.0F, 2048F, 1F)
                .build());
        register(ctx, "viscera", new EngineFluidType.Builder()
                .fluids(BFluids.VISCERA.getSource())
                .stats(96.0F, 1024F, 10F)
                .build());
        register(ctx, "blood", new EngineFluidType.Builder()
                .fluids(BFluids.BLOOD.getSource())
                .stats(96.0F, 2048F, 10F)
                .build());
        register(ctx, "enriched_blood", new EngineFluidType.Builder()
                .fluids(BFluids.ENRICHED_BLOOD.getSource())
                .stats(96.0F, 3072F, 10F)
                .build());
        register(ctx, "oil_enriched_blood", new EngineFluidType.Builder()
                .fluids(BFluids.OIL_ENRICHED_BLOOD.getSource())
                .stats(96.0F, 4096F, 10F)
                .build());
        register(ctx, "diesel_infused_blood", new EngineFluidType.Builder()
                .fluids(BFluids.DIESEL_INFUSED_BLOOD.getSource())
                .stats(96.0F, 4096F, 10F)
                .build());
        register(ctx, "gasoline_infused_blood", new EngineFluidType.Builder()
                .fluids(BFluids.GASOLINE_INFUSED_BLOOD.getSource())
                .stats(96.0F, 4096F, 10F)
                .build());
    }

    private static void register(BootstapContext<EngineFluidType> ctx, String name, EngineFluidType type) {
        ctx.register(ResourceKey.create(BRegistries.ENGINE_FLUIDS, BloodIsFuel.path(name)), type);
    }
}
