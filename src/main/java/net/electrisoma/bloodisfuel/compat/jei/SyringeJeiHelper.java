package net.electrisoma.bloodisfuel.compat.jei;

import net.electrisoma.bloodisfuel.registry.BTags;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidTypeManager;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.material.Fluid;

import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;


public class SyringeJeiHelper {
    private static final Set<String> IGNORED_POTIONS = Set.of("empty", "awkward", "water");

    public static List<SyringeInfo> collectAndGroupSyringeRecipes(RegistryAccess access) {
        Optional<HolderSet.Named<Item>> optionalSyringeTag =
                access.registryOrThrow(Registries.ITEM).getTag(BTags.BItemTags.SYRINGES.tag);

        if (optionalSyringeTag.isEmpty())
            return Collections.emptyList();

        List<FluidStack> allFluids = collectAllValidFluids(access);
        Map<String, SyringeInfo> grouped = groupFluidsByKey(allFluids, optionalSyringeTag.get());

        List<SyringeInfo> entries = new ArrayList<>(grouped.values());

        entries.sort(Comparator.comparing(info -> info.fluidVariants().stream()
                .map(SyringeJeiHelper::getNormalizedFluidKey)
                .min(String::compareTo)
                .orElse("")));

        entries.forEach(info ->
                info.fluidVariants().sort(Comparator.comparingInt(SyringeJeiHelper::getPotionAmplifier)));

        return entries;
    }
    private static List<FluidStack> collectAllValidFluids(RegistryAccess access) {
        List<FluidStack> fluids = new ArrayList<>();

        for (SyringeFluidType type : SyringeFluidTypeManager.getAll(access)) {
            for (HolderSet<Fluid> fluidSet : type.fluids()) {
                for (Holder<Fluid> holder : fluidSet) {
                    Fluid fluid = holder.value();
                    ResourceLocation key = ForgeRegistries.FLUIDS.getKey(fluid);

                    if (key == null || key.getPath().contains("flowing"))
                        continue;

                    boolean isPotion = SyringeFluidTypeManager.isPotion(fluid, access) || isTaggedPotionFluid(fluid);

                    if (isPotion && type.isPotionType()) {
                        expandPotionFluids(fluid, fluids);
                    } else {
                        fluids.add(new FluidStack(fluid, 1000));
                    }
                }
            }
        }

        return fluids;
    }

    private static Map<String, SyringeInfo> groupFluidsByKey(List<FluidStack> fluids, HolderSet.Named<Item> syringes) {
        Map<String, SyringeInfo> grouped = new HashMap<>();

        for (FluidStack fs : fluids) {
            String key = getNormalizedFluidKey(fs);

            for (Holder<Item> holder : syringes) {
                ItemStack empty = new ItemStack(holder.value());
                ItemStack output = simulateFillSyringe(empty, fs.copy());

                grouped.compute(key, (k, existing) -> {
                    if (existing == null) {
                        return new SyringeInfo(empty, new ArrayList<>(List.of(fs.copy())), new ArrayList<>(List.of(output)));
                    }

                    if (existing.fluidVariants().stream().noneMatch(f -> fluidStacksEqual(f, fs)))
                        existing.fluidVariants().add(fs.copy());

                    if (existing.outputVariants().stream().noneMatch(o -> ItemStack.isSameItemSameTags(o, output)))
                        existing.outputVariants().add(output);

                    return existing;
                });
            }
        }

        return grouped;
    }
    private static ItemStack simulateFillSyringe(ItemStack syringe, FluidStack fluid) {
        ItemStack result = syringe.copy();
        result.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler ->
                handler.fill(fluid, IFluidHandler.FluidAction.EXECUTE));
        return result;
    }
    private static boolean fluidStacksEqual(FluidStack a, FluidStack b) {
        return a.getFluid().equals(b.getFluid())
                && a.getAmount() == b.getAmount()
                && Objects.equals(a.getTag(), b.getTag());
    }
    public static String getNormalizedFluidKey(FluidStack stack) {
        String baseName = stack.getDisplayName().getString().toLowerCase(Locale.ROOT).trim();

        if (stack.hasTag() && stack.getTag().contains("Potion")) {
            Potion potion = getPotion(stack);
            if (potion != null) {
                String effects = potion.getEffects().stream()
                        .map(e -> Objects.requireNonNull(ForgeRegistries.MOB_EFFECTS.getKey(e.getEffect())).toString())
                        .sorted()
                        .collect(Collectors.joining(","));
                return baseName + "<" + effects + ">";
            }
        }

        return baseName;
    }

    private static void expandPotionFluids(Fluid fluid, List<FluidStack> list) {
        for (ResourceLocation potionId : ForgeRegistries.POTIONS.getKeys()) {
            if (IGNORED_POTIONS.contains(potionId.getPath())) continue;

            Potion potion = ForgeRegistries.POTIONS.getValue(potionId);
            if (potion == null || potion.getEffects().isEmpty()) continue;

            FluidStack fs = new FluidStack(fluid, 1000);
            fs.getOrCreateTag().putString("Potion", potionId.toString());
            list.add(fs);
        }
    }
    private static boolean isTaggedPotionFluid(Fluid fluid) {
        ResourceLocation key = ForgeRegistries.FLUIDS.getKey(fluid);
        if (key == null) return false;

        return key.getPath().contains("potion") ||
                fluid.getFluidType().toString().toLowerCase(Locale.ROOT).contains("potion");
    }
    public static Potion getPotion(FluidStack stack) {
        if (stack.hasTag() && stack.getTag().contains("Potion")) {
            return ForgeRegistries.POTIONS.getValue(ResourceLocation.tryParse(stack.getTag().getString("Potion")));
        }
        return null;
    }
    static int getPotionAmplifier(FluidStack fs) {
        Potion potion = getPotion(fs);
        if (potion != null) {
            return potion.getEffects().stream()
                    .mapToInt(MobEffectInstance::getAmplifier)
                    .min()
                    .orElse(0);
        }
        return 0;
    }
}
