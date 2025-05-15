package net.electrisoma.bloodisfuel.api.utils;

import net.electrisoma.bloodisfuel.api.equipment.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.equipment.SyringeFluidTypeManager;

import com.simibubi.create.foundation.utility.CreateLang;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;

import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;


@SuppressWarnings({"DataFlowIssue", "RedundantSuppression", "BooleanMethodIsAlwaysInverted"})
public interface TooltipUtils extends CombatContextUtils, FluidUtils {

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

    default void tooltipMaker(java.util.List<Component> tooltip, ItemStack stack, @Nullable RegistryAccess registryAccess) {
        FluidStack fluid = readFluid(stack);
        SyringeFluidType type = SyringeFluidTypeManager.fromFluid(fluid, registryAccess);
        java.util.List<MobEffectInstance> effects = SyringeFluidTypeManager.getEffects(type, fluid);
        // empty
        if (stack.getTag() == null || fluid.isEmpty()) {
            tooltip.add(Component.translatable("bloodisfuel.tooltip.empty").withStyle(ChatFormatting.GRAY));
            return;
        }
        // fluid
        tooltip.add(CreateLang.fluidName(fluid).component()
                .withStyle(ChatFormatting.GRAY)
                .append(" ")
                .append(CreateLang.number(fluid.getAmount()).style(ChatFormatting.GOLD).component())
                .append(Component.translatable("create.generic.unit.millibuckets").withStyle(ChatFormatting.GOLD))
                .append(" / ")
                .append(CreateLang.number(getCapacity(stack)).style(ChatFormatting.GRAY).component())
                .append(Component.translatable("create.generic.unit.millibuckets").withStyle(ChatFormatting.GRAY)));
        // mob effect
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
        // burning
        if (type != null && type.hasBurning()) {
            type.burning().ifPresent(burning -> {
                int duration = burning.durationSeconds();
                float damage = burning.damagePerSecond();

                MutableComponent line = Component.literal("• ").withStyle(ChatFormatting.GRAY)
                        .append(Component.translatable("bloodisfuel.tooltip.burning").withStyle(ChatFormatting.RED))
                        .append(": ");

                if (duration > 0) 
                    line.append(Component.literal(String.valueOf(duration)).withStyle(ChatFormatting.GOLD))
                            .append(Component.translatable("bloodisfuel.tooltip.seconds").withStyle(ChatFormatting.GOLD));
                
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
        // extinguishing
        if (type != null && type.hasExtinguishing()) {
            type.extinguishing().ifPresent(extinguishing -> {
                int duration = extinguishing.durationSeconds();
                float heal = extinguishing.healPerSecond();

                MutableComponent line = Component.literal("• ").withStyle(ChatFormatting.GRAY)
                        .append(Component.translatable("bloodisfuel.tooltip.extinguishing").withStyle(ChatFormatting.AQUA))
                        .append(": ");

                if (duration > 0)
                    line.append(Component.literal(String.valueOf(duration)).withStyle(ChatFormatting.GOLD))
                            .append(Component.translatable("bloodisfuel.tooltip.seconds").withStyle(ChatFormatting.GOLD));

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
}
