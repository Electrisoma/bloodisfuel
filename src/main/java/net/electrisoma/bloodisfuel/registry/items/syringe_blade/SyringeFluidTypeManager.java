package net.electrisoma.bloodisfuel.registry.items.syringe_blade;

import net.electrisoma.bloodisfuel.api.registry.BRegistries;

import com.simibubi.create.Create;

import net.electrisoma.bloodisfuel.registry.BFluids;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.effect.MobEffectInstance;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;


//@SuppressWarnings("unused")
public class SyringeFluidTypeManager {

    /**
     * accepted datapack formatting
     * ------------------------------------------------------------------
     * in data/MOD_ID/bloodisfuel/syringe_blade_fluids/FLUIDTYPE.json
     * {
     *   "color": COLOR,
     *   "fluids": "FLUIDS",
     *   "mobs": ["MOBS"],
     *   "on_entity_hit": {
     *     "amplifier": AMPLIFIER,
     *     "duration": DURATION,
     *     "effect": "EFFECT"
     *   }
     * }
     * ------------------------------------------------------------------
     * "color" refers to the bar color of the item to identify the fluid
     * "fluids" refers to the actual fluid that it applies to
     * "on_entity_hit" refers to the effects of hitting an entity
     */


    // hardcoded values
    public static final SyringeFluidType POTION = new SyringeFluidType.Builder()
            .addFluids(ForgeRegistries.FLUIDS.getValue(
                    new ResourceLocation(Create.ID, "potion")))
            .color(0x9966FF)
            .build();

    public static final SyringeFluidType EMPTY = new SyringeFluidType.Builder()
            .color(0xBD3228)
            .build();

    private static final List<SyringeFluidType> HARDCODED = List.of(
            POTION
    );

    // fluid
    public static SyringeFluidType fromFluid(FluidStack stack, RegistryAccess access) {
        if (stack.isEmpty()) return EMPTY;
        Fluid fluid = stack.getFluid();

        Registry<SyringeFluidType> registry = access.registryOrThrow(BRegistries.SYRINGE_BLADE_FLUIDS);
        Optional<SyringeFluidType> dynamic = registry.stream()
                .filter(type -> type.fluids().stream().anyMatch(holder -> holder.value() == fluid))
                .findFirst();

        return dynamic.orElseGet(() -> HARDCODED.stream()
                .filter(type -> type.fluids().stream().anyMatch(holder -> holder.value() == fluid))
                .findFirst()
                .orElse(EMPTY));
    }

    // bar color
    public static int getColor(SyringeFluidType type, FluidStack stack) {
        if (type.isPotionType()) {
            CompoundTag tag = stack.getOrCreateTag();
            return PotionUtils.getColor(PotionUtils.getAllEffects(tag)) | 0xff000000;
        } return type.color();
    }

    // fluid effects
    public static List<MobEffectInstance> getEffects(SyringeFluidType type, FluidStack fluidStack) {
        if (type.isPotionType() && fluidStack.hasTag()) return PotionUtils.getAllEffects(fluidStack.getTag());

        return type.onEntityHitEffect()
                .map(effect -> List.of(new MobEffectInstance(effect)))
                .orElse(List.of());
    }

    public static List<SyringeFluidType> getAll(RegistryAccess access) {
        Registry<SyringeFluidType> registry = access.registryOrThrow(BRegistries.SYRINGE_BLADE_FLUIDS);
        return registry.stream().toList();
    }

    public static Fluid getFluidFor(SyringeFluidType type) {
        return type.fluids().stream().findFirst()
                .map(Holder::value)
                .orElse(BFluids.BLOOD.get());
    }
}