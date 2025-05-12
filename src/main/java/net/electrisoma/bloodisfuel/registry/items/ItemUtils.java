package net.electrisoma.bloodisfuel.registry.items;

import com.simibubi.create.AllEnchantments;
import com.simibubi.create.foundation.utility.CreateLang;
import net.electrisoma.bloodisfuel.api.BurningData;
import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.infrastructure.data.entries.BSyringeFluidTypes;
import net.electrisoma.bloodisfuel.registry.BEnchantments;
import net.electrisoma.bloodisfuel.api.equipment.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.equipment.SyringeFluidTypeManager;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;


@SuppressWarnings("all")
public interface ItemUtils {

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

    default FluidStack readFluid(ItemStack stack) {
        return FluidStack.loadFluidStackFromNBT(stack.getOrCreateTag().getCompound("Fluid"));
    }

    default void writeFluid(ItemStack stack, FluidStack fluid) {
        stack.getOrCreateTag().put("Fluid", fluid.writeToNBT(new CompoundTag()));
    }

    default int getCurrentFillLevel(ItemStack stack) {
        return readFluid(stack).getAmount();
    }

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

    // matching fluid for mobs
    default SyringeFluidType getMatchingFluid(LivingEntity target, RegistryAccess access) {
        return SyringeFluidTypeManager.getAll(access).stream()
                .filter(type -> ForgeRegistries.ENTITY_TYPES.getHolder(target.getType())
                        .map(holder -> type.mobs().map(set -> set.contains(holder)).orElse(false))
                        .orElse(false))
                .findFirst().orElse(null);
    }

    // fallback fluid for mobs without set fluids
    default SyringeFluidType getFallback(RegistryAccess access) {
        return access.registryOrThrow(BRegistries.SYRINGE_BLADE_FLUIDS)
                .getOptional(BSyringeFluidTypes.FALLBACK)
                .orElse(null);
    }

    default void applyBurningEffect(LivingEntity entity, BurningData burningData) {
        if (entity == null || entity.level().isClientSide) return;

        entity.setSecondsOnFire(burningData.durationSeconds());
        if (burningData.damagePerSecond() > 0) {
            entity.hurt(entity.damageSources().onFire(), burningData.damagePerSecond());
        }

        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.FIRE_AMBIENT, entity.getSoundSource(), 1.0F, 1.0F);
    }

    default void playSound(Level level, Entity entity, SoundEvent sound) {
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

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

    default void spawnBloodParticles(Level level, Entity entity, ItemStack stack) {
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

    default void tooltipMaker(List<Component> tooltip, ItemStack stack, @Nullable RegistryAccess registryAccess) {
        FluidStack fluid = readFluid(stack);
        SyringeFluidType type = SyringeFluidTypeManager.fromFluid(fluid, registryAccess);
        List<MobEffectInstance> effects = SyringeFluidTypeManager.getEffects(type, fluid);

        if (stack.getTag() == null || fluid.isEmpty()) {
            tooltip.add(Component.translatable("bloodisfuel.tooltip.empty").withStyle(ChatFormatting.GRAY));
            return;
        }

        tooltip.add(CreateLang.fluidName(fluid).component()
                .withStyle(ChatFormatting.GRAY)
                .append(" ")
                .append(CreateLang.number(fluid.getAmount()).style(ChatFormatting.GOLD).component())
                .append(Component.translatable("create.generic.unit.millibuckets").withStyle(ChatFormatting.GOLD))
                .append(" / ")
                .append(CreateLang.number(getCapacity(stack)).style(ChatFormatting.GRAY).component())
                .append(Component.translatable("create.generic.unit.millibuckets").withStyle(ChatFormatting.GRAY)));

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

        if (type != null && type.hasBurning()) {
            type.burning().ifPresent(burning -> {
                int duration = burning.durationSeconds();
                float damage = burning.damagePerSecond();

                MutableComponent burningLine = Component.literal("• ")
                        .withStyle(ChatFormatting.GRAY)
                        .append(Component.translatable("bloodisfuel.tooltip.burning")
                                .withStyle(ChatFormatting.DARK_RED))
                        .append(": ")
                        .append(Component.literal(duration + "s")
                                .withStyle(ChatFormatting.GOLD));

                if (damage > 0) {
                    burningLine.append(Component.literal(" (" + damage + " dmg/s)")
                            .withStyle(ChatFormatting.RED));
                }

                tooltip.add(burningLine);
            });
        }
    }

    default FluidHandlerItemStack getFluidHandler(ItemStack stack) {
        return new ToolItemFluidHandler(stack, getCapacity(stack), this::readFluid, this::writeFluid);
    }

    // fluid handler with read/write delegation
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

    record CombatContext(FluidStack fluid, SyringeFluidType type, int useAmount, boolean canAttack,
                         boolean onlyBeneficial) {}

    default CombatContext getCombatContext(ItemStack stack, @Nullable RegistryAccess access) {
        FluidStack fluidStack = readFluid(stack);
        SyringeFluidType type = SyringeFluidTypeManager.fromFluid(fluidStack, access);
        int capacity = getCapacity(stack);
        int charges = getChargeCount(stack);
        int useAmount = getUseAmount(capacity, charges);
        int currentFill = fluidStack.getAmount();
        List<MobEffectInstance> effects = SyringeFluidTypeManager.getEffects(type, fluidStack);
        boolean onlyBeneficial = !effects.isEmpty() && effects.stream().allMatch(e -> e.getEffect().isBeneficial());
        boolean canAttack = currentFill >= useAmount && !onlyBeneficial;

        return new CombatContext(fluidStack, type, useAmount, canAttack, onlyBeneficial);
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
