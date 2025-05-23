package net.electrisoma.bloodisfuel.api.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import java.util.function.BiConsumer;
import java.util.function.Function;


public interface FluidUtils extends ItemCapacityUtils {
    default FluidStack readFluid(ItemStack stack) {
        return FluidStack.loadFluidStackFromNBT(stack.getOrCreateTag().getCompound("Fluid"));
    }
    default void writeFluid(ItemStack stack, FluidStack fluid) {
        stack.getOrCreateTag().put("Fluid", fluid.writeToNBT(new CompoundTag()));
    }
    default int getCurrentFillLevel(ItemStack stack) {
        return readFluid(stack).getAmount();
    }
    default FluidHandlerItemStack getFluidHandler(ItemStack stack) {
        return new ToolItemFluidHandler(stack, getCapacity(stack), this::readFluid, this::writeFluid);
    }

    class ToolItemFluidHandler extends FluidHandlerItemStack {
        private final BiConsumer<ItemStack, FluidStack> write;
        private final Function<ItemStack, FluidStack> read;

        public ToolItemFluidHandler(ItemStack container, int capacity,
                                    Function<ItemStack, FluidStack> read,
                                    BiConsumer<ItemStack, FluidStack> write) {
            super(container, capacity);
            this.read = read;
            this.write = write;
        }

        @Override
        public FluidStack getFluid() { return read.apply(container); }

        @Override
        protected void setFluid(FluidStack fluid) { write.accept(container, fluid); }
    }
}
