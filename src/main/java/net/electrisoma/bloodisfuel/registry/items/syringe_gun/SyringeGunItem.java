package net.electrisoma.bloodisfuel.registry.items.syringe_gun;

import com.simibubi.create.foundation.item.CustomArmPoseItem;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import net.electrisoma.bloodisfuel.registry.items.ItemUtils;
import net.electrisoma.bloodisfuel.api.equipment.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.equipment.SyringeFluidTypeManager;
import net.electrisoma.bloodisfuel.registry.items.syringe_blade.SyringeBladeItemRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SyringeGunItem extends ProjectileWeaponItem implements CustomArmPoseItem, ItemUtils {

    public SyringeGunItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            if (!level.isClientSide && !readFluid(stack).isEmpty()) {
                spawnDrainingParticles(level, player, stack, 5);
                writeFluid(stack, FluidStack.EMPTY);
                playSound(level, player, SoundEvents.BOTTLE_EMPTY);
                return InteractionResultHolder.sidedSuccess(stack, false);
            }
            return InteractionResultHolder.pass(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (!(entityLiving instanceof Player player)) return;

        RegistryAccess access = level.registryAccess();
        FluidStack fluid = readFluid(stack);

        // No fluid present, attempt to extract from player
        if (fluid.isEmpty()) {
            SyringeFluidType selfType = getMatchingFluid(player, access);
            if (selfType == null) selfType = getFallback(access);
            if (selfType != null) {
                writeFluid(stack, new FluidStack(SyringeFluidTypeManager.getFluidFor(selfType), getCapacity(stack)));
                player.hurt(player.damageSources().generic(), 2.0F);
                playSound(level, player, SoundEvents.PLAYER_HURT);
                spawnBloodParticles(level, player, stack);
            }
            return;
        }

        int capacity = getCapacity(stack);
        int charges = getChargeCount(stack);
        int useAmount = getUseAmount(capacity, charges);
        if (fluid.getAmount() < useAmount) return;

        if (!level.isClientSide) {
            SyringeProjectileEntity projectile = new SyringeProjectileEntity(level, player);
            projectile.setFluid(fluid.copy());
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
            level.addFreshEntity(projectile);

            fluid.shrink(useAmount);
            writeFluid(stack, fluid);

            spawnBloodParticles(level, player, stack);

            player.getCooldowns().addCooldown(this, 20);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltipMaker(tooltip, stack, level != null ? level.registryAccess() : null);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public HumanoidModel.ArmPose getArmPose(ItemStack stack, AbstractClientPlayer player, InteractionHand hand) {
        return HumanoidModel.ArmPose.ITEM;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return getFluidHandler(stack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return ItemUtils.super.isBarVisible(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return ItemUtils.super.getBarWidth(stack);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return ItemUtils.super.getBarColor(stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new SyringeGunItemRenderer()));
    }
}
