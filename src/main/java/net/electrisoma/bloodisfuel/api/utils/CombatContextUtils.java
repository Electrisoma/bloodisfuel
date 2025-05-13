package net.electrisoma.bloodisfuel.api.utils;

import net.electrisoma.bloodisfuel.api.equipment.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.equipment.SyringeFluidTypeManager;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;


public interface CombatContextUtils extends FluidUtils {

    record CombatContext(FluidStack fluid, SyringeFluidType type, int useAmount, boolean canAttack, boolean onlyBeneficial) {}

    default CombatContext getCombatContext(ItemStack stack, @Nullable RegistryAccess access) {
        FluidStack fluidStack = readFluid(stack);
        SyringeFluidType type = SyringeFluidTypeManager.fromFluid(fluidStack, access);
        int capacity = getCapacity(stack);
        int charges = getChargeCount(stack);
        int useAmount = getUseAmount(capacity, charges);
        int currentFill = fluidStack.getAmount();
        List<MobEffectInstance> effects = SyringeFluidTypeManager.getEffects(type, fluidStack);
        boolean onlyBeneficial = effects.stream().allMatch(e -> e.getEffect().isBeneficial());
        boolean canAttack = currentFill >= useAmount && !onlyBeneficial;

        return new CombatContext(fluidStack, type, useAmount, canAttack, onlyBeneficial);
    }
}
