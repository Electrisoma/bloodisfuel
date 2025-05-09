package net.electrisoma.bloodisfuel.registry.items;

import net.electrisoma.bloodisfuel.registry.BEnchantments;

import com.simibubi.create.AllEnchantments;
import com.simibubi.create.foundation.utility.CreateLang;

import net.electrisoma.bloodisfuel.registry.items.syringe_blade.SyringeFluidType;
import net.electrisoma.bloodisfuel.registry.items.syringe_blade.SyringeFluidTypeManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;
import java.util.function.BiConsumer;


@SuppressWarnings("all")
public interface ItemUtils {

    default int getBaseCapacity(ItemStack stack){
        return 1000;
    }

    default int getCapacityEnchantmentAddition(ItemStack stack){
        return 1000;
    }

    default int getCapacity(ItemStack stack){
        return getBaseCapacity(stack) + (getCapacityEnchantmentAddition(stack) *
                stack.getEnchantmentLevel(AllEnchantments.CAPACITY.get()));
    }

    default int getChargeCount(ItemStack stack) {
        int baseCharges = 4;
        int level = stack.getEnchantmentLevel(BEnchantments.EXTRA_VIALS.get());

        return baseCharges + (level * 2);
    }

    default FluidStack readFluid(ItemStack stack){
        return FluidStack.loadFluidStackFromNBT(stack.getOrCreateTag().getCompound("Fluid"));
    }

    default void writeFluid(ItemStack stack, FluidStack fluid){
        stack.getOrCreateTag().put("Fluid", fluid.writeToNBT(new CompoundTag()));
    }

    default int getCurrentFillLevel(ItemStack stack){
        return readFluid(stack).getAmount();
    }

    default String formatDuration(int ticks) {
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public static String toRoman(int number) {
        if (number < 1 || number > 10) return String.valueOf(number);
        String[] romanNumerals = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        return romanNumerals[number - 1];
    }

    default void tooltipMaker(List<Component> tooltip, ItemStack stack, @Nullable RegistryAccess registryAccess) {
        FluidStack fluid = readFluid(stack);
        SyringeFluidType type = SyringeFluidTypeManager.fromFluid(fluid, registryAccess);
        List<MobEffectInstance> effects = SyringeFluidTypeManager.getEffects(type, fluid);

        // empty
        if (stack.getTag() == null || readFluid(stack).isEmpty()) {
            tooltip.add(Component.translatable("bloodisfuel.tooltip.empty").withStyle(ChatFormatting.GRAY));
            return;
        }

        // fluid and amount
        tooltip.add(CreateLang.fluidName(fluid).component()
                .withStyle(ChatFormatting.GRAY)
                .append(" ")
                .append(CreateLang.number(fluid.getAmount()).style(ChatFormatting.GOLD).component())
                .append(Component.translatable("create.generic.unit.millibuckets").withStyle(ChatFormatting.GOLD))
                .append(Component.literal(" / "))
                .append(CreateLang.number(getCapacity(stack)).style(ChatFormatting.GRAY).component())
                .append(Component.translatable("create.generic.unit.millibuckets").withStyle(ChatFormatting.GRAY)));

        // effect tooltip
        if (!effects.isEmpty()) {
            effects.forEach(effect -> {
                Component effectName = Component.translatable(effect.getDescriptionId())
                        .withStyle(effect.getEffect().isBeneficial() ? ChatFormatting.GREEN : ChatFormatting.RED);
                Component level = Component.literal(" " + toRoman(effect.getAmplifier() + 1))
                        .withStyle(ChatFormatting.GOLD);
                Component duration = Component.literal(" (" + formatDuration(effect.getDuration()) + ")")
                        .withStyle(ChatFormatting.GRAY);

                tooltip.add(Component.literal("• ").withStyle(ChatFormatting.GRAY)
                        .append(Component.translatable("bloodisfuel.tooltip.effect").withStyle(ChatFormatting.GRAY))
                        .append(": ")
                        .append(effectName)
                        .append(level)
                        .append(duration));
            });
        }
    }

    default FluidHandlerItemStack getFluidHandler(ItemStack stack) {
        return new ToolItemFluidHandler(stack, getCapacity(stack), this::readFluid, this::writeFluid);
    }

    class ToolItemFluidHandler extends FluidHandlerItemStack{
        BiConsumer<ItemStack, FluidStack> write;
        Function<ItemStack, FluidStack> read;
        public ToolItemFluidHandler(ItemStack container, int capacity, Function<ItemStack, FluidStack> read, BiConsumer<ItemStack, FluidStack> write) {
            super(container, capacity);
            this.write = write;
            this.read = read;
        }
        @Override
        public FluidStack getFluid() {
            return read.apply(container);
        }
        @Override
        protected void setFluid(FluidStack fluid) {
            write.accept(container, fluid);
        }
    }

    class FuelItems extends Item {
        private int burnTime = 0;

        public FuelItems(Item.Properties pProperties, int burnTime) {
            super(pProperties);
            this.burnTime = burnTime;
        }

        @Override
        public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType){
            return this.burnTime;
        }
    }
}
