package net.electrisoma.bloodisfuel.api.data;

import net.minecraft.world.effect.MobEffectInstance;

public record EffectsData (MobEffectInstance effect, boolean visibleInTooltips) {}
