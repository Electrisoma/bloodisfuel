package net.electrisoma.bloodisfuel.foundation.data.entries;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BFluids;
import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidType;

import com.simibubi.create.AllFluids;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.data.worldgen.BootstapContext;

import net.minecraftforge.common.Tags;


public class BSyringeFluidTypes {
    public static final ResourceKey<SyringeFluidType> POTION =
            ResourceKey.create(BRegistries.SYRINGE_FLUIDS, BloodIsFuel.asResource("potion"));
    public static final ResourceKey<SyringeFluidType> FALLBACK =
            ResourceKey.create(BRegistries.SYRINGE_FLUIDS, BloodIsFuel.asResource("fallback"));

    public static void bootstrap(BootstapContext<SyringeFluidType> ctx) {
        var fluidLookup = (HolderLookup.RegistryLookup<Fluid>) ctx.lookup(Registries.FLUID);

        register(ctx, "potion", new SyringeFluidType.Builder()
                .fluids(AllFluids.POTION.getSource()) // potion support, the color is overridden later
                .appearance(0xFFFFFF, false, true)
                .build()
        );
        register(ctx, "fallback", new SyringeFluidType.Builder()
                .fluids(BFluids.BLOOD.getSource()) // default = blood
                .appearance(0xF10B0B, true, false)
                .stats(6, -2.5F)
                .effect(MobEffects.WEAKNESS, 500, 1)
                .build()
        );
        register(ctx, "milk", new SyringeFluidType.Builder()
                .fluidTag(Tags.Fluids.MILK.location(), fluidLookup)
                .appearance(0xFFFFFF, true)
                .effect(MobEffects.DAMAGE_BOOST, 100)
                .mobs(EntityType.COW)
                .build()
        );
        register(ctx, "water", new SyringeFluidType.Builder()
                .fluids(Fluids.WATER.getSource())
                .color(0x0C17CD)
                .extinguishing(1, 4F)
                .build()
        );
        register(ctx, "lava", new SyringeFluidType.Builder()
                .fluids(Fluids.LAVA.getSource())
                .appearance(0xFF6A00, true, true)
                .burning(15, 4F)
                .build()
        );
        register(ctx, "honey", new SyringeFluidType.Builder()
                .fluids(AllFluids.HONEY.getSource())
                .appearance(0xFFBF00, true)
                .food(4, 2)
                .mobs(EntityType.BEE)
                .build()
        );
        register(ctx, "chocolate", new SyringeFluidType.Builder()
                .fluids(AllFluids.CHOCOLATE.getSource())
                .appearance(0x895129, true)
                .food(4, 2)
                .build()
        );
        register(ctx, "viscera", new SyringeFluidType.Builder()
                .fluids(BFluids.VISCERA.getSource())
                .appearance(0xDF4416, true)
                .effect(MobEffects.POISON, 500, 1)
                .stats(6, -2.5F)
                .mobs(EntityType.ZOMBIE)
                .build()
        );
        register(ctx, "blood", new SyringeFluidType.Builder()
                .fluids(BFluids.BLOOD.getSource())
                .appearance(0x880000, true)
                .effect(MobEffects.WEAKNESS, 500, 1)
                .stats(6, -2.5F)
                .build()
        );
        register(ctx, "enriched_blood", new SyringeFluidType.Builder()
                .fluids(BFluids.ENRICHED_BLOOD.getSource())
                .appearance(0xE11313, true)
                .effect(MobEffects.WEAKNESS, 500, 1)
                .stats(6, -2.5F)
                .build()
        );
        register(ctx, "boiling_blood", new SyringeFluidType.Builder()
                .fluids(BFluids.BOILING_BLOOD.getSource())
                .appearance(0xE11313, true)
                .stats(6, -2.5F)
                .burning(4, 4F)
                .build()
        );
        register(ctx, "blazing_blood", new SyringeFluidType.Builder()
                .fluidTag("forge", "blazing_blood", fluidLookup)
                .appearance(0xFFAE42, true, true)
                .stats(6, -2.5F)
                .burning(4, 4F)
                .mobs(EntityType.BLAZE)
                .build()
        );
        register(ctx, "acid", new SyringeFluidType.Builder()
                .fluidTag("forge", "acid", fluidLookup)
                .appearance(0x61DE2A, true, true)
                .effect(MobEffects.WITHER, 300, 1)
                .build()
        );
        register(ctx, "purple_soda_from_fishes", new SyringeFluidType.Builder()
                .fluids(ACFluidRegistry.PURPLE_SODA_FLUID_SOURCE.get())
                .color(0x7F00FF)
                .mobs(ACEntityRegistry.SWEETISH_FISH.get())
                .effect(ACEffectRegistry.SUGAR_RUSH.get(), 500, 1)
                .food(4, 0.4F)
                .build()
        );
        register(ctx, "nutrients_fluid", new SyringeFluidType.Builder()
                .fluidTag("biofactory", "nutrients_fluid", fluidLookup)
                .appearance(0x495C22, true)
                .food(9, 1.2F)
                .build()
        );
    }

    private static void register(BootstapContext<SyringeFluidType> ctx, String name, SyringeFluidType type) {
        ctx.register(ResourceKey.create(BRegistries.SYRINGE_FLUIDS, BloodIsFuel.asResource(name)), type);
    }
}