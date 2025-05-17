package net.electrisoma.bloodisfuel.infrastructure.data.entries;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BFluids;
import net.electrisoma.bloodisfuel.api.data.BurningData;
import net.electrisoma.bloodisfuel.api.data.ExtinguishingData;
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
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.data.worldgen.BootstapContext;

import net.minecraftforge.common.Tags;


public class BSyringeFluidTypes {
    public static final ResourceKey<SyringeFluidType> FALLBACK =
            ResourceKey.create(BRegistries.SYRINGE_BLADE_FLUIDS, BloodIsFuel.asResource("fallback"));

    public static void bootstrap(BootstapContext<SyringeFluidType> ctx) {
        var fluidLookup = (HolderLookup.RegistryLookup<Fluid>) ctx.lookup(Registries.FLUID);

        register(ctx, "fallback", new SyringeFluidType.Builder()
                .fluids(BFluids.BLOOD.getSource()) // default = blood
                .color(0xF10B0B)
                .opaque(true)
                .damage(6)
                .attackSpeed(-2.5F)
                .onEntityHitEffect(new MobEffectInstance(MobEffects.WEAKNESS, 500, 1, false, true))
                .build());
        register(ctx, "milk", new SyringeFluidType.Builder()
                .fluidTag(Tags.Fluids.MILK.location(), fluidLookup)
                .mobs(EntityType.COW)
                .color(0xFFFFFF)
                .opaque(true)
                .onEntityHitEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 0, false, true))
                .build());
        register(ctx, "water", new SyringeFluidType.Builder()
                .fluids(Fluids.WATER.getSource())
                .color(0x0C17CD)
                .extinguishing(new ExtinguishingData(1, 4F))
                .build());
        register(ctx, "lava", new SyringeFluidType.Builder()
                .fluids(Fluids.LAVA.getSource())
                .color(0xFF6A00)
                .opaque(true)
                .glowing(true)
                .burning(new BurningData(15, 4F)).build());
        register(ctx, "honey", new SyringeFluidType.Builder()
                .fluids(AllFluids.HONEY.getSource())
                .mobs(EntityType.BEE)
                .color(0xFFBF00)
                .opaque(true)
                .food(new FoodProperties.Builder()
                        .nutrition(4)
                        .saturationMod(2)
                        .build()).build());
        register(ctx, "chocolate", new SyringeFluidType.Builder()
                .fluids(AllFluids.CHOCOLATE.getSource())
                .color(0x895129)
                .opaque(true)
                .food(new FoodProperties.Builder()
                        .nutrition(4)
                        .saturationMod(2)
                        .build()).build());
        register(ctx, "viscera", new SyringeFluidType.Builder()
                .fluids(BFluids.VISCERA.getSource())
                .mobs(EntityType.ZOMBIE)
                .color(0xDF4416)
                .opaque(true)
                .damage(6)
                .attackSpeed(-2.5F)
                .onEntityHitEffect(new MobEffectInstance(MobEffects.POISON, 500, 1, false, true))
                .build());
        register(ctx, "blood", new SyringeFluidType.Builder()
                .fluids(BFluids.BLOOD.getSource())
                .color(0xF10B0B)
                .opaque(true)
                .damage(6)
                .attackSpeed(-2.5F)
                .onEntityHitEffect(new MobEffectInstance(MobEffects.WEAKNESS, 500, 1, false, true))
                .build());
        register(ctx, "enriched_blood", new SyringeFluidType.Builder()
                .fluids(BFluids.ENRICHED_BLOOD.getSource())
                .color(0xE11313)
                .opaque(true)
                .damage(6)
                .attackSpeed(-2.5F)
                .onEntityHitEffect(new MobEffectInstance(MobEffects.WEAKNESS, 500, 1, false, true))
                .build());
        register(ctx, "boiling_blood", new SyringeFluidType.Builder()
                .fluids(BFluids.BOILING_BLOOD.getSource())
                .color(0xE11313)
                .opaque(true)
                .damage(6)
                .attackSpeed(-2.5F)
                .burning(new BurningData(4, 4F))
                .build());
        register(ctx, "purple_soda_from_fishes", new SyringeFluidType.Builder()
                .fluids(ACFluidRegistry.PURPLE_SODA_FLUID_SOURCE.get())
                .mobs(ACEntityRegistry.SWEETISH_FISH.get())
                .color(0x7F00FF)
                .food(new FoodProperties.Builder()
                        .nutrition(2)
                        .saturationMod(0.4f)
                        .build())
                .onEntityHitEffect(new MobEffectInstance(ACEffectRegistry.SUGAR_RUSH.get(), 500, 1, false, true))
                .build());
    }
    private static void register(BootstapContext<SyringeFluidType> ctx, String name, SyringeFluidType type) {
        ctx.register(ResourceKey.create(BRegistries.SYRINGE_BLADE_FLUIDS, BloodIsFuel.asResource(name)), type);
    }
}