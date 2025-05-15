package net.electrisoma.bloodisfuel.api.equipment;

import net.electrisoma.bloodisfuel.api.utils.SyringeUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.fluids.FluidStack;


public class SyringeItemColor implements ItemColor, SyringeUtils {

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex != 0) return 0xFFFFFFFF; // Default color if tintIndex is not 0

        Level level = Minecraft.getInstance().level;
        RegistryAccess access = level != null ? level.registryAccess() : RegistryAccess.EMPTY;

        FluidStack fluidStack = readFluid(stack);
        if (fluidStack.isEmpty()) return 0xFFFFFFFF;

        SyringeFluidType type = SyringeFluidTypeManager.fromFluid(fluidStack, access);

        return SyringeFluidTypeManager.getColor(type, fluidStack);
    }
}

