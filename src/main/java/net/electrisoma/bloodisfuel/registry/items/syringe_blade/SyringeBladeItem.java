package net.electrisoma.bloodisfuel.registry.items.syringe_blade;

import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.infrastructure.data.entries.BSyringeFluidTypes;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap;

import java.util.List;

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

    // we cant just have the item not have a cooldown or anything, that would be unbalanced
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (BTags.BItemTags.SYRINGE_BLADE.matches(stack) && entity instanceof Player player && isSelected) {
            isOnCooldown = player.getCooldowns().isOnCooldown(stack.getItem());
            offHandPower = BTags.BItemTags.SYRINGE_BLADE.matches(player.getOffhandItem().getItem());
        }
    }

    // attributes and stuff
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != EquipmentSlot.MAINHAND) return super.getAttributeModifiers(slot, stack);

        RegistryAccess access = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.registryAccess() : null;
        CombatContext ctx = getFluidCombatContext(stack, access);

        if (!ctx.canAttack()) return ImmutableMultimap.of();
        return ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 6.0,
                        AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4,
                        AttributeModifier.Operation.ADDITION))
                .build();
    }

    // what happens when the player attacks mobs
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof Player player)) return false;

        RegistryAccess access = attacker.level().registryAccess();
        CombatContext ctx = getFluidCombatContext(stack, access);

        // grab fluid if empty
        if (ctx.fluid().isEmpty()) {
            SyringeFluidType matchedType = getMatchingFluid(target, access);
            if (matchedType == null) matchedType = getFallback(access);
            if (matchedType != null) {
                writeFluid(stack, new FluidStack(SyringeFluidTypeManager.getFluidFor(matchedType), getCapacity(stack)));
                target.hurt(player.damageSources().playerAttack(player), 2.0F);
                return true;
            }
        }

        // if not empty, use what it has to hurt the enemy
        if (ctx.fluid().getAmount() < ctx.useAmount()) {
            target.hurt(player.damageSources().playerAttack(player), 2.0F);
            return true;
        }

        ctx.fluid().shrink(ctx.useAmount());
        writeFluid(stack, ctx.fluid());

        // effects
        SyringeFluidTypeManager.getEffects(ctx.type(), ctx.fluid())
                .forEach(effect -> target.addEffect(new MobEffectInstance(effect)));

        return super.hurtEnemy(stack, target, attacker);
    }

    // matching fluid for mobs
    private SyringeFluidType getMatchingFluid(LivingEntity target, RegistryAccess access) {
        return SyringeFluidTypeManager.getAll(access).stream()
                .filter(type -> ForgeRegistries.ENTITY_TYPES.getHolder(target.getType())
                        .map(holder -> type.mobs().map(mobSet -> mobSet.contains(holder)).orElse(false))
                        .orElse(false))
                .findFirst().orElse(null);
    }

    // fallback fluid for mobs
    private SyringeFluidType getFallback(RegistryAccess access) {
        return access.registryOrThrow(BRegistries.SYRINGE_BLADE_FLUIDS)
                .getOptional(BSyringeFluidTypes.FALLBACK)
                .orElse(null);
    }

    // helper method to assist with the charges
    public static int getUseAmount(int capacity, int charges) {
        return (int) Math.ceil((double) capacity / charges);
    }

    // tooltip stuff, like the amount counter
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltipMaker(tooltip, stack, level != null ? level.registryAccess() : null);
    }

    // bar color stuff based on fluids
    @Override
    public int getBarColor(ItemStack stack) {
        Level level = Minecraft.getInstance().player != null ? Minecraft.getInstance().player.level() : null;
        return SyringeFluidTypeManager.getColor(
                SyringeFluidTypeManager.fromFluid(readFluid(stack), level != null ? level.registryAccess() : null),
                readFluid(stack));
    }

    // bar visibility based on the presence of fluids
    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getCurrentFillLevel(stack) > 0;
    }

    // bar progress based on fluids amount
    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13 * (getCurrentFillLevel(stack) / (float) getCapacity(stack)));
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

    // is it enchantable? :shrug:
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    // lets it be used as a fluids container
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return getFluidHandler(stack);
    }

    // arm pose stuff, i want to mess with this later
    // currently not doing anything
    @Override
    @Nullable
    public ArmPose getArmPose(ItemStack stack, AbstractClientPlayer player, InteractionHand hand) {
        if (!player.swinging) return ArmPose.ITEM;
        return null;
    }

    // helper method and record for context to assist hurtEnemy and getAttributeModifiers
    private record CombatContext(FluidStack fluid, SyringeFluidType type, int useAmount, boolean canAttack, boolean onlyBeneficial) {}
    private CombatContext getFluidCombatContext(ItemStack stack, @Nullable RegistryAccess access) {
        FluidStack fluidStack = readFluid(stack);
        SyringeFluidType type = SyringeFluidTypeManager.fromFluid(fluidStack, access);
        int capacity = getCapacity(stack);
        int charges = getChargeCount(stack);
        int useAmount = getUseAmount(capacity, charges);
        int currentFill = getCurrentFillLevel(stack);

        List<MobEffectInstance> effects = SyringeFluidTypeManager.getEffects(type, fluidStack);
        boolean onlyBeneficial = !effects.isEmpty() && effects.stream().allMatch(effect -> effect.getEffect().isBeneficial());
        boolean canAttack = currentFill >= useAmount && !onlyBeneficial;

        return new CombatContext(fluidStack, type, useAmount, canAttack, onlyBeneficial);
    }
}