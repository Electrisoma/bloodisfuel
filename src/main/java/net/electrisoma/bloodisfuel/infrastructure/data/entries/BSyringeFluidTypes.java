package net.electrisoma.bloodisfuel.infrastructure.data.entries;

import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.registry.BFluids;
import net.electrisoma.bloodisfuel.registry.items.syringe_blade.SyringeFluidType;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.Tags;


public class BSyringeFluidTypes {

    public static final ResourceKey<SyringeFluidType> FALLBACK =
            ResourceKey.create(BRegistries.SYRINGE_BLADE_FLUID_TYPE, BloodIsFuel.asResource("fallback"));

    public static void bootstrap(BootstapContext<SyringeFluidType> ctx) {
        register(ctx, "fallback", new SyringeFluidType.Builder()
                .addFluids()
                .build()
        );

        register(ctx, "viscera", new SyringeFluidType.Builder()
                .addFluids(BFluids.VISCERA.getSource())
                .color(0xDF4416)
                .onEntityHitEffect(new MobEffectInstance(MobEffects.POISON, 500, 1, false, true))
                .addMobs(EntityType.ZOMBIE)
                .build()
        );

        register(ctx, "blood", new SyringeFluidType.Builder()
                .addFluids(BFluids.BLOOD.getSource())
                .color(0xF10B0B)
                .onEntityHitEffect(new MobEffectInstance(MobEffects.WEAKNESS, 500, 1, false, true))
                .build()
        );

        register(ctx, "enriched_blood", new SyringeFluidType.Builder()
                .addFluids(BFluids.ENRICHED_BLOOD.getSource())
                .color(0xE11313)
                .onEntityHitEffect(new MobEffectInstance(MobEffects.WEAKNESS, 500, 1, false, true))
                .build()
        );

        register(ctx, "alexs_caves_juice", new SyringeFluidType.Builder()
                .addFluids(ACFluidRegistry.ACID_FLUID_SOURCE.get(), ACFluidRegistry.PURPLE_SODA_FLUID_SOURCE.get())
                .color(0x7CFC00)
                .onEntityHitEffect(new MobEffectInstance(MobEffects.GLOWING, 300, 4, false, true))
                .build()
        );
    }

    private static void register(BootstapContext<SyringeFluidType> ctx, String name, SyringeFluidType type) {
        ctx.register(ResourceKey.create(BRegistries.SYRINGE_BLADE_FLUID_TYPE, BloodIsFuel.asResource(name)), type);
    }
}
