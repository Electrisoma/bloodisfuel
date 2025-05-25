package net.electrisoma.bloodisfuel.content.equipment.syringe_blade;

import net.electrisoma.bloodisfuel.api.utils.SyringeUtils;
import net.electrisoma.bloodisfuel.registry.BTags;
import net.electrisoma.bloodisfuel.registry.enchantments.ChargesEnchantment;

import com.simibubi.create.AllEnchantments;
import com.simibubi.create.foundation.item.CustomArmPoseItem;
import com.simibubi.create.content.equipment.armor.CapacityEnchantment;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nullable;


@SuppressWarnings("all")
public class SyringeBladeItem extends SwordItem
        implements CustomArmPoseItem, CapacityEnchantment.ICapacityEnchantable,
        ChargesEnchantment.ICapacityEnchantable, SyringeUtils {
    private boolean isOnCooldown;
    private boolean offHandPower;

    public SyringeBladeItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != EquipmentSlot.MAINHAND)
            return super.getAttributeModifiers(slot, stack);

        RegistryAccess access = Minecraft.getInstance().level != null
                ? Minecraft.getInstance().level.registryAccess()
                : null;

        CombatContext ctx = getCombatContext(stack, access);
        if (!ctx.canAttack()) return ImmutableMultimap.of();

        Optional<Float> optDamage = ctx.type() != null ? ctx.type().damage() : Optional.empty();
        Optional<Float> optSpeed = ctx.type() != null ? ctx.type().attackSpeed() : Optional.empty();

        if (optDamage.isEmpty() && optSpeed.isEmpty()) return ImmutableMultimap.of();

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        optDamage.ifPresent(damage ->
                builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID,
                        "Weapon modifier", (double) damage.floatValue(), AttributeModifier.Operation.ADDITION)));

        optSpeed.ifPresent(speed ->
                builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID,
                        "Weapon modifier", (double) speed.floatValue(), AttributeModifier.Operation.ADDITION)));

        return builder.build();
    }
    @Override public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (BTags.BItemTags.SYRINGE_BLADE.matches(stack) && entity instanceof Player player && isSelected) {
            isOnCooldown = player.getCooldowns().isOnCooldown(stack.getItem());
            offHandPower = BTags.BItemTags.SYRINGE_BLADE.matches(player.getOffhandItem().getItem());
        }
    }
    @Override public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            drainVial(stack, player, level);
            return InteractionResultHolder.sidedSuccess(stack, false);
        }

        if (player.getCooldowns().isOnCooldown(this)) return InteractionResultHolder.pass(stack);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }
    @Override public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (!(entityLiving instanceof Player player)) return;

        int useDuration = getUseDuration(stack) - player.getUseItemRemainingTicks();
        if (useDuration < 20) return;

        if (readFluid(stack).isEmpty()) {
            extractFromSelf(stack, level, player);
            return;
        }

        injectSelf(stack, level, player);
        player.getCooldowns().addCooldown(this, 40);
        isOnCooldown = false;
    }
    @Override public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof Player player)) return false;

        RegistryAccess access = attacker.level().registryAccess();

        if (extractFromTarget(stack, target, player, access)) return true;
        if (injectIntoTarget(stack, target, player, access)) return true;

        return super.hurtEnemy(stack, target, attacker);
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
    @Override @OnlyIn(Dist.CLIENT) public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltipMaker(tooltip, stack);
        itemToolTipMaker(tooltip, stack, level != null ? level.registryAccess() : null);
    }
    @Override @OnlyIn(Dist.CLIENT) public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new SyringeBladeItemRenderer()));
    }
    @Override public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return getFluidHandler(stack);
    }

    @Override public boolean isEnchantable(ItemStack stack) {
        return true;
    }
    @Override public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == AllEnchantments.CAPACITY.get()) return true;
        if (enchantment == Enchantments.SHARPNESS) return true;
        if (enchantment == Enchantments.FIRE_ASPECT) return true;

        return super.canApplyAtEnchantingTable(stack, enchantment);
    }
    @Override public boolean isFoil(ItemStack stack) {
        return false;
    }


    @Override public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }
    @Override public int getUseDuration(ItemStack stack) {
        return 72000;
    }
    @Override @Nullable public ArmPose getArmPose(ItemStack stack, AbstractClientPlayer player, InteractionHand hand) {
        return (player.isUsingItem() && player.getUseItem() == stack && player.getUsedItemHand() == hand)
                ? ArmPose.BOW_AND_ARROW : ArmPose.ITEM;
    }
}
