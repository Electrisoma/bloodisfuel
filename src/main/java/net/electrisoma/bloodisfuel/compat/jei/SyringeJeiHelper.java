package net.electrisoma.bloodisfuel.compat.jei;

import net.electrisoma.bloodisfuel.registry.BTags;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidTypeManager;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.material.Fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.*;
import java.util.stream.Collectors;


public class SyringeJeiHelper {
    public static List<SyringeInfo> collectAndGroupSyringeRecipes(RegistryAccess access) {
        Optional<HolderSet.Named<Item>> optionalSyringeTag =
                access.registryOrThrow(Registries.ITEM).getTag(BTags.BItemTags.SYRINGES.tag);

        if (optionalSyringeTag.isEmpty()) return Collections.emptyList();

        HolderSet.Named<Item> syringeTag = optionalSyringeTag.get();
        List<FluidStack> allFluids = collectAllFluids(access);

        Map<String, SyringeInfo> grouped = groupFluidsByKey(allFluids, syringeTag);
        List<SyringeInfo> entries = new ArrayList<>(grouped.values());

        entries.sort(Comparator.comparing(info -> {
            if (info.fluidVariants().isEmpty()) return "";
            return info.fluidVariants().stream()
                    .map(SyringeJeiHelper::getNormalizedFluidKey)
                    .min(String::compareTo)
                    .orElse("");
        }));

        entries.forEach(info -> info.fluidVariants().sort(Comparator.comparingInt(SyringeJeiHelper::getPotionAmplifier)));

        return entries;
    }
    private static List<FluidStack> collectAllFluids(RegistryAccess access) {
        List<FluidStack> allFluids = new ArrayList<>();

        for (SyringeFluidType type : SyringeFluidTypeManager.getAll(access)) {
            for (HolderSet<Fluid> holderSet : type.fluids()) {
                for (Holder<Fluid> fluidHolder : holderSet) {
                    Fluid fluid = fluidHolder.value();
                    ResourceLocation fluidKey = ForgeRegistries.FLUIDS.getKey(fluid);
                    if (fluidKey == null || fluidKey.getPath().contains("flowing")) continue;

                    boolean isPotion = SyringeFluidTypeManager.isPotion(fluid, access) || isTaggedPotionFluid(fluid);

                    if (isPotion && type.isPotionType()) {
                        for (ResourceLocation potionId : ForgeRegistries.POTIONS.getKeys()) {
                            if (Set.of("empty", "awkward", "water").contains(potionId.getPath())) continue;
                            Potion potion = ForgeRegistries.POTIONS.getValue(potionId);
                            if (potion == null || potion.getEffects().isEmpty()) continue;

                            FluidStack fs = new FluidStack(fluid, 1000);
                            fs.getOrCreateTag().putString("Potion", potionId.toString());
                            allFluids.add(fs);
                        }
                    } else {
                        FluidStack fs = new FluidStack(fluid, 1000);
                        fs.setTag(new CompoundTag());
                        allFluids.add(fs);
                    }
                }
            }
        }

        return allFluids;
    }
    private static Map<String, SyringeInfo> groupFluidsByKey(List<FluidStack> allFluids, HolderSet.Named<Item> syringeTag) {
        Map<String, SyringeInfo> grouped = new HashMap<>();

        for (FluidStack fs : allFluids) {
            for (Holder<Item> holder : syringeTag) {
                Item item = holder.value();
                ItemStack empty = new ItemStack(item);
                ItemStack output = simulateFillSyringe(empty, fs.copy());

                String key = getNormalizedFluidKey(fs);
                grouped.compute(key, (k, existing) -> {
                    if (existing == null) {
                        return new SyringeInfo(empty, new ArrayList<>(List.of(fs.copy())), new ArrayList<>(List.of(output)));
                    } else {
                        if (existing.fluidVariants().stream().noneMatch(existingFs -> fluidStacksEqual(existingFs, fs))) {
                            existing.fluidVariants().add(fs.copy());
                        }
                        if (existing.outputVariants().stream().noneMatch(o -> ItemStack.isSameItemSameTags(o, output))) {
                            existing.outputVariants().add(output);
                        }
                        return existing;
                    }
                });
            }
        }

        return grouped;
    }

    private static ItemStack simulateFillSyringe(ItemStack syringe, FluidStack fluid) {
        ItemStack result = syringe.copy();
        result.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler ->
                handler.fill(fluid.copy(), IFluidHandler.FluidAction.EXECUTE)
        );
        return result;
    }

    private static boolean fluidStacksEqual(FluidStack a, FluidStack b) {
        return a.getFluid().equals(b.getFluid())
                && a.getAmount() == b.getAmount()
                && Objects.equals(a.getTag(), b.getTag());
    }
    public static String getNormalizedFluidKey(FluidStack stack) {
        String displayName = stack.getDisplayName().getString().toLowerCase(Locale.ROOT).trim();

        if (stack.hasTag() && stack.getTag().contains("Potion")) {
            Potion potion = getPotion(stack);
            if (potion != null) {
                String effectKey = potion.getEffects().stream()
                        .map(e -> Objects.requireNonNull(ForgeRegistries.MOB_EFFECTS.getKey(e.getEffect())).toString())
                        .sorted()
                        .collect(Collectors.joining(","));
                return displayName + "<" + effectKey + ">";
            }
        }

        return displayName;
    }

    private static boolean isTaggedPotionFluid(Fluid fluid) {
        return fluid.getFluidType().toString().toLowerCase(Locale.ROOT).contains("potion") ||
                Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluid)).getPath().toLowerCase(Locale.ROOT).contains("potion");
    }
    public static Potion getPotion(FluidStack fs) {
        if (fs.hasTag() && fs.getTag().contains("Potion")) {
            ResourceLocation id = ResourceLocation.tryParse(fs.getTag().getString("Potion"));
            return ForgeRegistries.POTIONS.getValue(id);
        }
        return null;
    }
    static int getPotionAmplifier(FluidStack fs) {
        if (fs.hasTag() && fs.getTag().contains("Potion")) {
            Potion potion = getPotion(fs);
            if (potion != null) {
                return potion.getEffects().stream()
                        .mapToInt(MobEffectInstance::getAmplifier)
                        .min()
                        .orElse(0);
            }
        }
        return 0;
    }
}
