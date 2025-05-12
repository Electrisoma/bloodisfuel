package net.electrisoma.bloodisfuel.registry.items.syringe_blade;

import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import net.electrisoma.bloodisfuel.api.BurningData;
import net.electrisoma.bloodisfuel.api.equipment.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.equipment.SyringeFluidTypeManager;
import net.electrisoma.bloodisfuel.registry.BAdvancements;
import net.electrisoma.bloodisfuel.registry.BTags;
import net.electrisoma.bloodisfuel.registry.BEnchantments;
import net.electrisoma.bloodisfuel.registry.items.ItemUtils;
import net.electrisoma.bloodisfuel.registry.enchantments.ChargesEnchantment;
import com.simibubi.create.AllEnchantments;
import com.simibubi.create.foundation.item.CustomArmPoseItem;
import com.simibubi.create.content.equipment.armor.CapacityEnchantment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;


@SuppressWarnings("all")
public class SyringeBladeItem extends SwordItem
        implements CustomArmPoseItem, CapacityEnchantment.ICapacityEnchantable,
        ChargesEnchantment.ICapacityEnchantable, ItemUtils {

    private boolean isOnCooldown;
    private boolean offHandPower;

    public SyringeBladeItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    // attributes and stuff
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != EquipmentSlot.MAINHAND) return super.getAttributeModifiers(slot, stack);
        CombatContext ctx = getCombatContext(stack, Minecraft.getInstance().level != null ?
                Minecraft.getInstance().level.registryAccess() : null);
        if (!ctx.canAttack()) return ImmutableMultimap.of();
        return ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID,
                        "Weapon modifier", 6.0, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID,
                        "Weapon modifier", -2.4, AttributeModifier.Operation.ADDITION))
                .build();
    }

    // we cant just have the item not have a cooldown or anything, that would be unbalanced
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (BTags.BItemTags.SYRINGE_BLADE.matches(stack) && entity instanceof Player player && isSelected) {
            isOnCooldown = player.getCooldowns().isOnCooldown(stack.getItem());
            offHandPower = BTags.BItemTags.SYRINGE_BLADE.matches(player.getOffhandItem().getItem());
        }
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
            } return InteractionResultHolder.pass(stack);
        }

        if (player.getCooldowns().isOnCooldown(this)) return InteractionResultHolder.pass(stack);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    // self inflicted charge
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (!(entityLiving instanceof Player player)) return;

        int useDuration = getUseDuration(stack) - player.getUseItemRemainingTicks();
        if (useDuration < 20) return;

        RegistryAccess access = level.registryAccess();
        CombatContext ctx = getCombatContext(stack, access);

        if (ctx.fluid().isEmpty()) {
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

        if (ctx.fluid().getAmount() < ctx.useAmount()) return;

        if (!level.isClientSide) {
            if (SyringeFluidTypeManager.isMilk(ctx.fluid().getFluid(), access)) {
                player.removeAllEffects();
                BAdvancements.LACTOSE_TOLERANT.awardTo((ServerPlayer) player);
            }

            SyringeFluidTypeManager.getEffects(ctx.type(), ctx.fluid())
                    .forEach(effect -> player.addEffect(new MobEffectInstance(effect)));

            if (ctx.type().hasBurning()) {
                BurningData burningData = ctx.type().burning().get();
                applyBurningEffect(player, burningData);
            }

            ctx.fluid().shrink(ctx.useAmount());
            writeFluid(stack, ctx.fluid());
            playSound(level, player, SoundEvents.PLAYER_ATTACK_CRIT);
            player.getCooldowns().addCooldown(this, 40);
        }

        isOnCooldown = false;
    }

    // what happens when the player attacks mobs
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof Player player)) return false;

        RegistryAccess access = attacker.level().registryAccess();
        CombatContext ctx = getCombatContext(stack, access);

        if (ctx.fluid().isEmpty()) {
            SyringeFluidType matchedType = getMatchingFluid(target, access);
            if (matchedType == null) matchedType = getFallback(access);
            if (matchedType != null) {
                writeFluid(stack, new FluidStack(SyringeFluidTypeManager.getFluidFor(matchedType), getCapacity(stack)));
                target.hurt(player.damageSources().playerAttack(player), 2.0F);
                spawnBloodParticles(attacker.level(), target, stack);
                return true;
            }
        }

        if (ctx.fluid().getAmount() < ctx.useAmount()) {
            target.hurt(player.damageSources().playerAttack(player), 2.0F);
            return true;
        }

        if (SyringeFluidTypeManager.isMilk(ctx.fluid().getFluid(), access)) {
            target.removeAllEffects();
        }

        if (target instanceof Player targetPlayer) {
            if (ctx.onlyBeneficial()) BAdvancements.DOCTOR.awardTo((ServerPlayer) player);
            else BAdvancements.MEDICAL_MALPRACTICE.awardTo((ServerPlayer) player);
        }

        SyringeFluidTypeManager.getEffects(ctx.type(), ctx.fluid())
                .forEach(effect -> target.addEffect(new MobEffectInstance(effect)));

        if (ctx.type().hasBurning()) {
            BurningData burningData = ctx.type().burning().get();
            applyBurningEffect(target, burningData);
        }

        ctx.fluid().shrink(ctx.useAmount());
        writeFluid(stack, ctx.fluid());

        return super.hurtEnemy(stack, target, attacker);
    }

    // tooltip stuff, like the amount counter
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltipMaker(tooltip, stack, level != null ? level.registryAccess() : null);
    }

    // bar color stuff based on fluids
    @Override
    public int getBarColor(ItemStack stack) {
        return ItemUtils.super.getBarColor(stack);
    }

    // bar visibility based on the presence of fluids
    @Override
    public boolean isBarVisible(ItemStack stack) {
        return ItemUtils.super.isBarVisible(stack);
    }

    // bar progress based on fluids amount
    @Override
    public int getBarWidth(ItemStack stack) {
        return ItemUtils.super.getBarWidth(stack);
    }

    // is it enchantable? :shrug:
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    // valid enchantments
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == BEnchantments.EXTRA_VIALS.get()) return true;
        if (enchantment == AllEnchantments.CAPACITY.get()) return true;
        if (enchantment == Enchantments.SHARPNESS) return true;
        if (enchantment == Enchantments.FIRE_ASPECT) return true;

        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    // lets it be used as a fluids container
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return getFluidHandler(stack);
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
    @Nullable
    public ArmPose getArmPose(ItemStack stack, AbstractClientPlayer player, InteractionHand hand) {
        return (player.isUsingItem() && player.getUseItem() == stack && player.getUsedItemHand() == hand)
                ? ArmPose.BOW_AND_ARROW : ArmPose.ITEM;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new SyringeBladeItemRenderer()));
    }
}
