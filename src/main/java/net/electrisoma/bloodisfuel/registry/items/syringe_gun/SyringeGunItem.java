package net.electrisoma.bloodisfuel.registry.items.syringe_gun;


import net.electrisoma.bloodisfuel.api.equipment.ItemUtils;

import com.simibubi.create.foundation.item.CustomArmPoseItem;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
import java.util.Optional;
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
            drainVial(stack, player, level);
            return InteractionResultHolder.sidedSuccess(stack, false);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (!(entityLiving instanceof Player player)) return;

        if (readFluid(stack).isEmpty()) {
            extractFromSelf(stack, level, player);
            return;
        }

        FluidStack fluid = readFluid(stack);
        int capacity = getCapacity(stack);
        int charges = getChargeCount(stack);
        int useAmount = getUseAmount(capacity, charges);

        if (fluid.getAmount() < useAmount) return;

        if (!level.isClientSide) {
            double velocityX = player.getLookAngle().x * 2.5;
            double velocityY = player.getLookAngle().y * 2.5;
            double velocityZ = player.getLookAngle().z * 20;

            SyringeProjectileEntity projectile = new SyringeProjectileEntity(level, player, velocityX, velocityY, velocityZ);
            projectile.setFluid(fluid.copy());
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 5F, 1.0F);
            level.addFreshEntity(projectile);

            fluid.shrink(useAmount);
            writeFluid(stack, fluid);
            player.getCooldowns().addCooldown(this, 20);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltipMaker(tooltip, stack, level != null ? level.registryAccess() : null);

        RegistryAccess access = Minecraft.getInstance().level != null
                ? Minecraft.getInstance().level.registryAccess()
                : null;

        CombatContext ctx = getCombatContext(stack, access);

        if (ctx.canAttack()) {
            Optional<Float> optDamage = ctx.type() != null ? ctx.type().damage() : Optional.empty();
            if (optDamage.isPresent()) {
                float damage = optDamage.get();
                if (Minecraft.getInstance().player != null) {
                    float playerAttackDamage = (float) Minecraft.getInstance().player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    damage += playerAttackDamage;
                }
                tooltip.add(CommonComponents.EMPTY);
                tooltip.add(Component.translatable("item.modifiers.mainhand")
                        .withStyle(ChatFormatting.GRAY));

                String damageText = (damage % 1.0f == 0.0f)
                        ? String.valueOf((int) damage)
                        : String.format("%.2f", damage);

                tooltip.add(Component.literal(" ")
                        .append(Component.literal(damageText)
                        .append(Component.literal(" "))
                        .append(Component.translatable("bloodisfuel.tooltip.syringe_gun.damage"))
                        .withStyle(ChatFormatting.DARK_GREEN)));
            }
        }
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
        return (player.isUsingItem() && player.getUseItem() == stack && player.getUsedItemHand() == hand)
                ? HumanoidModel.ArmPose.CROSSBOW_CHARGE : HumanoidModel.ArmPose.ITEM;
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
