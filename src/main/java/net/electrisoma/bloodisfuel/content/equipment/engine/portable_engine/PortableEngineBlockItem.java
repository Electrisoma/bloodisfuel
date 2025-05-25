package net.electrisoma.bloodisfuel.content.equipment.engine.portable_engine;

import net.electrisoma.bloodisfuel.api.utils.SyringeUtils;

import com.simibubi.create.AllEnchantments;
import com.simibubi.create.content.equipment.armor.CapacityEnchantment;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;
import javax.annotation.Nullable;


public class PortableEngineBlockItem extends BlockItem implements CapacityEnchantment.ICapacityEnchantable, SyringeUtils {
    public PortableEngineBlockItem(Block block, Properties properties) {
        super(block, properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            drainVial(stack, player, level);
            return InteractionResultHolder.sidedSuccess(stack, false);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override public boolean isEnchantable(ItemStack stack) {
        return true;
    }
    @Override public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if(enchantment == AllEnchantments.CAPACITY.get()) return true;
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override @OnlyIn(Dist.CLIENT) public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltipMaker(tooltip, stack);
    }
    @Override public int getBarColor(ItemStack stack) {
        return SyringeUtils.super.getBarColor(stack);
    }
    @Override public boolean isBarVisible(ItemStack stack) {
        return SyringeUtils.super.isBarVisible(stack);
    }
    @Override public int getBarWidth(ItemStack stack) {
        return SyringeUtils.super.getBarWidth(stack);
    }
    @Override public void writeFluid(ItemStack stack, FluidStack fluid) {
        ListTag list = new ListTag();
        CompoundTag tankContent = new CompoundTag();
        tankContent.put("TankContent", fluid.writeToNBT(new CompoundTag()));
        list.add(tankContent);
        CompoundTag tag = new CompoundTag();
        tag.put("Tanks", list);
        stack.getOrCreateTag().put("BlockEntityTag", tag);
    }
    @Override public FluidStack readFluid(ItemStack stack) {
        return FluidStack.loadFluidStackFromNBT(stack.getOrCreateTag().getCompound("BlockEntityTag")
                .getList("Tanks", Tag.TAG_COMPOUND).getCompound(0).getCompound("TankContent"));
    }
    @Override public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return getFluidHandler(stack);
    }
}
