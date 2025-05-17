package net.electrisoma.bloodisfuel.infrastructure.data;

import net.electrisoma.bloodisfuel.infrastructure.data.entries.BDamageTypes;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageSource;


@SuppressWarnings("unused")
public class BDamageSources {
    public static DamageSource boiling(Level level) {
        return source(BDamageTypes.BOILING, level);
    }
    public static DamageSource syringeBlade(Level level, Entity causingEntity, Entity directEntity) {
        return source(BDamageTypes.SYRINGE_BLADE, level, causingEntity, directEntity);
    }
    public static DamageSource syringeGun(Level level, Entity causingEntity, Entity directEntity) {
        return source(BDamageTypes.SYRINGE_GUN, level, causingEntity, directEntity);
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