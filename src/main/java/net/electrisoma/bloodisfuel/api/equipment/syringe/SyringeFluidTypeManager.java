package net.electrisoma.bloodisfuel.api.equipment.syringe;

import net.electrisoma.bloodisfuel.api.data.*;
import net.electrisoma.bloodisfuel.registry.BFluids;
import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.foundation.data.entries.BSyringeFluidTypes;

import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;


/**
 * Expected datapack format:
 * {
 *   "fluids": "FLUIDS",
 *@  "color": COLOR,
 *@  "mobs": ["MOBS"],
 *@  "damage": DAMAGE,
 *@  "attack_speed": ATTACK SPEED,
 *@  "food": {
 *      "nutrition": NUTRITION,
 *      "saturation": SATURATION
 *   },
 *@  "on_entity_hit": {
 *      "amplifier": AMPLIFIER,
 *      "duration": DURATION,
 *      "effect": "EFFECT"
 *   },
 *@  "burning": {
 *      "duration_seconds": SECONDS,
 *      "damage_per_second": DAMAGE
 *   },
 *@  "extinguishing": {
 *      "duration_seconds": SECONDS,
 *      "heal_per_second": HEALTH
 *   }
 * }
 * Optional fields will be marked with a @
 */

// L warning lol -----------------V laugh at this fool
@SuppressWarnings({"unused", "RedundantSuppression"})
public class SyringeFluidTypeManager {
    public static final SyringeFluidType EMPTY = new SyringeFluidType.Builder()
            .color(0xFFFFFF)
            .build();

    /**
     * Gets the SyringeFluidType for a given FluidStack.
     */
    @SuppressWarnings("deprecation")
    public static SyringeFluidType fromFluid(FluidStack stack, RegistryAccess access) {
        if (stack.isEmpty()) return EMPTY;

        Fluid fluid = stack.getFluid();
        ResourceLocation fluidKey = ForgeRegistries.FLUIDS.getKey(fluid);
        if (fluidKey == null) return EMPTY;

        Registry<SyringeFluidType> registry = access.registryOrThrow(BRegistries.SYRINGE_FLUIDS);

        if (isPotion(fluid, access) && stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains("Potion")) {
                ResourceLocation potionId = ResourceLocation.tryParse(tag.getString("Potion"));
                if (potionId != null) {
                    for (SyringeFluidType type : registry) {
                        if (type.potion().isPresent() &&
                                Objects.equals(ForgeRegistries.POTIONS.getKey(type.potion().get()), potionId)) {
                            return type;
                        }
                    }
                }
            }
        }

        for (SyringeFluidType type : registry) {
            boolean match = type.fluids().stream()
                    .anyMatch(holderSet -> holderSet.stream()
                            .anyMatch(holder -> {
                                ResourceLocation key = ForgeRegistries.FLUIDS.getKey(holder.value());
                                return key != null && key.equals(fluidKey);
                            }));
            if (match) return type;
        }

        if (registry.containsKey(BSyringeFluidTypes.POTION)) return registry.get(BSyringeFluidTypes.POTION);
        if (registry.containsKey(BSyringeFluidTypes.FALLBACK)) return registry.get(BSyringeFluidTypes.FALLBACK);

        return EMPTY;
    }

    /**
     * Gets the display color of the fluid, including potion.
     */
    public static int getColor(SyringeFluidType type, FluidStack stack) {
        if (type.isPotionType() && stack.hasTag()) {
            return PotionUtils.getColor(PotionUtils.getAllEffects(stack.getTag())) | 0xFF000000;
        }
        return type.color();
    }
    public static boolean isGlowing(SyringeFluidType type) {
        return type.glowing().orElse(false);
    }
    public static boolean isOpaque(SyringeFluidType type) {
        return type.opaque().orElse(true);
    }

    /**
     * Returns the list of effects this fluid should apply on hit.
     */
    public static List<MobEffectInstance> getEffects(SyringeFluidType type, FluidStack stack) {
        if (isMilk(stack.getFluid(), null)) {
            return type.statusEffects()
                    .flatMap(OnHitEffects::effects)
                    .map(list -> list.stream().map(EffectsData::effect).toList())
                    .orElse(List.of());
        }

        if (type.isPotionType() && stack.hasTag()) {
            return PotionUtils.getAllEffects(stack.getTag());
        }

        return type.statusEffects()
                .flatMap(OnHitEffects::effects)
                .map(list -> list.stream().map(EffectsData::effect).toList())
                .orElse(List.of());
    }
    /**
     * Returns all dynamically registered fluid types.
     */
    public static List<SyringeFluidType> getAll(RegistryAccess access) {
        Registry<SyringeFluidType> registry = access.registryOrThrow(BRegistries.SYRINGE_FLUIDS);
        return registry.stream().toList();
    }
    /**
     * Gets the default fluid associated with a type.
     */
    public static Fluid getFluidFor(SyringeFluidType type) {
        return type.fluids().stream()
                .flatMap(HolderSet::stream)
                .findFirst()
                .map(Holder::value)
                .orElse(BFluids.BLOOD.get());
    }

    /**
     * Check if the fluid is vanilla milk.
     */
    public static boolean isMilk(Fluid fluid, RegistryAccess access) {
        ResourceLocation fluidKey = ForgeRegistries.FLUIDS.getKey(fluid);
        return fluidKey != null && fluidKey.equals(new ResourceLocation("minecraft", "milk"));
    }

    /**
     * Check if the fluid is a potion.
     */
    public static boolean isPotion(Fluid fluid, RegistryAccess access) {
        ResourceLocation key = ForgeRegistries.FLUIDS.getKey(fluid);
        return key != null && Set.of(
                new ResourceLocation("forge", "potion"),
                new ResourceLocation("create", "potion")
        ).contains(key);
    }
    public static FluidStack createPotionFluidStack(SyringeFluidType type, int amount) {
        if (!type.isPotionType()) return FluidStack.EMPTY;

        Fluid potionFluid = getFluidFor(type);
        FluidStack stack = new FluidStack(potionFluid, amount);

        type.potion().ifPresent(potion -> {
            ItemStack dummy = new ItemStack(Items.POTION);
            PotionUtils.setPotion(dummy, potion);

            if (dummy.hasTag()) {
                assert dummy.getTag() != null;
                stack.setTag(dummy.getTag().copy());
            }
        });

        return stack;
    }

    /**
     * Applies all status effects.
     */
    public static void applyAllEffects(SyringeFluidType type, FluidStack fluid, LivingEntity target) {
        if (type == null || fluid.isEmpty() || target == null) return;

        if (isMilk(fluid.getFluid(), target.level().registryAccess())) {
            target.removeAllEffects();
            return;
        }

        getEffects(type, fluid).forEach(effect -> target.addEffect(new MobEffectInstance(effect)));

        applyFood(type, target);
        applyBurning(type, target);
        applyExtinguishing(type, target);
        type.statusEffects().flatMap(OnHitEffects::drowning)
                .ifPresent(drowning -> applyDrowningEffect(target, drowning));
        type.statusEffects().flatMap(OnHitEffects::freezing)
                .ifPresent(freezing -> applyFreezingEffect(target, freezing));
        applyTeleporting(type, target);
    }

    /**
     * Applies the burning effects.
     */
    @SuppressWarnings("DataFlowIssue")
    public static void applyBurning(SyringeFluidType type, LivingEntity target) {
        type.statusEffects()
                .flatMap(OnHitEffects::burning)
                .ifPresent(burning -> {
            target.setSecondsOnFire(burning.durationSeconds());
            if (burning.damagePerSecond() > 0)
                target.hurt(target.damageSources().onFire(), burning.damagePerSecond() * burning.durationSeconds());
        });
    }

    /**
     * Applies the extinguishing effects.
     */
    @SuppressWarnings("DataFlowIssue")
    public static void applyExtinguishing(SyringeFluidType type, LivingEntity target) {
        type.statusEffects()
                .flatMap(OnHitEffects::extinguishing)
                .ifPresent(extinguishing -> {
            target.clearFire();
            if (extinguishing.healPerSecond() > 0 && extinguishing.durationSeconds() > 0) {
                float totalHeal = extinguishing.healPerSecond() * extinguishing.durationSeconds();
                target.heal(totalHeal);
            }
        });
    }

    /**
     * Applies the food effects.
     */
    public static void applyFood(SyringeFluidType type, LivingEntity entity) {
        if (!(entity instanceof Player player)) return;
        type.statusEffects()
                .flatMap(OnHitEffects::food)
                .ifPresent(food -> {
            if (player.getFoodData().needsFood()) {
                player.getFoodData().eat(food.getNutrition(), food.getSaturationModifier());
            }
        });
    }

    /**
     * Applies drowning effects.
     */
    public static void applyDrowningEffect(LivingEntity entity, DrowningData data) {
        if (data == null || entity.level().isClientSide || !(entity.level() instanceof ServerLevel serverLevel)) return;

        int durationTicks = Optional.ofNullable(data.durationSeconds()).orElse(5) * 20;
        float damage = Optional.ofNullable(data.damagePerSecond()).orElse(1.0f);

        entity.hurt(entity.damageSources().drown(), damage);
        serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.DROWNED_HURT, SoundSource.PLAYERS, 1.0F, 1.0F);

        UUID drowningId = UUID.nameUUIDFromBytes(("bloodisfuel:drowning:" + entity.getUUID()).getBytes());

        Runnable task = new Runnable() {
            int remaining = durationTicks;

            @Override
            public void run() {
                if (!entity.isAlive()) return;

                if (--remaining <= 0) {
                    entity.setAirSupply(0);
                } else {
                    entity.setAirSupply(Math.max(0, entity.getAirSupply() - 1));
                    entity.hurt(entity.damageSources().drown(), damage);
                    serverLevel.getServer().execute(this);
                }
            }
        };

        serverLevel.getServer().execute(task);
    }

    /**
     * Applies freezing effects.
     */
    public static void applyFreezingEffect(LivingEntity entity, FreezingData data) {
        if (data == null || entity.level().isClientSide || !(entity.level() instanceof ServerLevel serverLevel))
            return;
        if (entity.getType().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES))
            return;

        int durationTicks = Optional.ofNullable(data.durationSeconds()).orElse(5) * 20;
        float damage = Optional.ofNullable(data.damagePerSecond()).orElse(1.0f);
        float slowAmount = Optional.ofNullable(data.slowAmount()).orElse(0.5f);
        UUID slowId = UUID.nameUUIDFromBytes("bloodisfuel:freezing_slow".getBytes());

        if (entity.isFreezing()) {
            entity.setTicksFrozen(entity.getTicksFrozen() + durationTicks);
        } else {
            entity.setTicksFrozen(durationTicks);
        }

        entity.hurt(entity.damageSources().freeze(), damage);
        serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_HURT_FREEZE, SoundSource.PLAYERS, 1.0F, 1.0F);

        AttributeInstance attr = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr != null && !attr.hasModifier(new AttributeModifier(slowId, "Freezing Slowness", -slowAmount, AttributeModifier.Operation.MULTIPLY_TOTAL))) {
            attr.addTransientModifier(new AttributeModifier(slowId, "Freezing Slowness", -slowAmount, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }

        Runnable task = new Runnable() {
            int remaining = durationTicks;

            @Override
            public void run() {
                if (!entity.isAlive()) return;

                if (--remaining <= 0) {
                    if (attr != null) attr.removeModifier(slowId);
                } else {
                    serverLevel.getServer().execute(this);
                }
            }
        };

        serverLevel.getServer().execute(task);
    }

    /**
     * Applies teleportation effects.
     */
    public static void applyTeleporting(SyringeFluidType type, LivingEntity entity) {
        Optional<TeleportationData> teleportData = type.statusEffects()
                .flatMap(OnHitEffects::teleportation);

        if (teleportData.isEmpty()) return;
        if (!(entity.level() instanceof ServerLevel serverLevel)) return;

        int diameter = teleportData.map(TeleportationData::diameter).orElse(20);
        final int MAX_ATTEMPTS = 16;

        double entityX = entity.getX();
        double entityY = entity.getY();
        double entityZ = entity.getZ();

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            double offsetX = (entity.getRandom().nextDouble() - 0.5D) * diameter;
            double offsetY = entity.getRandom().nextInt((int) diameter) - (diameter / 2.0);
            double offsetZ = (entity.getRandom().nextDouble() - 0.5D) * diameter;

            double targetX = entityX + offsetX;
            double targetY = Mth.clamp(entityY + offsetY, serverLevel.getMinBuildHeight(), serverLevel.getMaxBuildHeight() - 1);
            double targetZ = entityZ + offsetZ;

            EntityTeleportEvent.ChorusFruit event = ForgeEventFactory.onChorusFruitTeleport(entity, targetX, targetY, targetZ);
            if (event.isCanceled()) return;

            if (entity.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true)) {
                if (entity.isPassenger()) entity.stopRiding();

                SoundEvent sound = (entity instanceof Fox)
                        ? SoundEvents.FOX_TELEPORT
                        : SoundEvents.CHORUS_FRUIT_TELEPORT;

                serverLevel.playSound(null, entityX, entityY, entityZ, sound, SoundSource.PLAYERS, 1.0F, 1.0F);
                entity.playSound(sound, 1.0F, 1.0F);
                entity.setDeltaMovement(Vec3.ZERO);
                return;
            }
        }
    }
}