package net.electrisoma.bloodisfuel.api.utils;

import net.electrisoma.bloodisfuel.api.data.EffectsData;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidTypeManager;

import com.simibubi.create.foundation.utility.CreateLang;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;

import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;


@SuppressWarnings({"DataFlowIssue", "RedundantSuppression", "BooleanMethodIsAlwaysInverted"})
public interface TooltipUtils extends CombatContextUtils, FluidUtils {
    default String formatDuration(int ticks) {
        int seconds = ticks / 20;
        return String.format("%d:%02d", seconds / 60, seconds % 60);
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
        Level level = Minecraft.getInstance().player != null
                ? Minecraft.getInstance().player.level()
                : null;
        RegistryAccess access = level != null ? level.registryAccess() : null;

        FluidStack fluid = readFluid(stack);
        SyringeFluidType type = SyringeFluidTypeManager.fromFluid(fluid, access);
        return SyringeFluidTypeManager.getColor(type, fluid);
    }

    default void tooltipMaker(List<Component> tooltip, ItemStack stack) {
        FluidStack fluid = readFluid(stack);

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
    }
    default void itemToolTipMaker(List<Component> tooltip, ItemStack stack, @Nullable RegistryAccess registryAccess) {
        FluidStack fluid = readFluid(stack);
        SyringeFluidType type = SyringeFluidTypeManager.fromFluid(fluid, registryAccess);

        List<MobEffectInstance> effects = type.isPotionType() && fluid.hasTag()
                ? PotionUtils.getAllEffects(fluid.getTag())
                : type.effects()
                .map(list -> list.stream()
                        .filter(EffectsData::visibleInTooltips)
                        .map(EffectsData::effect)
                        .toList())
                .orElse(List.of());

        for (MobEffectInstance effect : effects) {
            Component name = Component.translatable(effect.getDescriptionId())
                    .withStyle(effect.getEffect().isBeneficial() ? ChatFormatting.GREEN : ChatFormatting.RED);
            Component level = Component.literal(" " + toRoman(effect.getAmplifier() + 1))
                    .withStyle(ChatFormatting.GOLD);
            Component duration = effect.getDuration() > 1
                    ? Component.literal(" (" + formatDuration(effect.getDuration()) + ")")
                    .withStyle(ChatFormatting.GRAY)
                    : Component.empty();

            tooltip.add(Component.literal("• ").withStyle(ChatFormatting.GRAY)
                    .append(Component.translatable("bloodisfuel.tooltip.effect").withStyle(ChatFormatting.GRAY))
                    .append(": ").append(name).append(level).append(duration));
        }

        if (type.hasBurning()) {
            type.burning().ifPresent(burning -> {
                int duration = burning.durationSeconds();
                float dps = burning.damagePerSecond();

                MutableComponent line = Component.literal("• ")
                        .withStyle(ChatFormatting.GRAY)
                        .append(Component.translatable("bloodisfuel.tooltip.burning").withStyle(ChatFormatting.RED))
                        .append(": ");

                if (duration > 0) {
                    line.append(Component.literal(String.valueOf(duration)).withStyle(ChatFormatting.GOLD))
                            .append(Component.translatable("bloodisfuel.tooltip.seconds").withStyle(ChatFormatting.GOLD));
                }

                if (dps > 0) {
                    if (duration > 0) line.append(" ");
                    line.append(Component.literal("(" + dps + " ").withStyle(ChatFormatting.RED))
                            .append(Component.translatable("bloodisfuel.tooltip.damage"))
                            .append(Component.literal("/"))
                            .append(Component.translatable("bloodisfuel.tooltip.seconds"))
                            .append(Component.literal(")"));
                }

                tooltip.add(line);
            });
        }

        if (type.hasExtinguishing()) {
            type.extinguishing().ifPresent(ext -> {
                int duration = ext.durationSeconds();
                float heal = ext.healPerSecond();

                MutableComponent line = Component.literal("• ")
                        .withStyle(ChatFormatting.GRAY)
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

        if (!ctx.canAttack()) return;

        Optional<Float> optDamage = ctx.type() != null ? ctx.type().damage() : Optional.empty();
        if (optDamage.isEmpty()) return;

        float damage = optDamage.get();
        var player = Minecraft.getInstance().player;
        if (player != null) damage += (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);

        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("item.modifiers.mainhand").withStyle(ChatFormatting.GRAY));

        String dmgText = damage % 1.0f == 0 ? String.valueOf((int) damage) : String.format("%.2f", damage);
        tooltip.add(Component.literal(" ")
                .append(Component.literal(dmgText))
                .append(" ")
                .append(Component.translatable("bloodisfuel.tooltip.syringe_gun.damage"))
                .withStyle(ChatFormatting.DARK_GREEN));
    }
}