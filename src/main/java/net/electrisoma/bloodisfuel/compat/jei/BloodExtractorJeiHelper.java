package net.electrisoma.bloodisfuel.compat.jei;

import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeExtractionType;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidExtractionTypeManager;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidTypeManager;
import net.electrisoma.bloodisfuel.api.utils.SyringeUtils;

import net.electrisoma.bloodisfuel.registry.BTags;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.material.Fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class BloodExtractorJeiHelper implements SyringeUtils {
    public static final BloodExtractorJeiHelper INSTANCE = new BloodExtractorJeiHelper();
    private List<BloodExtractorInfo> cachedRecipes = null;

    public List<BloodExtractorInfo> getOrCollectRecipes(RegistryAccess access) {
        if (cachedRecipes == null) {
            cachedRecipes = collectAndGroupBloodExtractorRecipes(access);
        }
        return cachedRecipes;
    }

    public List<BloodExtractorInfo> collectAndGroupBloodExtractorRecipes(RegistryAccess access) {
        List<BloodExtractorInfo> recipes = new ArrayList<>();
        List<SyringeExtractionType> extractionTypes = (List<SyringeExtractionType>) SyringeFluidExtractionTypeManager.getAll(access);
        SyringeExtractionType fallback = getFallbackExtraction(access);

        var entityRegistry = access.registryOrThrow(Registries.ENTITY_TYPE);

        for (EntityType<?> entityType : ForgeRegistries.ENTITY_TYPES) {
            if (entityType.getCategory() == MobCategory.MISC &&
                    !entityType.is(BTags.BEntityTags.ALLOW_FLUID_DROP.tag)) {
                continue;
            }
            if (entityType.is(BTags.BEntityTags.DOES_NOT_DROP_FLUID.tag)) continue;

            ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
            if (entityId == null) continue;

            ResourceKey<EntityType<?>> entityKey = ResourceKey.create(Registries.ENTITY_TYPE, entityId);
            Optional<Holder.Reference<EntityType<?>>> holderOpt = entityRegistry.getHolder(entityKey);
            if (holderOpt.isEmpty()) continue;

            Holder.Reference<EntityType<?>> entityHolder = holderOpt.get();

            SyringeExtractionType matched = extractionTypes.stream()
                    .filter(type -> type.mobs().stream()
                            .anyMatch(set -> set.contains(entityHolder)))
                    .findFirst()
                    .orElse(null);

            SyringeExtractionType toUse = matched != null ? matched : fallback;
            if (toUse != null) {
                List<FluidStack> outputs = buildOutputFluidStacks(toUse);
                if (!outputs.isEmpty()) {
                    recipes.add(new BloodExtractorInfo(outputs, HolderSet.direct(entityHolder)));
                }
            }
        }

        // Special handling for client player
        Player clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer != null) {
            ResourceLocation playerId = Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(EntityType.PLAYER));
            ResourceKey<EntityType<?>> playerKey = ResourceKey.create(Registries.ENTITY_TYPE, playerId);
            Holder.Reference<EntityType<?>> playerHolder = entityRegistry.getHolderOrThrow(playerKey);

            SyringeExtractionType matched = extractionTypes.stream()
                    .filter(type -> type.mobs().stream()
                            .anyMatch(set -> set.contains(playerHolder)))
                    .findFirst()
                    .orElse(fallback);

            if (matched != null) {
                List<FluidStack> playerOutputs = buildOutputFluidStacks(matched);
                if (!playerOutputs.isEmpty()) {
                    recipes.add(new BloodExtractorInfo(playerOutputs, HolderSet.direct(playerHolder)));
                }
            }
        }

        return recipes;
    }

    private List<FluidStack> buildOutputFluidStacks(SyringeExtractionType type) {
        List<FluidStack> outputs = new ArrayList<>();

        for (Holder<Fluid> fluidHolder : type.fluid()) {
            Fluid fluid = fluidHolder.value();
            ResourceLocation fluidId = ForgeRegistries.FLUIDS.getKey(fluid);
            if (fluidId == null || fluidId.getPath().contains("flowing")) continue;

            FluidStack stack = new FluidStack(fluid, type.amount());

            type.potion().ifPresent(potion -> {
                CompoundTag tag = stack.getOrCreateTag();
                ResourceLocation potionId = ForgeRegistries.POTIONS.getKey(potion);
                if (potionId != null) {
                    tag.putString("Potion", potionId.toString());
                }
            });

            if (!stack.isEmpty()) {
                outputs.add(stack);
            }
        }

        return outputs;
    }}

