package net.electrisoma.bloodisfuel.compat.jei;

import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidTypeManager;
import net.electrisoma.bloodisfuel.api.utils.SyringeUtils;

import net.electrisoma.bloodisfuel.registry.BTags;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class BloodExtractorJeiHelper implements SyringeUtils {
    public static final BloodExtractorJeiHelper INSTANCE = new BloodExtractorJeiHelper();

    public List<BloodExtractorInfo> collectAndGroupBloodExtractorRecipes(RegistryAccess access) {
        List<BloodExtractorInfo> recipes = new ArrayList<>();
        List<SyringeFluidType> fluidTypes = SyringeFluidTypeManager.getAll(access);
        SyringeFluidType fallback = getFallback(access);

        for (EntityType<?> entityType : ForgeRegistries.ENTITY_TYPES) {
            if (entityType.getCategory() == MobCategory.MISC) continue;
            if (entityType.is(BTags.BEntityTags.DOES_NOT_DROP_FLUID.tag)) continue;

            Optional<Holder<EntityType<?>>> holderOpt = ForgeRegistries.ENTITY_TYPES.getHolder(entityType);
            if (holderOpt.isEmpty()) continue;
            Holder<EntityType<?>> entityHolder = holderOpt.get();

            SyringeFluidType matchedType = fluidTypes.stream()
                    .filter(type -> type.mobs()
                            .map(sets -> sets.stream().anyMatch(set -> set.contains(entityHolder)))
                            .orElse(false))
                    .max(Comparator.comparingInt(SyringeFluidType::mobPriority))
                    .orElse(null);

            SyringeFluidType toUse = matchedType != null ? matchedType : fallback;
            if (toUse == null) continue;

            List<FluidStack> outputs = new ArrayList<>();
            for (HolderSet<Fluid> fluidSet : toUse.fluids()) {
                for (Holder<Fluid> fluidHolder : fluidSet) {
                    Fluid fluid = fluidHolder.value();
                    if (Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluid)).getPath().contains("flowing")) continue;
                    outputs.add(new FluidStack(fluid, 1000));
                }
            }

            if (!outputs.isEmpty()) {
                recipes.add(new BloodExtractorInfo(outputs, HolderSet.direct(entityHolder)));
            }
        }

        Player clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer != null) {
            Holder<EntityType<?>> playerHolder = Holder.direct(EntityType.PLAYER);

            SyringeFluidType playerType = fluidTypes.stream()
                    .filter(type -> type.mobs()
                            .map(sets -> sets.stream().anyMatch(set -> set.contains(playerHolder)))
                            .orElse(false))
                    .max(Comparator.comparingInt(SyringeFluidType::mobPriority))
                    .orElse(fallback);

            if (playerType != null) {
                List<FluidStack> playerOutputs = new ArrayList<>();
                for (HolderSet<Fluid> fluidSet : playerType.fluids()) {
                    for (Holder<Fluid> fluidHolder : fluidSet) {
                        Fluid fluid = fluidHolder.value();
                        if (Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluid)).getPath().contains("flowing")) continue;
                        playerOutputs.add(new FluidStack(fluid, 1000));
                    }
                }

                if (!playerOutputs.isEmpty()) {
                    recipes.add(new BloodExtractorInfo(playerOutputs, HolderSet.direct(playerHolder)));
                }
            }
        }

        return recipes;
    }
}
