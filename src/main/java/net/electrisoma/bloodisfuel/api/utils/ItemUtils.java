package net.electrisoma.bloodisfuel.api.utils;

import net.electrisoma.bloodisfuel.api.data.DrowningData;
import net.electrisoma.bloodisfuel.api.data.FreezingData;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidTypeManager;
import net.electrisoma.bloodisfuel.registry.BEnchantments;
import net.electrisoma.bloodisfuel.api.data.BurningData;
import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.foundation.data.entries.BSyringeFluidTypes;

import com.simibubi.create.AllEnchantments;
import com.simibubi.create.foundation.utility.CreateLang;

import net.electrisoma.bloodisfuel.api.data.ColorableDripParticleData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import org.joml.Vector3f;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;


@Deprecated
@SuppressWarnings("all")
public interface ItemUtils {

    /**
     * Item capacity utilities.
     */
    default int getBaseCapacity(ItemStack stack) {
        return 1000;
    }
    default int getCapacityEnchantmentAddition(ItemStack stack) {
        return 1000;
    }
    default int getCapacity(ItemStack stack) {
        int enchantLevel = stack.getEnchantmentLevel(AllEnchantments.CAPACITY.get());
        return getBaseCapacity(stack) + getCapacityEnchantmentAddition(stack) * enchantLevel;
    }

    /**
     * Item usage utilities.
     */
    default int getChargeCount(ItemStack stack) {
        int baseCharges = 4;
        int extraLevel = stack.getEnchantmentLevel(BEnchantments.EXTRA_VIALS.get());
        return baseCharges + (extraLevel * 2);
    }
    default int getUseAmount(int capacity, int charges) {
        return (int) Math.ceil((double) capacity / charges);
    }
    default int getUseAmount(ItemStack stack) {
        return getUseAmount(getCapacity(stack), getChargeCount(stack));
    }
    default int getCurrentFillLevel(ItemStack stack) {
        return readFluid(stack).getAmount();
    }

    /**
     * Text utilities.
     */
    default String formatDuration(int ticks) {
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format("%d:%02d", minutes, seconds);
    }
    static String toRoman(int number) {
        if (number < 1 || number > 10) return String.valueOf(number);
        return new String[]{"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"}[number - 1];
    }
    default void tooltipMaker(List<Component> tooltip, ItemStack stack, @Nullable RegistryAccess registryAccess) {
        FluidStack fluid = readFluid(stack);
        SyringeFluidType type = SyringeFluidTypeManager.fromFluid(fluid, registryAccess);
        List<MobEffectInstance> effects = SyringeFluidTypeManager.getEffects(type, fluid);

        /**
         * Empty tooltip.
         */
        if (stack.getTag() == null || fluid.isEmpty()) {
            tooltip.add(Component.translatable("bloodisfuel.tooltip.empty").withStyle(ChatFormatting.GRAY));
            return;
        }

        /**
         * Fluid tooltip.
         */
        tooltip.add(CreateLang.fluidName(fluid).component()
                .withStyle(ChatFormatting.GRAY)
                .append(" ")
                .append(CreateLang.number(fluid.getAmount()).style(ChatFormatting.GOLD).component())
                .append(Component.translatable("create.generic.unit.millibuckets").withStyle(ChatFormatting.GOLD))
                .append(" / ")
                .append(CreateLang.number(getCapacity(stack)).style(ChatFormatting.GRAY).component())
                .append(Component.translatable("create.generic.unit.millibuckets").withStyle(ChatFormatting.GRAY)));

        /**
         * Effects tooltip.
         */
        for (MobEffectInstance effect : effects) {
            Component effectName = Component.translatable(effect.getDescriptionId())
                    .withStyle(effect.getEffect().isBeneficial() ? ChatFormatting.GREEN : ChatFormatting.RED);
            Component level = Component.literal(" " + toRoman(effect.getAmplifier() + 1))
                    .withStyle(ChatFormatting.GOLD);
            Component duration = Component.empty();
            if (effect.getDuration() > 1) {
                duration = Component.literal(" (" + formatDuration(effect.getDuration()) + ")")
                        .withStyle(ChatFormatting.GRAY);
            }

            tooltip.add(Component.literal("• ").withStyle(ChatFormatting.GRAY)
                    .append(Component.translatable("bloodisfuel.tooltip.effect").withStyle(ChatFormatting.GRAY))
                    .append(": ")
                    .append(effectName)
                    .append(level)
                    .append(duration));
        }

        /**
         * Burning tooltip.
         */
        if (type != null && type.hasBurning()) {
            type.burning().ifPresent(burning -> {
                int duration = burning.durationSeconds();
                float damage = burning.damagePerSecond();

                MutableComponent line = Component.literal("• ").withStyle(ChatFormatting.GRAY)
                        .append(Component.translatable("bloodisfuel.tooltip.burning").withStyle(ChatFormatting.RED))
                        .append(": ");

                if (duration > 0) {
                    line.append(Component.literal(String.valueOf(duration)).withStyle(ChatFormatting.GOLD))
                            .append(Component.translatable("bloodisfuel.tooltip.seconds").withStyle(ChatFormatting.GOLD));
                }

                if (damage > 0) {
                    if (duration > 0) line.append(" ");
                    line.append(Component.literal("(" + damage + " ").withStyle(ChatFormatting.RED))
                            .append(Component.translatable("bloodisfuel.tooltip.damage"))
                            .append(Component.literal("/"))
                            .append(Component.translatable("bloodisfuel.tooltip.seconds"))
                            .append(Component.literal(")"));
                }

                tooltip.add(line);
            });
        }

        /**
         * Extinguishing tooltip.
         */
        if (type != null && type.hasExtinguishing()) {
            type.extinguishing().ifPresent(extinguishing -> {
                int duration = extinguishing.durationSeconds();
                float heal = extinguishing.healPerSecond();

                MutableComponent line = Component.literal("• ").withStyle(ChatFormatting.GRAY)
                        .append(Component.translatable("bloodisfuel.tooltip.extinguishing").withStyle(ChatFormatting.AQUA))
                        .append(": ");

                if (duration > 0) {
                    line.append(Component.literal(String.valueOf(duration)).withStyle(ChatFormatting.GOLD))
                            .append(Component.translatable("bloodisfuel.tooltip.seconds").withStyle(ChatFormatting.GOLD));
                }

                if (heal > 0) {
                    if (duration > 0) line.append(" ");
                    line.append(Component.literal("(" + heal + " "))
                            .append(Component.translatable("bloodisfuel.tooltip.heal"))
                            .append(Component.literal("/"))
                            .append(Component.translatable("bloodisfuel.tooltip.seconds"))
                            .append(Component.literal(")"));
                }

                tooltip.add(line);
            });
        }
    }
    default void projectileTooltipMaker(List<Component> tooltip, ItemStack stack, @Nullable RegistryAccess registryAccess) {
        CombatContext ctx = getCombatContext(stack, registryAccess);
        if (ctx.canAttack()) {
            Optional<Float> optDamage = ctx.type() != null ? ctx.type().damage() : Optional.empty();
            if (optDamage.isPresent()) {
                float damage = optDamage.get();
                if (Minecraft.getInstance().player != null) {
                    float playerAttackDamage = (float) Minecraft.getInstance().player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    damage += playerAttackDamage;
                }
                tooltip.add(Component.empty());
                tooltip.add(Component.translatable("item.modifiers.mainhand").withStyle(ChatFormatting.GRAY));
                String damageText = (damage % 1.0f == 0.0f)
                        ? String.valueOf((int) damage)
                        : String.format("%.2f", damage);
                tooltip.add(Component.literal(" ")
                        .append(Component.literal(damageText))
                        .append(" ")
                        .append(Component.translatable("bloodisfuel.tooltip.syringe_gun.damage"))
                        .withStyle(ChatFormatting.DARK_GREEN));
            }
        }
    }

    /**
     * Matching fluid utilities.
     */
    default SyringeFluidType getMatchingFluid(LivingEntity target, RegistryAccess access) {
        return SyringeFluidTypeManager.getAll(access).stream()
                .filter(type -> ForgeRegistries.ENTITY_TYPES.getHolder(target.getType())
                        .map(holder -> type.mobs().map(set -> set.contains(holder)).orElse(false))
                        .orElse(false))
                .findFirst().orElse(null);
    }
    default SyringeFluidType getFallback(RegistryAccess access) {
        return access.registryOrThrow(BRegistries.SYRINGE_FLUIDS)
                .getOptional(BSyringeFluidTypes.FALLBACK)
                .orElse(null);
    }

    /**
     * Fluid handler utiltiies.
     */
    default FluidHandlerItemStack getFluidHandler(ItemStack stack) {
        return new ToolItemFluidHandler(stack, getCapacity(stack), this::readFluid, this::writeFluid);
    }
    class ToolItemFluidHandler extends FluidHandlerItemStack {
        private final BiConsumer<ItemStack, FluidStack> write;
        private final Function<ItemStack, FluidStack> read;

        public ToolItemFluidHandler(ItemStack container, int capacity,
                                    Function<ItemStack, FluidStack> read,
                                    BiConsumer<ItemStack, FluidStack> write) {
            super(container, capacity);
            this.read = read;
            this.write = write;
        }

        @Override
        public FluidStack getFluid() {
            return read.apply(container);
        }

        @Override
        protected void setFluid(FluidStack fluid) {
            write.accept(container, fluid);
        }
    }

    /**
     * Fluid read/right utilities.
     */
    default FluidStack readFluid(ItemStack stack) {
        return FluidStack.loadFluidStackFromNBT(stack.getOrCreateTag().getCompound("Fluid"));
    }
    default void writeFluid(ItemStack stack, FluidStack fluid) {
        stack.getOrCreateTag().put("Fluid", fluid.writeToNBT(new CompoundTag()));
    }

    /**
     * Context for various syringe combat.
     */
    record CombatContext(FluidStack fluid, SyringeFluidType type, int useAmount, boolean canAttack, boolean onlyBeneficial) {}
    default CombatContext getCombatContext(ItemStack stack, @Nullable RegistryAccess access) {
        FluidStack fluidStack = readFluid(stack);
        SyringeFluidType type = SyringeFluidTypeManager.fromFluid(fluidStack, access);
        int capacity = getCapacity(stack);
        int charges = getChargeCount(stack);
        int useAmount = getUseAmount(capacity, charges);
        int currentFill = fluidStack.getAmount();
        List<MobEffectInstance> effects = SyringeFluidTypeManager.getEffects(type, fluidStack);
        boolean onlyBeneficial = effects.stream().allMatch(e -> e.getEffect().isBeneficial());
        boolean canAttack = currentFill >= useAmount && !onlyBeneficial;

        return new CombatContext(fluidStack, type, useAmount, canAttack, onlyBeneficial);
    }

    /**
     * Syringe utiltiies.
     */
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
            return;
        }
    }
    default void injectSelf(ItemStack stack, Level level, LivingEntity entityLiving) {
        if (!(entityLiving instanceof Player player)) return;

        RegistryAccess access = level.registryAccess();
        CombatContext ctx = getCombatContext(stack, access);
        FluidStack fluid = ctx.fluid();

        if (fluid.getAmount() < ctx.useAmount()) return;
        if (!level.isClientSide) {
            applySyringeEffects(player, ctx);

            fluid.shrink(ctx.useAmount());
            writeFluid(stack, fluid);
            playSound(level, player, SoundEvents.PLAYER_ATTACK_CRIT);
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
            applySyringeEffects(target, ctx);

            ctx.fluid().shrink(ctx.useAmount());
            writeFluid(stack, ctx.fluid());

            Optional<Float> optDamage = ctx.type() != null ? ctx.type().damage() : Optional.empty();
            optDamage.ifPresent(damage -> target.hurt(player.damageSources().playerAttack(player), damage));

            spawnBloodParticles(player.level(), target, stack, 5);
        }
        return true;
    }

    /**
     * Syringe effects utilities.
     */
    default void applySyringeEffects(LivingEntity entity, CombatContext ctx) {
        if (ctx.fluid().isEmpty()) return;

        if (SyringeFluidTypeManager.isMilk(ctx.fluid().getFluid(), entity.level().registryAccess())) {
            entity.removeAllEffects();
        }

        SyringeFluidTypeManager.getEffects(ctx.type(), ctx.fluid())
                .forEach(effect -> entity.addEffect(new MobEffectInstance(effect)));

        if (ctx.type().hasBurning()) {
            BurningData burningData = ctx.type().burning().get();
            applyBurningEffect(entity, burningData);
        }
        if (ctx.type().hasExtinguishing()) {
            applyExtinguishingEffect(entity, ctx.type());
        }
        if (ctx.type().hasFood()) {
            applyFoodEffect(entity, ctx.type());
        }
        if (ctx.type().hasDrowning()) {
            DrowningData drowningData = ctx.type().drowning().get();
            applyDrowningEffect(entity, drowningData);
        }
        if (ctx.type().hasFreezing()) {
            FreezingData freezingData = ctx.type().freezing().get();
            applyFreezingEffect(entity, freezingData);
        }
    }
    default void applyBurningEffect(LivingEntity entity, BurningData burningData) {
        if (entity == null || entity.level().isClientSide || burningData == null) return;
        entity.setSecondsOnFire(burningData.durationSeconds());
        if (burningData.damagePerSecond() > 0) {
            entity.hurt(entity.damageSources().onFire(), burningData.damagePerSecond());
        }

        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.FIRE_AMBIENT, entity.getSoundSource(), 1.0F, 1.0F);
    }
    default void applyExtinguishingEffect(LivingEntity entity, SyringeFluidType type) {
        if (entity == null || entity.level().isClientSide) return;
        if (type != null && type.hasExtinguishing() && entity.isOnFire()) {
            SyringeFluidTypeManager.applyExtinguishing(type, entity);
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.FIRE_EXTINGUISH, entity.getSoundSource(), 1.0F, 1.0F);
        }
    }
    default void applyFoodEffect(LivingEntity entity, SyringeFluidType type) {
        if (!(entity instanceof Player player) || entity.level().isClientSide || type == null || type.food().isEmpty()) return;

        FoodProperties food = type.food().get();
        if (player.getFoodData().needsFood()) {
            player.getFoodData().eat(food.getNutrition(), food.getSaturationModifier());
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }
    default void applyDrowningEffect(LivingEntity entity, DrowningData drowningData) {
        if (entity == null || entity.level().isClientSide || drowningData == null) return;

        float damagePerTick = drowningData.damagePerSecond();
        int durationTicks = drowningData.durationSeconds() != null ? drowningData.durationSeconds() * 20 : 100;

        entity.hurt(entity.damageSources().drown(), damagePerTick);

        if (entity instanceof Player player) {
            if (!player.level().isClientSide && player.level() instanceof ServerLevel serverLevel) {
                MinecraftServer server = serverLevel.getServer();
                server.execute(() -> handleDrowning(player, damagePerTick, durationTicks));
            }
        }

        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.DROWNED_HURT, entity.getSoundSource(), 1.0F, 1.0F);
    }
    private void handleDrowning(LivingEntity entity, float damagePerTick, int durationTicks) {
        final int[] ticksLeft = {durationTicks};

        Runnable tickTask = new Runnable() {
            @Override
            public void run() {
                if (ticksLeft[0] > 0) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        int currentAir = player.getAirSupply();
                        if (currentAir > 0) player.setAirSupply(currentAir - 1);
                    }
                    entity.hurt(entity.damageSources().drown(), damagePerTick);
                    ticksLeft[0]--;
                    entity.level().getServer().execute(this);
                }
                else {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        player.setAirSupply(0);
                    }
                }
            }
        };

        entity.level().getServer().execute(tickTask);
    }
    default void applyFreezingEffect(LivingEntity entity, FreezingData freezingData) {
        if (entity == null || entity.level().isClientSide || freezingData == null) return;
        int durationTicks = freezingData.durationSeconds() != null ? freezingData.durationSeconds() * 20 : 100;
        float damage = freezingData.damagePerSecond() != null ? freezingData.damagePerSecond() : 1.0f;
        float slowAmount = freezingData.slowAmount() != null ? freezingData.slowAmount() : 0.5f;

        int freezeThreshold = entity.getTicksRequiredToFreeze();
        entity.setTicksFrozen(freezeThreshold + durationTicks);

        entity.hurt(entity.damageSources().freeze(), damage);

        UUID slowId = UUID.nameUUIDFromBytes("bloodisfuel:freezing_slow".getBytes());
        AttributeInstance speedAttr = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttr != null) {
            AttributeModifier slowMod = new AttributeModifier(slowId, "Freezing Slowness",
                    -slowAmount, AttributeModifier.Operation.MULTIPLY_TOTAL);
            if (!speedAttr.hasModifier(slowMod)) speedAttr.addTransientModifier(slowMod);
        }

        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.PLAYER_HURT_FREEZE, entity.getSoundSource(), 1.0F, 1.0F);

        if (!entity.level().isClientSide && entity.level() instanceof ServerLevel serverLevel) {
            MinecraftServer server = serverLevel.getServer();
            server.execute(() -> {
                serverLevel.getServer().tell(new TickTask(1, new Runnable() {
                    int ticksLeft = durationTicks;
                    @Override
                    public void run() {
                        if (--ticksLeft <= 0) {
                            AttributeInstance attr = entity.getAttribute(Attributes.MOVEMENT_SPEED);
                            if (attr != null) attr.removeModifier(slowId);
                        } else serverLevel.getServer().tell(new TickTask(1, this));
                    }
                }));
            });
        }
    }

    /**
     * Syringe particle utilities.
     */
    default void spawnBloodParticles(Level level, Entity entity, ItemStack stack, int count) {
        if (!(level instanceof ServerLevel server)) return;
        int color = getBarColor(stack);
        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        Vector3f particleColor = new Vector3f(r, g, b);
        for (int i = 0; i < 10; i++) {
            double dx = (level.random.nextDouble() - 0.5) * 0.5;
            double dy = level.random.nextDouble();
            double dz = (level.random.nextDouble() - 0.5) * 0.5;
            server.sendParticles(new DustParticleOptions(particleColor, 1.0F),
                    entity.getX() + dx, entity.getY() + dy, entity.getZ() + dz,
                    1, 0.0, 0.0, 0.0, 0.0);
        }
    }
    default void spawnDrainingParticles(Level level, Entity entity, ItemStack stack, int count) {
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
    default void spawnTrailParticles(Level level, Entity entity, ItemStack stack, int count) {
        if (!(level instanceof ServerLevel server)) return;
        int color = getBarColor(stack);
        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        for (int i = 0; i < count; i++) {
            double x = entity.getX() + (level.random.nextDouble() - 0.5) * 0.2;
            double y = entity.getY() + level.random.nextDouble() * 0.6 + 0.5;
            double z = entity.getZ() + (level.random.nextDouble() - 0.5) * 0.2;
            server.sendParticles(
                    new ColorableDripParticleData(r, g, b),
                    x, y, z,
                    5, 0.0, 0.0, 0.0, 0.0
            );
        }
    }

    /**
     * Item bar utilities.
     */
    default boolean isBarVisible(ItemStack stack) {
        return getCurrentFillLevel(stack) > 0;
    }
    default int getBarWidth(ItemStack stack) {
        return Math.round(13 * (getCurrentFillLevel(stack) / (float) getCapacity(stack)));
    }
    default int getBarColor(ItemStack stack) {
        Level level = Minecraft.getInstance().player != null ? Minecraft.getInstance().player.level() : null;
        RegistryAccess access = level != null ? level.registryAccess() : null;

        FluidStack fluidStack = readFluid(stack);
        SyringeFluidType type = SyringeFluidTypeManager.fromFluid(fluidStack, access);

        return SyringeFluidTypeManager.getColor(type, fluidStack);
    }

    /**
     * Sound utilities.
     */
    default void playSound(Level level, Entity entity, SoundEvent sound) {
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    // simple fuel item with fixed burn time
    class FuelItems extends Item {
        private final int burnTime;

        public FuelItems(Properties properties, int burnTime) {
            super(properties);
            this.burnTime = burnTime;
        }

        @Override
        public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
            return burnTime;
        }
    }
}