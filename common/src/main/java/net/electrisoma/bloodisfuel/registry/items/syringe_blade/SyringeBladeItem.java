package net.electrisoma.bloodisfuel.registry.items.syringe_blade;

import com.simibubi.create.AllEnchantments;
import com.simibubi.create.content.equipment.armor.CapacityEnchantment;
import com.simibubi.create.foundation.item.CustomArmPoseItem;
import com.simibubi.create.foundation.utility.Lang;
import io.github.fabricators_of_create.porting_lib.enchant.CustomEnchantingBehaviorItem;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.item.FluidHandlerItemStack;
import net.electrisoma.bloodisfuel.config.BConfigs;
import net.electrisoma.bloodisfuel.registry.BTags;
import net.electrisoma.bloodisfuel.registry.items.FluidStorageItem;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;


@SuppressWarnings("all")
public class SyringeBladeItem extends SwordItem implements
        CustomArmPoseItem, CapacityEnchantment.ICapacityEnchantable, CustomEnchantingBehaviorItem, FluidStorageItem {

    public static final int BLOOD_CAPACITY = 1000;
    public static final int BLOOD_CAPACITY_ENCHANTMENT = 1000;

    boolean isOnCooldown;
    boolean offHandPower;

    /// WEAPON STUFF

    public SyringeBladeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (BTags.AllItemTags.SYRINGE_BLADE.matches(pStack)) {
            if (pEntity instanceof Player pPlayer && pIsSelected) {
                isOnCooldown = pPlayer.getCooldowns().isOnCooldown(pPlayer.getMainHandItem().getItem());
                offHandPower = BTags.AllItemTags.SYRINGE_BLADE.matches(pPlayer.getOffhandItem().getItem());
            }
        }
    }

    //enemy debuffs and cooldown trigger
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (BTags.AllItemTags.SYRINGE_BLADE.matches(pStack)) {
            if (pAttacker instanceof Player pPlayer && !isOnCooldown) {
                resetCooldown(pStack, pPlayer);
                return true;
            }
            return false;
        }
        else if (pStack.getTag() != null) {
            CompoundTag Blood = pStack.getTag().getCompound("Fluid");

            FluidStack fStack = FluidStack.loadFluidStackFromNBT(Blood);
            if (fStack.isEmpty()) {
                MobEffectInstance poison = pTarget.getEffect(MobEffects.POISON);
                if (poison != null) {
                    pTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 100 + poison.getDuration()));
                    return false;
                }
                pTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 100));
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    //cooldown reset
    public void resetCooldown(ItemStack pStack, Player pPlayer) {
        if (!pPlayer.getCooldowns().isOnCooldown(pPlayer.getMainHandItem().getItem())) {
            pPlayer.getCooldowns().addCooldown(this, getUseDuration(pStack));
        }
        pStack.hurtAndBreak(2, pPlayer, (p_43276_) ->
                p_43276_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    }

    //no attacking on cooldown >:(
    @Override
    public float getDamage() {
        return isOnCooldown ? 0.0F :
                super.getDamage();
    }

    //no attacking block >:(
    @Override
    public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        return !isOnCooldown;
    }

    /// ITEM STUFF

    //tooltip for fluid amount
    public void appendHoverText(ItemStack stack, Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, components, tooltipFlag);
        if(stack.getTag() != null) {
            CompoundTag tankCompound = stack.getTag().getCompound("Fluid");

            FluidStack fStack = FluidStack.loadFluidStackFromNBT(tankCompound);
            if(fStack.isEmpty()){
                components.add(Component.translatable("bloodisfuel.tooltip.empty").withStyle(ChatFormatting.GRAY));
                return;
            }

            var v = EnchantmentHelper.getItemEnchantmentLevel(AllEnchantments.CAPACITY.get(), stack);
            components.add(Lang.fluidName(fStack).component().withStyle(ChatFormatting.GRAY).append(" ").append(Lang.number(fStack.getAmount()).style(ChatFormatting.GOLD).component()).append(Component.translatable("create.generic.unit.millibuckets").withStyle(ChatFormatting.GOLD)).append(Component.literal(" / ")).append(Lang.number(BLOOD_CAPACITY + v*BLOOD_CAPACITY_ENCHANTMENT).style(ChatFormatting.GRAY).component()).append(Component.translatable("create.generic.unit.millibuckets").withStyle(ChatFormatting.GRAY)));
            return;
        }
        components.add(Component.translatable( "bloodisfuel.tooltip.empty").withStyle(ChatFormatting.GRAY));
    }

    //red bar if contains fluid
    @Override
    public int getBarColor(ItemStack stack) {
    return stack
            .getOrCreateTag()
            .getString("Fluid")
            .isEmpty() ? 0xBD3228 : 0xFFFFFF;
    }

    //bar visible based on if there is fluid
    @Override
    public boolean isBarVisible(ItemStack stack) {
        if(stack.getTag() != null) {
            CompoundTag tankCompound = stack.getTag().getCompound("Fluid");
            FluidStack fStack = FluidStack.loadFluidStackFromNBT(tankCompound);
            return !fStack.isEmpty();
        }
        return false;
    }

    //bar width based on how much fluid
    @Override
    public int getBarWidth(ItemStack stack) {
        if(stack.getTag() == null)
            return 0;
        CompoundTag tankCompound = stack.getTag().getCompound("Fluid");
        var v = EnchantmentHelper.getItemEnchantmentLevel(AllEnchantments.CAPACITY.get(), stack);
        return Math.round(13 * Mth.clamp
                (FluidStack.loadFluidStackFromNBT(tankCompound).getAmount()
                        / ((float)BLOOD_CAPACITY
                        + v*BLOOD_CAPACITY_ENCHANTMENT), 0, 1));
    }

    //valid enchantments
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if(enchantment == AllEnchantments.CAPACITY.get())
            return true;
        if(enchantment == Enchantments.SHARPNESS)
            return true;
        if(enchantment == Enchantments.FIRE_ASPECT)
            return true;
        return false;
    }

    //enchantable
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    //filling
    @Override
    public Storage<FluidVariant> getFluidStorage(ItemStack stack, ContainerItemContext context) {
        return new FluidHandlerItemStack(context, BLOOD_CAPACITY);
    }

    //3rd person arm pose
    @Override
    @Nullable
    public ArmPose getArmPose(ItemStack stack, AbstractClientPlayer player, InteractionHand hand) {
        if (!player.swinging) {
            return ArmPose.ITEM;
        }
        return null;
    }
}