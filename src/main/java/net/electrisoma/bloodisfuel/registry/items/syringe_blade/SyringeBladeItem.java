package net.electrisoma.bloodisfuel.registry.items.syringe_blade;

import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.infrastructure.data.entries.BSyringeFluidTypes;
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
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResultHolder;
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

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap;
import org.joml.Vector3f;

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

    // attributes and stuff
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != EquipmentSlot.MAINHAND) return super.getAttributeModifiers(slot, stack);
        CombatContext ctx = getFluidCombatContext(stack, Minecraft.getInstance().level != null ?
                Minecraft.getInstance().level.registryAccess() : null);
        if (!ctx.canAttack) return ImmutableMultimap.of();
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
                writeFluid(stack, FluidStack.EMPTY);
                playSound(level, player, SoundEvents.BOTTLE_EMPTY);
                spawnDrainingParticles(level, player, stack, 5);
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
        CombatContext ctx = getFluidCombatContext(stack, access);

        if (ctx.fluid().isEmpty()) {
            SyringeFluidType selfType = getMatchingFluid(player, access);
            if (selfType == null) selfType = getFallback(access);
            if (selfType != null) {
                writeFluid(stack, new FluidStack(SyringeFluidTypeManager.getFluidFor(selfType), getCapacity(stack)));
                player.hurt(player.damageSources().generic(), 2.0F);
                playSound(level, player, SoundEvents.PLAYER_HURT);
                spawnBloodParticles(level, player, stack);
            } return;
        }
        if (ctx.fluid().getAmount() < ctx.useAmount()) return;

        if (!level.isClientSide) {
            Fluid fluid = ctx.fluid().getFluid();
            boolean isMilk = SyringeFluidTypeManager.isMilk(fluid, access);
            boolean isPotion = SyringeFluidTypeManager.isPotion(fluid, access);

            if (isMilk) {
                player.removeAllEffects();
                BAdvancements.LACTOSE_TOLERANT.awardTo((ServerPlayer) player);
            }

            SyringeFluidTypeManager.getEffects(ctx.type(), ctx.fluid())
                    .forEach(effect -> player.addEffect(new MobEffectInstance(effect)));

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
        CombatContext ctx = getFluidCombatContext(stack, access);

        // grab fluid if empty
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

        // if not empty, use what it has to hurt the enemy
        if (ctx.fluid().getAmount() < ctx.useAmount()) {
            target.hurt(player.damageSources().playerAttack(player), 2.0F);
            return true;
        }

        Fluid fluid = ctx.fluid().getFluid();
        boolean isMilk = SyringeFluidTypeManager.isMilk(fluid, access);

        // effects
        if (isMilk) target.removeAllEffects();

        if (target instanceof Player targetPlayer) {
            if (ctx.onlyBeneficial) BAdvancements.DOCTOR.awardTo((ServerPlayer) player);
            else BAdvancements.MEDICAL_MALPRACTICE.awardTo((ServerPlayer) player);
        }

        SyringeFluidTypeManager.getEffects(ctx.type(), ctx.fluid())
                .forEach(effect -> target.addEffect(new MobEffectInstance(effect)));

        ctx.fluid().shrink(ctx.useAmount());
        writeFluid(stack, ctx.fluid());

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

    // fallback fluid for mobs without set fluids
    private SyringeFluidType getFallback(RegistryAccess access) {
        return access.registryOrThrow(BRegistries.SYRINGE_BLADE_FLUIDS)
                .getOptional(BSyringeFluidTypes.FALLBACK)
                .orElse(null);
    }

    private void spawnDrainingParticles(Level level, Entity entity, ItemStack stack, int count) {
        if (!(level instanceof ServerLevel server)) return;
        int color = getBarColor(stack);
        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        Vector3f particleColor = new Vector3f(r, g, b);
        for (int i = 0; i < count; i++) {
            double dx = (level.random.nextDouble() - 0.5) * 0.3;
            double dy = level.random.nextDouble() * 0.2;
            double dz = (level.random.nextDouble() - 0.5) * 0.3;
            server.sendParticles(new DustParticleOptions(particleColor, 1.0F),
                    entity.getX() + dx, entity.getY() + dy, entity.getZ() + dz,
                    1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    private void spawnBloodParticles(Level level, Entity entity, ItemStack stack) {
        if (!(level instanceof ServerLevel server)) return;
        int color = getBarColor(stack);
        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        Vector3f particleColor = new Vector3f(r, g, b);
        for (int i = 0; i < 10; i++) {
            double dx = (level.random.nextDouble() - 0.5) * 0.5;
            double dy = level.random.nextDouble() * 1.0;
            double dz = (level.random.nextDouble() - 0.5) * 0.5;
            server.sendParticles(new DustParticleOptions(particleColor, 1.0F),
                    entity.getX() + dx, entity.getY() + dy, entity.getZ() + dz,
                    1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    private void playSound(Level level, Entity entity, SoundEvent sound) {
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.PLAYERS, 1.0F, 1.0F);
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
        return SyringeFluidTypeManager.getColor(SyringeFluidTypeManager.fromFluid(readFluid(stack),
                level != null ? level.registryAccess() : null), readFluid(stack));
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

    // helper method to assist with the charges
    public static int getUseAmount(int capacity, int charges) {
        return (int) Math.ceil((double) capacity / charges);
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