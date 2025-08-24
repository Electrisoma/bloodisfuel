package net.electrisoma.bloodisfuel.api.data;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

import java.util.List;
import java.util.Optional;

public record OnHitEffects(Optional<List<EffectsData>> effects,
                           Optional<FoodProperties> food,
                           Optional<BurningData> burning,
                           Optional<ExtinguishingData> extinguishing,
                           Optional<DrowningData> drowning,
                           Optional<FreezingData> freezing,
                           Optional<TeleportationData> teleportation)
{}
