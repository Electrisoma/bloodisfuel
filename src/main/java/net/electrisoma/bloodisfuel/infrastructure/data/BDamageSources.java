package net.electrisoma.bloodisfuel.infrastructure.data;

import net.electrisoma.bloodisfuel.registry.BDamageTypes;

import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageSource;


public class BDamageSources {

    public static DamageSource boiling(Level level) {
        return source(BDamageTypes.BOILING, level);
    }


    private static DamageSource source(ResourceKey<DamageType> key, LevelReader level) {
        Registry<DamageType> registry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        return new DamageSource(registry.getHolderOrThrow(key));
    }

    private static DamageSource source(ResourceKey<DamageType> key, LevelReader level, Entity entity) {
        Registry<DamageType> registry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        return new DamageSource(registry.getHolderOrThrow(key), entity);
    }

    private static DamageSource source(ResourceKey<DamageType> key, LevelReader level, Entity causingEntity, Entity directEntity) {
        Registry<DamageType> registry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        return new DamageSource(registry.getHolderOrThrow(key), causingEntity, directEntity);
    }

}
