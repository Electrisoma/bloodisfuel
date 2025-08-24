package net.electrisoma.bloodisfuel.api.equipment.syringe;

import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.foundation.data.entries.BExtractionTypes;
import net.electrisoma.bloodisfuel.registry.BFluids;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class SyringeFluidExtractionTypeManager {
    private static final Set<ResourceLocation> POTION_FLUID_IDS = Set.of(
            new ResourceLocation("forge", "potion"),
            new ResourceLocation("create", "potion")
    );

    public static Collection<SyringeExtractionType> getAll(RegistryAccess access) {
        return access.registryOrThrow(BRegistries.SYRINGE_EXTRACTION).stream().toList();
    }

    public static Fluid getFluidFor(SyringeExtractionType type) {
        return type.fluid().stream()
                .map(Holder::value)
                .findFirst()
                .orElse(BFluids.BLOOD.getSource()); // fallback
    }

    public static boolean isPotionFluid(Fluid fluid) {
        ResourceLocation key = ForgeRegistries.FLUIDS.getKey(fluid);
        return key != null && POTION_FLUID_IDS.contains(key);
    }

    public static FluidStack createPotionFluidStack(SyringeExtractionType type, int amount) {
        if (!type.isPotionType()) return FluidStack.EMPTY;

        Fluid potionFluid = getFluidFor(type);
        FluidStack stack = new FluidStack(potionFluid, amount);

        type.potion().ifPresent(potion -> {
            ItemStack dummy = new ItemStack(Items.POTION);
            PotionUtils.setPotion(dummy, potion);

            if (dummy.hasTag()) {
                assert dummy.getTag() != null;
                stack.setTag(dummy.getTag().copy());
            }
        });

        return stack;
    }

    public static int getAmount(SyringeExtractionType type) {
        return type.amount();
    }

    public static boolean matchesEntity(SyringeExtractionType type, EntityType<?> entityType) {
        for (HolderSet<EntityType<?>> set : type.mobs()) {
            for (Holder<EntityType<?>> holder : set)
                if (holder.value() == entityType) return true;
        }
        return false;
    }
}