package net.electrisoma.bloodisfuel.api.utils;

import net.electrisoma.bloodisfuel.api.data.*;
import net.electrisoma.bloodisfuel.api.equipment.syringe.*;
import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.foundation.data.entries.BExtractionTypes;
import net.electrisoma.bloodisfuel.registry.BAdvancements;
import net.electrisoma.bloodisfuel.foundation.data.entries.BSyringeFluidTypes;

import net.electrisoma.bloodisfuel.registry.BTags;
import net.minecraft.core.*;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@SuppressWarnings({"OptionalGetWithoutIsPresent", "DataFlowIssue", "RedundantSuppression", "unused"})
public interface SyringeUtils extends FluidUtils, CombatContextUtils, TooltipUtils {
    default SyringeExtractionType getMatchingFluid(LivingEntity target, RegistryAccess registryAccess) {
        if (target.getType().is(BTags.BEntityTags.DOES_NOT_DROP_FLUID.tag)) return null;

        var registry = registryAccess.registryOrThrow(Registries.ENTITY_TYPE);
        var keyOpt = registry.getResourceKey(target.getType());
        if (keyOpt.isEmpty()) return null;

        var entityHolderOpt = registry.getHolder(keyOpt.get());
        if (entityHolderOpt.isEmpty()) return null;

        Holder<EntityType<?>> entityHolder = entityHolderOpt.get();

        return SyringeFluidExtractionTypeManager.getAll(registryAccess).stream()
                .filter(type -> type.mobs().stream()
                        .anyMatch(set -> set.contains(entityHolder)))
                .findFirst()
                .orElse(null);
    }
    default SyringeExtractionType getFallbackExtraction(RegistryAccess access) {
        return access.registryOrThrow(BRegistries.SYRINGE_EXTRACTION)
                .getOptional(BExtractionTypes.FALLBACK)
                .orElse(null);
    }

    default boolean tryInsertFluidFromContainer(ItemStack syringeStack, ItemStack incomingStack, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action != ClickAction.SECONDARY)
            return false;

        if (!FluidUtil.getFluidHandler(incomingStack).isPresent())
            return false;

        IFluidHandlerItem source = FluidUtil.getFluidHandler(incomingStack).orElse(null);

        FluidStack simulatedDrain = source.drain(1000, IFluidHandler.FluidAction.SIMULATE);
        if (simulatedDrain.isEmpty() || simulatedDrain.getAmount() < 1000)
            return false;

        FluidStack currentFluid = readFluid(syringeStack);
        if (!currentFluid.isEmpty() && !currentFluid.isFluidEqual(simulatedDrain))
            return false;

        int capacity = getCapacity(syringeStack);
        FluidStack newFluid;

        if (player.isCreative()) {
            newFluid = new FluidStack(simulatedDrain.getFluid(), capacity);
        } else {
            int newAmount = currentFluid.getAmount() + simulatedDrain.getAmount();
            if (newAmount > capacity)
                return false;

            FluidStack drained = source.drain(1000, IFluidHandler.FluidAction.EXECUTE);
            if (drained.isEmpty())
                return false;

            newFluid = drained.copy();
            newFluid.setAmount(newAmount);

            ItemStack containerItem = source.getContainer().copy();
            if (containerItem.isEmpty() && incomingStack.getItem() instanceof BucketItem) {
                containerItem = new ItemStack(Items.BUCKET);
            }

            incomingStack.shrink(1);
            updateContainerReference(player, containerItem, incomingStack, slot, access);
        }

        writeFluid(syringeStack, newFluid);
        updateStackReference(player, syringeStack, slot, access);

        if (player.level().isClientSide) {
            player.level().playSound(player, player.blockPosition(),
                    SoundEvents.BUCKET_EMPTY, player.getSoundSource(), 1.0F, 1.0F);
        }

        return true;
    }

    private void updateStackReference(Player player, ItemStack updatedStack, @Nullable Slot slot, @Nullable SlotAccess access) {
        if (slot != null && ItemStack.isSameItemSameTags(slot.getItem(), updatedStack)) {
            slot.set(updatedStack);
        } else if (access != null) {
            access.set(updatedStack);
        } else {
            if (ItemStack.isSameItemSameTags(player.getMainHandItem(), updatedStack)) {
                player.setItemInHand(InteractionHand.MAIN_HAND, updatedStack);
            } else if (ItemStack.isSameItemSameTags(player.getOffhandItem(), updatedStack)) {
                player.setItemInHand(InteractionHand.OFF_HAND, updatedStack);
            } else {
                for (int i = 0; i < player.getInventory().items.size(); i++) {
                    if (ItemStack.isSameItemSameTags(player.getInventory().items.get(i), updatedStack)) {
                        player.getInventory().items.set(i, updatedStack);
                        break;
                    }
                }
            }
        }
    }
    private void updateContainerReference(Player player, ItemStack containerItem, ItemStack originalStack, @Nullable Slot slot, @Nullable SlotAccess access) {
        if (slot != null && slot.hasItem() && ItemStack.isSameItemSameTags(slot.getItem(), originalStack)) {
            slot.set(containerItem);
        } else if (access != null) {
            access.set(containerItem);
        } else {
            boolean replaced = false;

            if (ItemStack.isSameItem(player.getMainHandItem(), originalStack)) {
                player.setItemInHand(InteractionHand.MAIN_HAND, containerItem);
                replaced = true;
            } else if (ItemStack.isSameItem(player.getOffhandItem(), originalStack)) {
                player.setItemInHand(InteractionHand.OFF_HAND, containerItem);
                replaced = true;
            }

            if (!replaced && !containerItem.isEmpty()) {
                if (!player.getInventory().add(containerItem)) {
                    player.drop(containerItem, false);
                }
            }
        }
    }

    default void drainVial(ItemStack stack, Player player, Level level) {
        if (level.isClientSide) return;

        FluidStack fluid = readFluid(stack);
        if (fluid.isEmpty()) return;

        spawnDrainingParticles(level, player, stack, 5);
        writeFluid(stack, FluidStack.EMPTY);
        playSound(level, player, SoundEvents.BOTTLE_EMPTY);
    }
    default void extractFromSelf(ItemStack stack, Level level, LivingEntity entityLiving) {
        if (!(entityLiving instanceof Player player)) return;

        RegistryAccess access = level.registryAccess();
        CombatContext ctx = getCombatContext(stack, access);

        if (ctx.fluid().isEmpty()) {
            SyringeExtractionType extractionType = getMatchingFluid(player, access);
            if (extractionType == null && !player.getType().is(BTags.BEntityTags.DOES_NOT_DROP_FLUID.tag)) {
                extractionType = getFallbackExtraction(access);
            }

            if (extractionType != null) {
                FluidStack fluidStack;
                if (extractionType.isPotionType()) {
                    fluidStack = SyringeFluidExtractionTypeManager.createPotionFluidStack(extractionType, getCapacity(stack));
                } else {
                    Fluid fluid = SyringeFluidExtractionTypeManager.getFluidFor(extractionType);
                    fluidStack = new FluidStack(fluid, getCapacity(stack));
                }

                writeFluid(stack, fluidStack);

                player.hurt(player.damageSources().generic(), 2.0F);
                playSound(level, player, SoundEvents.PLAYER_HURT);
                spawnBloodParticles(level, player, stack, 5);
            }
        }
    }
    default boolean extractFromTarget(ItemStack stack, LivingEntity target, Player player, RegistryAccess access) {
        if (!readFluid(stack).isEmpty()) return false;

        SyringeExtractionType extractionType = getMatchingFluid(target, access);
        if (extractionType == null && !target.getType().is(BTags.BEntityTags.DOES_NOT_DROP_FLUID.tag)) {
            extractionType = getFallbackExtraction(access);
        }

        if (extractionType != null) {
            FluidStack fluidStack;

            if (extractionType.isPotionType()) {
                fluidStack = SyringeFluidExtractionTypeManager.createPotionFluidStack(extractionType, getCapacity(stack));
            } else {
                Fluid fluid = SyringeFluidExtractionTypeManager.getFluidFor(extractionType);
                fluidStack = new FluidStack(fluid, getCapacity(stack));
            }

            writeFluid(stack, fluidStack);
            target.hurt(player.damageSources().playerAttack(player), 2.0F);
            spawnBloodParticles(player.level(), target, stack, 5);
            return true;
        }

        return false;
    }
    default void injectSelf(ItemStack stack, Level level, LivingEntity entityLiving, boolean isProjectile) {
        if (!(entityLiving instanceof Player player)) return;

        RegistryAccess access = level.registryAccess();
        CombatContext ctx = getCombatContext(stack, access);
        FluidStack fluid = ctx.fluid();

        if (fluid.getAmount() < ctx.useAmount()) return;
        if (!level.isClientSide) {
            spawnBloodParticles(player.level(), player, stack, 5);
            applySyringeEffects(player, ctx);

            fluid.shrink(ctx.useAmount());
            writeFluid(stack, fluid);
            player.hurt(player.damageSources().generic(), 2.0F);
            playSound(level, player, SoundEvents.PLAYER_ATTACK_CRIT);

            advancementLogic(player, player, ctx, isProjectile);
        }
    }
    default boolean injectIntoTarget(ItemStack stack, LivingEntity target, Player player, RegistryAccess access) {
        CombatContext ctx = getCombatContext(stack, access);
        if (ctx.fluid().getAmount() < ctx.useAmount()) {
            target.hurt(player.damageSources().playerAttack(player), 2.0F);
            return true;
        }
        if (!player.level().isClientSide) {
            spawnBloodParticles(player.level(), target, stack, 5);
            applySyringeEffects(target, ctx);
            ctx.fluid().shrink(ctx.useAmount());
            writeFluid(stack, ctx.fluid());

            Optional<Float> optDamage = ctx.type() != null ? ctx.type().damage() : Optional.empty();
            float damage = ctx.type() != null ? ctx.type().damage().orElse(2.0F) : 2.0F;
            target.hurt(player.damageSources().playerAttack(player), damage);

            boolean isDirect = !target.getUUID().equals(player.getUUID());

            advancementLogic(player, target, ctx, isDirect);
        }
        return true;
    }

    default void advancementLogic(Player source, LivingEntity target, CombatContext ctx, boolean isDirect) {
        if (!(source instanceof ServerPlayer player)) return;
        if (ctx == null || ctx.type() == null) return;

        boolean isSelf = source.getUUID().equals(target.getUUID());
        boolean isMilk = SyringeFluidTypeManager.isMilk(ctx.fluid().getFluid(), source.level().registryAccess());
        boolean hasBurning = ctx.type().hasBurning();
        boolean isHelpful = SyringeFluidTypeManager.getEffects(ctx.type(), ctx.fluid()).stream()
                .anyMatch(effect -> effect.getEffect().isBeneficial());
        boolean isHarmful = SyringeFluidTypeManager.getEffects(ctx.type(), ctx.fluid()).stream()
                .anyMatch(effect -> !effect.getEffect().isBeneficial());

        if (isSelf) {
            if (isMilk) BAdvancements.LACTOSE_TOLERANT.awardTo(player);
            if (isDirect) BAdvancements.SELF_INFLICTED_SCIENCE.awardTo(player);
        }
        else {
            if (isHelpful) BAdvancements.MEDIC.awardTo(player);
            else if (isHarmful) BAdvancements.MEDICAL_MALPRACTICE.awardTo(player);
            if (hasBurning) BAdvancements.FIRE_FIRE_FIRE.awardTo(player);
        }
    }

    default void applySyringeEffects(LivingEntity entity, CombatContext ctx) {
        SyringeFluidTypeManager.applyAllEffects(ctx.type(), ctx.fluid(), entity);
    }

    default void spawnBloodParticles(Level level, Entity entity, FluidStack fluid, RegistryAccess access, int count) {
        spawnColorParticles(level, entity, fluid, access, count, 0.5, 1.0, 0.5);
    }
    default void spawnBloodParticles(Level level, Entity entity, ItemStack stack, int count) {
        spawnColorParticles(level, entity, stack, count, 0.5, 1.0, 0.5);
    }
    default void spawnDrainingParticles(Level level, Entity entity, ItemStack stack, int count) {
        spawnColorParticles(level, entity, stack, count, 0.3, 0.2, 0.3);
    }
    default void spawnTrailParticles(Level level, Entity entity, ItemStack stack, int count) {
        if (!(level instanceof ServerLevel server)) return;
        Vector3f color = getParticleColor(stack, level.registryAccess());

        for (int i = 0; i < count; i++) {
            double x = entity.getX() + (level.random.nextDouble() - 0.5) * 0.2;
            double y = entity.getY() + level.random.nextDouble() * 0.6 + 0.5;
            double z = entity.getZ() + (level.random.nextDouble() - 0.5) * 0.2;
            server.sendParticles(new ColorableDripParticleData(color.x(), color.y(), color.z()), x, y, z, 5, 0, 0, 0, 0);
        }
    }
    default void spawnColorParticles(Level level, Entity entity, FluidStack fluid, RegistryAccess access, int count, double dxRange, double dyRange, double dzRange) {
        if (!(level instanceof ServerLevel server)) return;
        Vector3f color = getParticleColor(fluid, access);

        for (int i = 0; i < count; i++) {
            double dx = (level.random.nextDouble() - 0.5) * dxRange;
            double dy = level.random.nextDouble() * dyRange;
            double dz = (level.random.nextDouble() - 0.5) * dzRange;
            server.sendParticles(new DustParticleOptions(color, 1.0F),
                    entity.getX() + dx, entity.getY() + dy, entity.getZ() + dz,
                    1, 0, 0, 0, 0);
        }
    }
    default void spawnColorParticles(Level level, Entity entity, ItemStack stack, int count, double dxRange, double dyRange, double dzRange) {
        if (!(level instanceof ServerLevel server)) return;
        Vector3f color = getParticleColor(stack, level.registryAccess());

        for (int i = 0; i < count; i++) {
            double dx = (level.random.nextDouble() - 0.5) * dxRange;
            double dy = level.random.nextDouble() * dyRange;
            double dz = (level.random.nextDouble() - 0.5) * dzRange;
            server.sendParticles(new DustParticleOptions(color, 1.0F), entity.getX() + dx, entity.getY() + dy, entity.getZ() + dz, 1, 0, 0, 0, 0);
        }
    }

    default Vector3f getParticleColor(FluidStack fluidStack, RegistryAccess access) {
        SyringeFluidType type = SyringeFluidTypeManager.fromFluid(fluidStack, access);
        int color = SyringeFluidTypeManager.getColor(type, fluidStack);
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        return new Vector3f(r, g, b);
    }
    default Vector3f getParticleColor(ItemStack stack, RegistryAccess access) {
        FluidStack fluidStack = readFluid(stack);
        SyringeFluidType type = SyringeFluidTypeManager.fromFluid(fluidStack, access);
        int color = SyringeFluidTypeManager.getColor(type, fluidStack);
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        return new Vector3f(r, g, b);
    }

    default void playSound(Level level, Entity entity, SoundEvent sound) {
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}