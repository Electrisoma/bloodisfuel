package net.electrisoma.bloodisfuel.foundation.data.entries;

import com.simibubi.create.AllFluids;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeExtractionType;
import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.registry.BFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluid;

public class BExtractionTypes {
    public static final ResourceKey<SyringeExtractionType> FALLBACK =
            ResourceKey.create(BRegistries.SYRINGE_EXTRACTION, BloodIsFuel.path("fallback"));

    public static void bootstrap(BootstapContext<SyringeExtractionType> ctx) {
        var fluidLookup = (HolderLookup.RegistryLookup<Fluid>) ctx.lookup(Registries.FLUID);
        var mobLookup = (HolderLookup.RegistryLookup<EntityType<?>>) ctx.lookup(Registries.ENTITY_TYPE);

        register(ctx, "fallback", new SyringeExtractionType.Builder()
                .addMobs("minecraft:player")
                .fluid(BFluids.BLOOD.getSource())
                .build());

        register(ctx, "cow", new SyringeExtractionType.Builder()
                .addMobs(EntityType.COW)
                .fluidTag("forge", "milk", fluidLookup)
                .build());

        register(ctx, "bee", new SyringeExtractionType.Builder()
                .addMobs(EntityType.BEE)
                .fluidTag("forge", "honey", fluidLookup)
                .build());

        register(ctx, "creeper", new SyringeExtractionType.Builder()
                .addMobs(EntityType.CREEPER)
                .fluid(AllFluids.POTION.getSource())
                .potion(Potions.HARMING)
                .build());

        register(ctx, "slime", new SyringeExtractionType.Builder()
                .mobTag("forge", "slimes", mobLookup)
                .fluidTag("forge", "slime", fluidLookup)
                .build());

        register(ctx, "ender", new SyringeExtractionType.Builder()
                .mobTag("forge", "ender", mobLookup)
                .fluidTag("forge", "ender", fluidLookup)
                .build());

        register(ctx, "undead", new SyringeExtractionType.Builder()
                .mobTag("forge", "undead", mobLookup)
                .fluid(BFluids.VISCERA.getSource())
                .build());

        register(ctx, "ac_acid", new SyringeExtractionType.Builder()
                .addMobs(
                        "alexscaves:nucleeper",
                        "alexscaves:radgill",
                        "alexscaves:brainiac",
                        "alexscaves:gammaroach",
                        "alexscaves:raycat",
                        "alexscaves:tremorzilla"
                )
                .fluid("alexscaves:acid")
                .build());
    }

    private static void register(BootstapContext<SyringeExtractionType> ctx, String name, SyringeExtractionType type) {
        ctx.register(ResourceKey.create(BRegistries.SYRINGE_EXTRACTION, BloodIsFuel.path(name)), type);
    }
}
