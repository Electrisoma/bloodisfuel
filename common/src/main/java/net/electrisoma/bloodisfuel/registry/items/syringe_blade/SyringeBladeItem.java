package net.electrisoma.bloodisfuel.registry.items.syringe_blade;

import net.electrisoma.bloodisfuel.registry.BTags;

import com.simibubi.create.content.equipment.armor.CapacityEnchantment;
import com.simibubi.create.foundation.item.CustomArmPoseItem;


import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;


import static net.minecraft.client.model.HumanoidModel.*;


public class SyringeBladeItem extends SwordItem implements CustomArmPoseItem, CapacityEnchantment.ICapacityEnchantable {
    boolean isOnCooldown;
    boolean offHandPower;

    public SyringeBladeItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, (int) pAttackDamageModifier, pAttackSpeedModifier, pProperties);
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
//        else if (pStack.getTag() != null) {
//            CompoundTag Blood = pStack.getTag().getCompound("Fluid");
//
//            FluidStack fStack = FluidStack.loadFluidStackFromNBT(Blood);
//            if (fStack.isEmpty()) {
//                MobEffectInstance poison = pTarget.getEffect(MobEffects.POISON);
//                if (poison != null) {
//                    pTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 100 + poison.getDuration()));
//                    return false;
//                }
//                pTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 100));
//            }
//        }
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

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public ArmPose getArmPose(ItemStack stack, AbstractClientPlayer player, InteractionHand hand) {
        if (!player.swinging) {
            return ArmPose.CROSSBOW_HOLD;
        }
        return null;
    }
}
