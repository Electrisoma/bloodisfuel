//package net.electrisoma.bloodisfuel.registry.items;
//
//import com.simibubi.create.foundation.utility.CreateLang;
//
//import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
//import io.github.fabricators_of_create.porting_lib.transfer.fluid.item.FluidHandlerItemStack;
//
//import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
//
//import net.minecraft.ChatFormatting;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.world.level.ItemLike;
//
//import org.jetbrains.annotations.ApiStatus;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.List;
//import java.util.function.BiConsumer;
//import java.util.function.Function;
//
//
////gotta make this not bad
//public interface ItemUtils {
//
//    default int getBaseCapacity(ItemStack stack){
//        return 1000;
//        //return BServer.BLOOD_CAPACITY.get();
//    }
//
//    default int getCapacityEnchantmentAddition(ItemStack stack){
//        return 1000;
//        //return BServer.BLOOD_CAPACITY_ENCHANTMENT.get();
//    }
//
//    default int getCapacity(ItemStack stack){
//        return 1000;
//        //return BServer.BLOOD_CAPACITY_ENCHANTMENT.get();
//    }
//
//
//    default FluidStack readFluid(ItemStack stack){
//        return FluidStack.loadFluidStackFromNBT(stack.getOrCreateTag().getCompound("Fluid"));
//    }
//
//    default BiConsumer<ContainerItemContext, FluidStack> writeFluid(ItemStack stack, FluidStack fluid){
//        stack.getOrCreateTag().put("Fluid", fluid.writeToNBT(new CompoundTag()));
//        return null;
//    }
//
//    default int getCurrentFillLevel(ItemStack stack){
//        return (int) readFluid(stack).getAmount();
//    }
//
//    default void tooltipMaker(List<Component> tooltip, ItemStack stack){
//        if(stack.getTag() != null) {
//            FluidStack fluid = readFluid(stack);
//            if(fluid.isEmpty()){
//                tooltip.add(Component.translatable("bloodisfuel.tooltip.empty").withStyle(ChatFormatting.GRAY));
//                return;
//            }
//            tooltip.add(CreateLang.fluidName(fluid).component()
//                    .withStyle(ChatFormatting.GRAY)
//                    .append(" ")
//                    .append(CreateLang.number(fluid.getAmount()).style(ChatFormatting.GOLD).component())
//                    .append(Component.translatable("create.generic.unit.millibuckets").withStyle(ChatFormatting.GOLD))
//                    .append(Component.literal(" / "))
//                    .append(CreateLang.number(getCapacity(stack)).style(ChatFormatting.GRAY).component())
//                    .append(Component.translatable("create.generic.unit.millibuckets").withStyle(ChatFormatting.GRAY)));
//            return;
//        }
//        tooltip.add(Component.translatable("bloodisfuel.tooltip.empty").withStyle(ChatFormatting.GRAY));
//    }
//
//    default FluidHandlerItemStack getFluidHandler(ItemStack stack){
//        return new ToolItemFluidHandler(stack, getCapacity(stack), this::readFluid, this::writeFluid);
//    }
//
//    void writeFluid(ContainerItemContext containerItemContext, FluidStack fluidStack);
//
//    @ApiStatus.Experimental
//    class ToolItemFluidHandler extends FluidHandlerItemStack{
//        BiConsumer<ItemStack, FluidStack> write;
//        Function<ItemStack, FluidStack> read;
//        public ToolItemFluidHandler(ItemStack container, int capacity, Function<ItemStack, FluidStack> read, BiConsumer<ItemStack, FluidStack> write) {
//            super(ContainerItemContext.withConstant(container), capacity);
//            this.write = write;
//            this.read = read;
//        }
//
//        @Override
//        public @NotNull FluidStack getFluid() {
//            return read.apply(new ItemStack((ItemLike) container));
//        }
//
//        protected void setFluid(FluidStack fluid) {
//            write.accept(new ItemStack((ItemLike) container), fluid);
//        }
//    }
//
//    class FuelItems extends Item {
//        private int burnTime = 0;
//
//        public FuelItems(Item.Properties pProperties, int burnTime) {
//            super(pProperties);
//            this.burnTime = burnTime;
//        }
//
//        public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType){
//            return this.burnTime;
//        }
//    }
//}
