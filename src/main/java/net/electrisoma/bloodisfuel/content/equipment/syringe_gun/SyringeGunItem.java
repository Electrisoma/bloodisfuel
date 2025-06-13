package net.electrisoma.bloodisfuel.content.equipment.syringe_gun;

import com.simibubi.create.content.equipment.zapper.ShootableGadgetItemMethods;
import net.electrisoma.bloodisfuel.api.utils.SyringeUtils;

import com.simibubi.create.AllEnchantments;
import com.simibubi.create.foundation.item.CustomArmPoseItem;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;


public class SyringeGunItem extends ProjectileWeaponItem
        implements CustomArmPoseItem, SyringeUtils {
    public int cooldown = 40;

    public SyringeGunItem(Properties properties) {
        super(properties);
    }

    @Override public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            drainVial(stack, player, level);
            return InteractionResultHolder.sidedSuccess(stack, false);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }
    @Override public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (!(entityLiving instanceof Player player)) return;

        if (readFluid(stack).isEmpty()) {
            extractFromSelf(stack, level, player);
            return;
        }

        FluidStack fluid = readFluid(stack);
        int useAmount = getUseAmount(stack);
        if (fluid.getAmount() < useAmount) return;
        InteractionHand hand = player.getUsedItemHand();

        if (!level.isClientSide) {
            Vec3 velocity = player.getLookAngle().scale(2.5);
            SyringeProjectileEntity projectile = new SyringeProjectileEntity(level, player, velocity.x, velocity.y, velocity.z);
            projectile.setFluid(fluid.copy());
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 5F, 1.0F);
            level.addFreshEntity(projectile);

            fluid.shrink(useAmount);
            writeFluid(stack, fluid);

            ShootableGadgetItemMethods.applyCooldown(player, stack, hand,
                    s -> s.getItem() instanceof SyringeGunItem, this.cooldown);
        }
    }
    @Override public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player player) {
        return false;
    }
    @Override public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }
    @Override public int getDefaultProjectileRange() {
        return 15;
    }

    @Override public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || newStack.getItem() != oldStack.getItem();
    }
    @Override public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }
    @Override public HumanoidModel.ArmPose getArmPose(ItemStack stack, AbstractClientPlayer player, InteractionHand hand) {
        return (player.isUsingItem() && player.getUseItem() == stack && player.getUsedItemHand() == hand)
                ? HumanoidModel.ArmPose.CROSSBOW_CHARGE : HumanoidModel.ArmPose.ITEM;
    }
    @Override public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override public boolean isEnchantable(ItemStack stack) {
        return true;
    }
    @Override public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == AllEnchantments.CAPACITY.get()) return true;

        return super.canApplyAtEnchantingTable(stack, enchantment);
    }
    @Override public boolean isFoil(ItemStack stack) {
        return false;
    }

    @Override public boolean isBarVisible(ItemStack stack) {
        return SyringeUtils.super.isBarVisible(stack);
    }
    @Override public int getBarWidth(ItemStack stack) {
        return SyringeUtils.super.getBarWidth(stack);
    }
    @Override public int getBarColor(ItemStack stack) {
        return SyringeUtils.super.getBarColor(stack);
    }
    @Override public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltipMaker(tooltip, stack);
        itemToolTipMaker(tooltip, stack, level != null ? level.registryAccess() : null);
        projectileTooltipMaker(tooltip, stack, level != null ? level.registryAccess() : null);
    }
    @Override @OnlyIn(Dist.CLIENT) public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new SyringeGunItemRenderer()));
    }
    @Override public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return getFluidHandler(stack);
    }
}