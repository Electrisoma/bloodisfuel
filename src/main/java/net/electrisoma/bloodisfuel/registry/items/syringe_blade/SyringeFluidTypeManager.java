package net.electrisoma.bloodisfuel.registry.items.syringe_blade;

import net.electrisoma.bloodisfuel.api.registry.BRegistries;

import com.simibubi.create.Create;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.effect.MobEffectInstance;

import net.minecraftforge.fluids.FluidStack;

import java.util.*;


@SuppressWarnings("unused")
public class SyringeFluidTypeManager {

    // hardcoded values
    public static final SyringeFluidType POTION = new SyringeFluidType.Builder()
            .addFluids(net.minecraftforge.registries.ForgeRegistries.FLUIDS.getValue(
                    new net.minecraft.resources.ResourceLocation(Create.ID, "potion")))
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

        Registry<SyringeFluidType> registry = access.registryOrThrow(BRegistries.SYRINGE_BLADE_FLUID_TYPE);
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

    // potion type check
    public static boolean isPotionType(SyringeFluidType type) {
        return type.fluids().stream().anyMatch(holder ->
                holder.unwrapKey().map(ResourceKey::location)
                        .map(ResourceLocation::getPath)
                        .orElse("")
                        .equals("potion"));
    }
}
