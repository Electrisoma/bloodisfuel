package net.electrisoma.bloodisfuel.registry.items.syringe_blade;

import net.electrisoma.bloodisfuel.registry.BTags;
import net.electrisoma.bloodisfuel.registry.items.ItemUtils;

import com.simibubi.create.AllEnchantments;
import com.simibubi.create.content.equipment.armor.CapacityEnchantment;
import com.simibubi.create.foundation.item.CustomArmPoseItem;

import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

//all these methods are really giving me a headache lol
@SuppressWarnings("all")
public class SyringeBladeItem extends SwordItem implements CustomArmPoseItem, CapacityEnchantment.ICapacityEnchantable, ItemUtils {
    boolean isOnCooldown;
    boolean offHandPower;

    public SyringeBladeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (BTags.BItemTags.SYRINGE_BLADE.matches(pStack)) {
            if (pEntity instanceof Player pPlayer && pIsSelected) {
                isOnCooldown = pPlayer.getCooldowns().isOnCooldown(pPlayer.getMainHandItem().getItem());
                offHandPower = BTags.BItemTags.SYRINGE_BLADE.matches(pPlayer.getOffhandItem().getItem());
            }
        }
    }

    //enemy debuffs and cooldown trigger
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (BTags.BItemTags.SYRINGE_BLADE.matches(pStack)) {
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

    //tooltip for fluid amount
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltip, tooltipFlag);
        tooltipMaker(tooltip, stack);
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
        return getCurrentFillLevel(stack) > 0;
    }

    //bar width based on how much fluid
    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13 * (getCurrentFillLevel(stack) / (float) getCapacity(stack)));
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

        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    //enchantable
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    //filling
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return getFluidHandler(stack);
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