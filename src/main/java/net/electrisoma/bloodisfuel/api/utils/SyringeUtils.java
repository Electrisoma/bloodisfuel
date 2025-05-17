package net.electrisoma.bloodisfuel.api.utils;

import net.electrisoma.bloodisfuel.api.data.*;
import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidTypeManager;
import net.electrisoma.bloodisfuel.registry.BAdvancements;
import net.electrisoma.bloodisfuel.infrastructure.data.entries.BSyringeFluidTypes;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.effect.MobEffectInstance;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import org.joml.Vector3f;

import java.util.UUID;
import java.util.Optional;


@SuppressWarnings({"OptionalGetWithoutIsPresent", "DataFlowIssue", "RedundantSuppression", "unused"})
public interface SyringeUtils extends FluidUtils, CombatContextUtils, TooltipUtils {

    default SyringeFluidType getMatchingFluid(LivingEntity target, RegistryAccess access) {
        return SyringeFluidTypeManager.getAll(access).stream()
                .filter(type -> ForgeRegistries.ENTITY_TYPES.getHolder(target.getType())
                        .map(holder -> type.mobs().map(set -> set.contains(holder)).orElse(false))
                        .orElse(false))
                .findFirst().orElse(null);
    }
    default SyringeFluidType getFallback(RegistryAccess access) {
        return access.registryOrThrow(BRegistries.SYRINGE_BLADE_FLUIDS)
                .getOptional(BSyringeFluidTypes.FALLBACK)
                .orElse(null);
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
            SyringeFluidType selfType = getMatchingFluid(player, access);
            if (selfType == null) selfType = getFallback(access);
            if (selfType != null) {
                FluidStack fluid = new FluidStack(SyringeFluidTypeManager.getFluidFor(selfType), getCapacity(stack));
                writeFluid(stack, fluid);
                player.hurt(player.damageSources().generic(), 2.0F);
                playSound(level, player, SoundEvents.PLAYER_HURT);
                spawnBloodParticles(level, player, stack, 5);
            }
        }
    }
    default void injectSelf(ItemStack stack, Level level, LivingEntity entityLiving) {
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

            advancementLogic(player, player, ctx);
        }
    }
    default boolean extractFromTarget(ItemStack stack, LivingEntity target, Player player, RegistryAccess access) {
        if (readFluid(stack).isEmpty()) {
            SyringeFluidType matchedType = getMatchingFluid(target, access);
            if (matchedType == null) matchedType = getFallback(access);
            if (matchedType != null) {
                FluidStack fluid = new FluidStack(SyringeFluidTypeManager.getFluidFor(matchedType), getCapacity(stack));
                writeFluid(stack, fluid);
                target.hurt(player.damageSources().playerAttack(player), 2.0F);
                spawnBloodParticles(player.level(), target, stack, 5);
                return true;
            }
        }
        return false;
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

            advancementLogic(player, target, ctx);
        }
        return true;
    }

    default void advancementLogic(Player source, LivingEntity target, CombatContext ctx) {
        if (!(source instanceof ServerPlayer player)) return;
        if (ctx == null || ctx.type() == null) return;

        boolean isSelf = source == target;
        boolean isMilk = SyringeFluidTypeManager.isMilk(ctx.fluid().getFluid(), source.level().registryAccess());
        boolean hasBurning = ctx.type().hasBurning();
        boolean isHelpful = SyringeFluidTypeManager.getEffects(ctx.type(), ctx.fluid()).stream()
                .anyMatch(effect -> effect.getEffect().isBeneficial());
        boolean isHarmful = SyringeFluidTypeManager.getEffects(ctx.type(), ctx.fluid()).stream()
                .anyMatch(effect -> !effect.getEffect().isBeneficial());

        if (isSelf) {
            if (isMilk) BAdvancements.LACTOSE_TOLERANT.awardTo(player);
            BAdvancements.SELF_INFLICTED_SCIENCE.awardTo(player);
        }
        else {
            if (isHelpful) BAdvancements.MEDIC.awardTo(player);
            else if (isHarmful) BAdvancements.MEDICAL_MALPRACTICE.awardTo(player);
            if (hasBurning) BAdvancements.FIRE_FIRE_FIRE.awardTo(player);
        }
    }

    default void applySyringeEffects(LivingEntity entity, CombatContext ctx) {
        if (ctx.fluid().isEmpty()) return;

        SyringeFluidTypeManager.getEffects(ctx.type(), ctx.fluid())
                .forEach(effect -> entity.addEffect(new MobEffectInstance(effect)));

        if (ctx.type().hasBurning()) applyBurningEffect(entity, ctx.type().burning().get());
        if (ctx.type().hasDrowning()) applyDrowningEffect(entity, ctx.type().drowning().get());
        if (ctx.type().hasFreezing()) applyFreezingEffect(entity, ctx.type().freezing().get());
        if (ctx.type().hasFood()) applyFoodEffect(entity, ctx.type());
        if (ctx.type().hasExtinguishing()) applyExtinguishingEffect(entity, ctx.type());

        if (SyringeFluidTypeManager.isMilk(ctx.fluid().getFluid(), entity.level().registryAccess())) {
            entity.removeAllEffects();
        }
    }
    default void applyBurningEffect(LivingEntity entity, BurningData data) {
        if (entity == null || entity.level().isClientSide || data == null) return;
        entity.setSecondsOnFire(data.durationSeconds());
        if (data.damagePerSecond() > 0)
            entity.hurt(entity.damageSources().onFire(), data.damagePerSecond());
        playSound(entity.level(), entity, SoundEvents.FIRE_AMBIENT);
    }
    default void applyExtinguishingEffect(LivingEntity entity, SyringeFluidType type) {
        if (entity.level().isClientSide || type == null || !entity.isOnFire()) return;
        SyringeFluidTypeManager.applyExtinguishing(type, entity);
        playSound(entity.level(), entity, SoundEvents.FIRE_EXTINGUISH);
    }
    default void applyFoodEffect(LivingEntity entity, SyringeFluidType type) {
        if (!(entity instanceof Player player) || type.food().isEmpty() || !player.getFoodData().needsFood()) return;
        FoodProperties food = type.food().get();
        player.getFoodData().eat(food.getNutrition(), food.getSaturationModifier());
        playSound(player.level(), player, SoundEvents.GENERIC_EAT);
    }
    default void applyDrowningEffect(LivingEntity entity, DrowningData data) {
        if (entity.level().isClientSide || data == null) return;
        int durationTicks = data.durationSeconds() != null ? data.durationSeconds() * 20 : 100;
        float damage = data.damagePerSecond();

        entity.hurt(entity.damageSources().drown(), damage);

        if (entity instanceof Player player && entity.level() instanceof ServerLevel serverLevel) {
            MinecraftServer server = serverLevel.getServer();
            server.execute(() -> handleDrowning(player, damage, durationTicks));
        }

        playSound(entity.level(), entity, SoundEvents.DROWNED_HURT);
    }
    default void applyFreezingEffect(LivingEntity entity, FreezingData data) {
        if (entity.level().isClientSide || data == null) return;

        int durationTicks = data.durationSeconds() != null ? data.durationSeconds() * 20 : 100;
        float damage = Optional.ofNullable(data.damagePerSecond()).orElse(1.0f);
        float slow = Optional.ofNullable(data.slowAmount()).orElse(0.5f);

        entity.setTicksFrozen(entity.getTicksRequiredToFreeze() + durationTicks);
        entity.hurt(entity.damageSources().freeze(), damage);

        playSound(entity.level(), entity, SoundEvents.PLAYER_HURT_FREEZE);

        if (entity instanceof Player player && entity.level() instanceof ServerLevel serverLevel) {
            MinecraftServer server = serverLevel.getServer();
            server.execute(() -> handleFreezing(player, slow, durationTicks));
        }
    }

    private void handleDrowning(Player player, float damage, int ticks) {
        final int[] ticksLeft = {ticks};
        Runnable task = new Runnable() {
            @Override
            public void run() {
                if (ticksLeft[0]-- > 0) {
                    player.setAirSupply(Math.max(0, player.getAirSupply() - 1));
                    player.hurt(player.damageSources().drown(), damage);
                    player.level().getServer().execute(this);
                } else {
                    player.setAirSupply(0);
                }
            }
        };
        player.level().getServer().execute(task);
    }
    private void handleFreezing(Player player, float slowAmount, int durationTicks) {
        UUID slowId = UUID.nameUUIDFromBytes("bloodisfuel:freezing_slow".getBytes());
        AttributeInstance attr = player.getAttribute(Attributes.MOVEMENT_SPEED);

        if (attr != null && !attr.hasModifier(new AttributeModifier(slowId, "Freezing Slowness", -slowAmount, AttributeModifier.Operation.MULTIPLY_TOTAL))) {
            attr.addTransientModifier(new AttributeModifier(slowId, "Freezing Slowness", -slowAmount, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }

        Runnable task = new Runnable() {
            int remaining = durationTicks;

            @Override
            public void run() {
                if (--remaining <= 0) {
                    if (attr != null) attr.removeModifier(slowId);
                } else {
                    player.level().getServer().tell(new TickTask(1, this));
                }
            }
        };

        player.level().getServer().tell(new TickTask(1, task));
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