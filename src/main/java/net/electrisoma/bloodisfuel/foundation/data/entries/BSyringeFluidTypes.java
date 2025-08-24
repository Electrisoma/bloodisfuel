package net.electrisoma.bloodisfuel.foundation.data.entries;


import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BFluids;
import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidType;

import com.simibubi.create.AllFluids;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.data.worldgen.BootstapContext;

import net.minecraftforge.common.Tags;

public class BSyringeFluidTypes {
    public static final ResourceKey<SyringeFluidType> FALLBACK =
            ResourceKey.create(BRegistries.SYRINGE_FLUIDS, BloodIsFuel.path("fallback"));
    public static final ResourceKey<SyringeFluidType> POTION =
            ResourceKey.create(BRegistries.SYRINGE_FLUIDS, BloodIsFuel.path("potion"));

    public static void bootstrap(BootstapContext<SyringeFluidType> ctx) {
        var fluidLookup = (HolderLookup.RegistryLookup<Fluid>) ctx.lookup(Registries.FLUID);

        // always
        register(ctx, "fallback", new SyringeFluidType.Builder()
                .fluids(BFluids.BLOOD.getSource()) // default = blood
                .appearance(0xF10B0B, true, false)
                .stats(6, -2.5F)
                .addEffect(MobEffects.WEAKNESS, 500, 1)
                .build());
        register(ctx, "potion", new SyringeFluidType.Builder()
                .fluids(AllFluids.POTION.getSource()) // potion support, the color is overridden later
                .fluidTag("forge", "potion", fluidLookup)
                .appearance(0xFFFFFF, false, true)
                .build());

        // potions (mobs dropping potions; not hardcoded)
        register(ctx, "harming_potion", new SyringeFluidType.Builder()
                .fluids(AllFluids.POTION.getSource())
                .fluidTag("forge", "potion", fluidLookup)
                .appearance(0xFFFFFF, false, true)
                .potion(Potions.HARMING)
//                .addMobs(EntityType.CREEPER)
                .build());

        // vanilla and forge
        register(ctx, "water", new SyringeFluidType.Builder()
                .fluids(Fluids.WATER.getSource())
                .color(0x0C17CD)
                .extinguishing(1, 4F)
                .build());
        register(ctx, "lava", new SyringeFluidType.Builder()
                .fluids(Fluids.LAVA.getSource())
                .appearance(0xFF6A00, true, true)
                .burning(15, 4F)
                .build());
        register(ctx, "milk", new SyringeFluidType.Builder()
                .fluidTag(Tags.Fluids.MILK.location(), fluidLookup)
                .appearance(0xFFFFFF, true)
                .addEffect(MobEffects.DAMAGE_BOOST, 100)
//                .addMobs(EntityType.COW)
                .build());
        register(ctx, "honey", new SyringeFluidType.Builder()
                .fluidTag("forge", "honey", fluidLookup)
                .appearance(0xFFBF00, true)
                .food(4, 2)
//                .addMobs(EntityType.BEE)
                .build());
        register(ctx, "chocolate", new SyringeFluidType.Builder()
                .fluidTag("forge", "chocolate", fluidLookup)
                .appearance(0x895129, true)
                .food(4, 2)
                .build());
        register(ctx, "beetroot_soup", new SyringeFluidType.Builder()
                .fluidTag("forge", "beetroot_soup", fluidLookup)
                .appearance(0x80150C, true)
                .food(6, 7.2F)
                .build());
        register(ctx, "rabbit_stew", new SyringeFluidType.Builder()
                .fluidTag("forge", "rabbit_stew", fluidLookup)
                .appearance(0xC58866, true)
                .food(10, 12)
                .build());
        register(ctx, "suspicious_stew", new SyringeFluidType.Builder()
                .fluidTag("forge", "suspicious_stew", fluidLookup)
                .appearance(0xA8D475, true)
                .food(6, 7.2F)
                .build());
        register(ctx, "experience", new SyringeFluidType.Builder()
                .fluidTag("forge", "experience", fluidLookup)
                .appearance(0x44EC16, true, true)
                .build());
        register(ctx, "acid", new SyringeFluidType.Builder()
                .fluidTag("forge", "acid", fluidLookup)
                .appearance(0x61DE2A, true, true)
                .addEffect(MobEffects.WITHER, 300, 1)
                .build());
        register(ctx, "slime", new SyringeFluidType.Builder()
                .fluidTag("forge", "slime", fluidLookup)
                .appearance(0x7AC25B, true)
                .addEffect(MobEffects.JUMP, 300, 1)
//                .addMobs(EntityType.SLIME)
                .build());
        register(ctx, "ender", new SyringeFluidType.Builder()
                .fluidTag("forge", "ender", fluidLookup)
                .appearance(0x258474, true, true)
                .teleports(20)
                .build());
        register(ctx, "powdered_snow", new SyringeFluidType.Builder()
                .fluidTag("forge", "powdered_snow", fluidLookup)
                .appearance(0xFFFFFF, true, false)
//                .addMobs("minecraft:snow_golem")
                .freezing(8, 0.25f, 0.5f)
                .build());

        // blood is fuel
        register(ctx, "molten", new SyringeFluidType.Builder()
                .fluidTag(BloodIsFuel.MOD_ID, "molten", fluidLookup)
                .appearance(0xFF4500, true, true)
                .burning(15, 4F)
                .build());
        register(ctx, "viscera", new SyringeFluidType.Builder()
                .fluids(BFluids.VISCERA.getSource())
                .appearance(0xDF4416, true)
                .addEffect(MobEffects.POISON, 500, 1)
                .stats(6, -2.5F)
                //.mobTag("forge", "undead", mobLookup)
                .build());
        register(ctx, "blood", new SyringeFluidType.Builder()
                .fluids(BFluids.BLOOD.getSource())
                .appearance(0x880000, true)
                .addEffect(MobEffects.WEAKNESS, 500, 1)
                .addEffect(MobEffects.MOVEMENT_SLOWDOWN, 500, 1, false, true, false)
                .stats(6, -2.5F)
                .build());
        register(ctx, "enriched_blood", new SyringeFluidType.Builder()
                .fluids(BFluids.ENRICHED_BLOOD.getSource())
                .appearance(0xE11313, true)
                .addEffect(MobEffects.WEAKNESS, 500, 1)
                .stats(6, -2.5F)
                .build());
        register(ctx, "boiling_blood", new SyringeFluidType.Builder()
                .fluids(BFluids.BOILING_BLOOD.getSource())
                .appearance(0xE11313, true)
                .stats(6, -2.5F)
                .burning(4, 4F)
                .build());
        register(ctx, "blazing_blood", new SyringeFluidType.Builder()
                .fluids(BFluids.BLAZING_BLOOD.getSource())
                .appearance(0xFFAE42, true, true)
                .stats(6, -2.5F)
                .burning(4, 4F)
                //.addMobs(EntityType.BLAZE)
                .build());

        // mod compat
        register(ctx, "ac_purple_soda", new SyringeFluidType.Builder()
                .fluids("alexscaves:purple_soda")
                .color(0x7F00FF)
//                .addMobs(
//                        "alexscaves:sweetish_fish",
//                        "alexscaves:gummy_bear",
//                        "alexscaves:gumbeeper",
//                        "alexscaves:gum_worm",
//                        "alexscaves:licowitch",
//                        "alexscaves:caramel_cube",
//                        "alexscaves:candicorn",
//                        "alexscaves:caniac"
//                )
                .addEffect("alexscaves:sugar_rush", 500, 1)
                .food(4, 0.4F)
                .build());
        register(ctx, "ac_acid", new SyringeFluidType.Builder()
                .fluids("alexscaves:acid")
                .appearance(0x61DE2A, true, true)
                .addEffect(MobEffects.WITHER, 300, 1)
//                .addMobs(
//                        "alexscaves:nucleeper",
//                        "alexscaves:radgill",
//                        "alexscaves:brainiac",
//                        "alexscaves:gammaroach",
//                        "alexscaves:raycat",
//                        "alexscaves:tremorzilla"
//                )
                .build());
        register(ctx, "bf_nutrients_fluid", new SyringeFluidType.Builder()
                .fluidTag("biofactory", "nutrients_fluid", fluidLookup)
                .appearance(0x495C22, true)
                .food(9, 1.2F)
                .build());
        register(ctx, "tc_earthslime", new SyringeFluidType.Builder()
                .fluidTag("tconstruct", "earth_slime", fluidLookup)
                .appearance(0x7AC25B, true)
                .addEffect("tconstruct:bouncy", 300,1)
                //.addMobs(EntityType.SLIME)
                .build());
        register(ctx, "tc_skyslime", new SyringeFluidType.Builder()
                .fluidTag("tconstruct", "sky_slime", fluidLookup)
                .appearance(0x6CC8C2, true)
                .addEffect("tconstruct:ricochet", 300,1)
                //.addMobs("tconstruct:sky_slime")
                .build());
        register(ctx, "tc_enderslime", new SyringeFluidType.Builder()
                .fluidTag("tconstruct", "ender_slime", fluidLookup)
                .appearance(0xB552F0, true)
                .addEffect("tconstruct:enderference", 300,1)
                //.addMobs("tconstruct:ender_slime")
                .build());
        register(ctx, "tc_terracube", new SyringeFluidType.Builder()
                .fluidTag("tconstruct", "molten_clay", fluidLookup)
                .appearance(0x563D2D, true, true)
                .burning(2, 7F)
                //.addMobs("tconstruct:terracube")
                .build());
    }

    private static void register(BootstapContext<SyringeFluidType> ctx, String name, SyringeFluidType type) {
        ctx.register(ResourceKey.create(BRegistries.SYRINGE_FLUIDS, BloodIsFuel.path(name)), type);
    }
}