package net.electrisoma.bloodisfuel.api.equipment;

import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.registry.BFluids;

import com.simibubi.create.Create;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.effect.MobEffectInstance;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;


@SuppressWarnings({"unused", "DataFlowIssue"})
public class SyringeFluidTypeManager {

    /**
     * Expected datapack format:
     * {
     *   "color": COLOR,
     *   "fluids": "FLUIDS",
     *   "mobs": ["MOBS"],
     *   "on_entity_hit": {
     *     "amplifier": AMPLIFIER,
     *     "duration": DURATION,
     *     "effect": "EFFECT"
     *   },
     *   "burning": {
     *       "damage_per_second": DAMAGE",
     *       "duration_seconds": SECONDS"
     *   }
     * }
     * Mobs, color, on_entity_hit, and burning are optional
     */

    // hardcoded values
    public static final SyringeFluidType POTION = new SyringeFluidType.Builder()
            .addFluids(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(Create.ID, "potion")))
            .color(0x9966FF)
            .build();

    public static final SyringeFluidType EMPTY = new SyringeFluidType.Builder()
            .color(0xBD3228)
            .build();

    private static final List<SyringeFluidType> HARDCODED = List.of(POTION);

    /**
     * Gets the SyringeFluidType for a given FluidStack.
     */
    public static SyringeFluidType fromFluid(FluidStack stack, RegistryAccess access) {
        if (stack.isEmpty()) return EMPTY;
        Fluid fluid = stack.getFluid();

        Registry<SyringeFluidType> registry = access.registryOrThrow(BRegistries.SYRINGE_BLADE_FLUIDS);
        Optional<SyringeFluidType> dynamicMatch = registry.stream()
                .filter(type -> type.fluids().stream().anyMatch(holder -> holder.value() == fluid))
                .findFirst();

        return dynamicMatch.orElseGet(() ->
                HARDCODED.stream()
                        .filter(type -> type.fluids().stream().anyMatch(holder -> holder.value() == fluid))
                        .findFirst()
                        .orElse(EMPTY));
    }

    /**
     * Gets the display color of the fluid, including potion.
     */
    public static int getColor(SyringeFluidType type, FluidStack stack) {
        if (type.isPotionType()) {
            CompoundTag tag = stack.getOrCreateTag();
            return PotionUtils.getColor(PotionUtils.getAllEffects(tag)) | 0xFF000000;
        } return type.color();
    }

    /**
     * Returns the list of effects this fluid should apply on hit.
     */
    public static List<MobEffectInstance> getEffects(SyringeFluidType type, FluidStack stack) {
        if (isMilk(stack.getFluid(), null)) return type.onEntityHitEffect()
                .map(effect -> List.of(new MobEffectInstance(effect)))
                .orElse(List.of());
        if (type.isPotionType() && stack.hasTag()) return PotionUtils.getAllEffects(stack.getTag());
        return type.onEntityHitEffect()
                .map(effect -> List.of(new MobEffectInstance(effect)))
                .orElse(List.of());
    }

    /**
     * Returns all dynamically registered fluid types.
     */
    public static List<SyringeFluidType> getAll(RegistryAccess access) {
        Registry<SyringeFluidType> registry = access.registryOrThrow(BRegistries.SYRINGE_BLADE_FLUIDS);
        return registry.stream().toList();
    }

    /**
     * Gets the default fluid associated with a type.
     */
    public static Fluid getFluidFor(SyringeFluidType type) {
        return type.fluids().stream()
                .findFirst()
                .map(Holder::value)
                .orElse(BFluids.BLOOD.get());
    }

    /**
     * Check if the fluid is vanilla milk.
     */
    public static boolean isMilk(Fluid fluid, RegistryAccess access) {
        ResourceLocation fluidKey = ForgeRegistries.FLUIDS.getKey(fluid);
        return fluidKey != null && fluidKey.equals(new ResourceLocation("minecraft", "milk"));
    }

    /**
     * Check if the fluid is a potion.
     */
    public static boolean isPotion(Fluid fluid, RegistryAccess access) {
        ResourceLocation fluidKey = ForgeRegistries.FLUIDS.getKey(fluid);
        return fluidKey != null && fluidKey.equals(new ResourceLocation(Create.ID, "potion"));
    }

    /**
     * Gets the burning properties.
     */
    public static void applyBurning(SyringeFluidType type, LivingEntity target) {
        type.burning().ifPresent(burning -> {
            target.setSecondsOnFire(burning.durationSeconds());
            if (burning.damagePerSecond() > 0)
                target.hurt(target.damageSources().onFire(), burning.damagePerSecond() * burning.durationSeconds());
        });
    }

    /**
     * Gets the extinguishing properties.
     */
    public static void applyExtinguishing(SyringeFluidType type, LivingEntity target) {
        type.extinguishing().ifPresent(extinguishing -> {
            target.clearFire();
            if (extinguishing.healPerSecond() > 0 && extinguishing.durationSeconds() > 0) {
                float totalHeal = extinguishing.healPerSecond() * extinguishing.durationSeconds();
                target.heal(totalHeal);
            }
        });
    }
}