package net.electrisoma.bloodisfuel.infrastructure.data.entries;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.registry.BFluids;
import net.electrisoma.bloodisfuel.registry.items.syringe_blade.SyringeFluidType;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;


public class BSyringeFluidTypes {

    public static final ResourceKey<SyringeFluidType> FALLBACK =
            ResourceKey.create(BRegistries.SYRINGE_BLADE_FLUID_TYPE, BloodIsFuel.asResource("fallback"));

    public static void bootstrap(BootstapContext<SyringeFluidType> ctx) {
        register(ctx, "fallback", new SyringeFluidType.Builder()
                .addFluids()
                .build()
        );

        register(ctx, "blood", new SyringeFluidType.Builder()
                .addFluids(BFluids.BLOOD.get())
                .color(0xAA0000)
                .onEntityHitEffect(new MobEffectInstance(MobEffects.WEAKNESS, 160, 1))
                .build()
        );
    }


    private static void register(BootstapContext<SyringeFluidType> ctx, String name, SyringeFluidType type) {
        ctx.register(ResourceKey.create(BRegistries.SYRINGE_BLADE_FLUID_TYPE, BloodIsFuel.asResource(name)), type);
    }
}
