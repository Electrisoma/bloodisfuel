package net.electrisoma.bloodisfuel.api.equipment.syringe;

import net.electrisoma.bloodisfuel.api.data.*;

import com.simibubi.create.Create;

import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.material.Fluid;

import net.minecraftforge.registries.ForgeRegistries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Represents a fluid type used in the Syringe system.
 * Can define which fluids match, display color, its effects on hit, and which mobs drop it.
 * This system can also be used by a data generator for convenience.
 */
@SuppressWarnings("unused")
public record SyringeFluidType(
        List<HolderSet<Fluid>> fluids,
        int color,
        Optional<Boolean> glowing,
        Optional<Boolean> opaque,
        Optional<Float> damage,
        Optional<Float> attackSpeed,
        Optional<FoodProperties> food,
        Optional<MobEffectInstance> effect,
        Optional<HolderSet<EntityType<?>>> mobs,
        Optional<BurningData> burning,
        Optional<ExtinguishingData> extinguishing,
        Optional<DrowningData> drowning,
        Optional<FreezingData> freezing) {
    public static final Codec<SyringeFluidType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(RegistryCodecs.homogeneousList(Registries.FLUID)).fieldOf("fluids").forGetter(SyringeFluidType::fluids),
            Codec.INT.fieldOf("color").forGetter(SyringeFluidType::color),
            Codec.BOOL.optionalFieldOf("glowing").forGetter(SyringeFluidType::glowing),
            Codec.BOOL.optionalFieldOf("opaque").forGetter(SyringeFluidType::opaque),
            Codec.FLOAT.optionalFieldOf("damage").forGetter(SyringeFluidType::damage),
            Codec.FLOAT.optionalFieldOf("attack_speed").forGetter(SyringeFluidType::attackSpeed),
            BCodecs.FOOD_PROPERTIES.optionalFieldOf("food").forGetter(SyringeFluidType::food),
            BCodecs.MOB_EFFECT_INSTANCE.optionalFieldOf("effect").forGetter(SyringeFluidType::effect),
            RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).optionalFieldOf("mobs").forGetter(SyringeFluidType::mobs),
            BCodecs.BURNING_DATA.optionalFieldOf("burning").forGetter(SyringeFluidType::burning),
            BCodecs.EXTINGUISHING_DATA.optionalFieldOf("extinguishing").forGetter(SyringeFluidType::extinguishing),
            BCodecs.DROWNING_DATA.optionalFieldOf("drowning").forGetter(SyringeFluidType::drowning),
            BCodecs.FREEZING_DATA.optionalFieldOf("freezing").forGetter(SyringeFluidType::freezing)
    ).apply(instance, SyringeFluidType::new));

    /**
     * Returns true if this fluid type is a potion-based fluid.
     */
    public boolean isPotionType() {
        ResourceLocation potionFluidId = new ResourceLocation(Create.ID, "potion");
        return fluids().stream()
                .flatMap(HolderSet::stream)
                .map(Holder::value)
                .anyMatch(fluid -> Objects.equals(ForgeRegistries.FLUIDS.getKey(fluid), potionFluidId));
    }

    /**
     * Returns true if this fluid type can burn.
     */
    public boolean hasBurning() {
        return burning.isPresent();
    }

    /**
     * Returns true if this fluid type can extinguish.
     */
    public boolean hasExtinguishing() {
        return extinguishing.isPresent();
    }

    /**
     * Returns true if this fluid type can burn.
     */
    public boolean hasDrowning() {
        return drowning.isPresent();
    }

    /**
     * Returns true if this fluid type can extinguish.
     */
    public boolean hasFreezing() {
        return freezing.isPresent();
    }

    /**
     * Returns true if this fluid type can feed.
     */
    public boolean hasFood() {
        return food.isPresent();
    }

    /**
     * Returns the default damage set by the fallback.
     */
    public float getDamageOrDefault(float fallback) {
        return damage.orElse(fallback);
    }

    /**
     * Returns the default attack speed set by the fallback.
     */
    public float getAttackSpeedOrDefault(float fallback) {
        return attackSpeed.orElse(fallback);
    }

    @SuppressWarnings({"deprecation", "OptionalUsedAsFieldOrParameterType", "RedundantSuppression"})
    public static class Builder {
        private final List<HolderSet<Fluid>> fluidSets = new ArrayList<>();
        private int color = 0xFFFFFF;
        private Optional<Boolean> glowing = Optional.empty();
        private Optional<Boolean> opaque = Optional.empty();
        private Optional<Float> damage = Optional.empty();
        private Optional<Float> attackSpeed = Optional.empty();
        private Optional<FoodProperties> food = Optional.empty();
        private MobEffectInstance effect;
        private final List<Holder<EntityType<?>>> mobs = new ArrayList<>();
        private Optional<BurningData> burning = Optional.empty();
        private Optional<ExtinguishingData> extinguishing = Optional.empty();
        private Optional<DrowningData> drowning = Optional.empty();
        private Optional<FreezingData> freezing = Optional.empty();

        /**
         * Adds specific fluids that this type applies to.
         */
        public Builder fluids(Fluid... fluids) {
            List<Holder<Fluid>> holders = new ArrayList<>();
            for (Fluid fluid : fluids)
                holders.add(fluid.builtInRegistryHolder());
            fluidSets.add(HolderSet.direct(holders));
            return this;
        }

        /**
         * Adds fluid tags that this type applies to.
         */
        public Builder fluidTag(String namespace, String tag, HolderLookup.RegistryLookup<Fluid> lookup) {
            ResourceLocation tagLocation = new ResourceLocation(namespace, tag);
            return fluidTag(tagLocation, lookup);
        }
        public Builder fluidTag(String path, HolderLookup.RegistryLookup<Fluid> lookup) {
            ResourceLocation tagLocation = new ResourceLocation(path);
            return fluidTag(tagLocation, lookup);
        }
        public Builder fluidTag(ResourceLocation tagId, HolderLookup.RegistryLookup<Fluid> lookup) {
            TagKey<Fluid> tag = TagKey.create(Registries.FLUID, tagId);
            HolderSet.Named<Fluid> tagSet = lookup.getOrThrow(tag);
            fluidSets.add(tagSet);
            return this;
        }

        /**
         * Sets appearance of the fluid type.
         */
        public Builder appearance(int color, boolean opaque) {
            this.color = color;
            this.opaque = Optional.of(opaque);
            return this;
        }
        public Builder appearance(int color, boolean opaque, boolean glowing) {
            this.color = color;
            this.opaque = Optional.of(opaque);
            this.glowing = Optional.of(glowing);
            return this;
        }
        public Builder color(int color) {
            this.color = color;
            return this;
        }
        public Builder opaque(boolean opaque) {
            this.opaque = Optional.of(opaque);
            return this;
        }
        public Builder glowing(boolean glowing) {
            this.glowing = Optional.of(glowing);
            return this;
        }

        /**
         * Sets the stats that the item or projectile does on hit.
         */
        public Builder stats(float damage, float attackSpeed) {
            this.damage = Optional.of(damage);
            this.attackSpeed = Optional.of(attackSpeed);
            return this;
        }
        public Builder damage(float damage) {
            this.damage = Optional.of(damage);
            return this;
        }
        public Builder attackSpeed(float attackSpeed) {
            this.attackSpeed = Optional.of(attackSpeed);
            return this;
        }

        /**
         * Defines the food properties applied when the fluid type hits an entity.
         */
        public Builder food(int nutrition, float saturationMod) {
            new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturationMod).build();
            return this;
        }
        public Builder food(FoodProperties foodProperties) {
            this.food = Optional.ofNullable(foodProperties);
            return this;
        }

        /**
         * Defines the effect applied when the fluid type hits an entity.
         */
        public Builder effect(MobEffect effect, int duration) {
            this.effect = new MobEffectInstance(effect, duration, 0, false, true);
            return this;
        }
        public Builder effect(MobEffect effect, int duration, int amplifier) {
            this.effect = new MobEffectInstance(effect, duration, amplifier, false, true);
            return this;
        }
        public Builder effect(MobEffect effect, int duration, int amplifier, boolean ambient, boolean visible) {
            this.effect = new MobEffectInstance(effect, duration, amplifier, ambient, visible);
            return this;
        }
        public Builder effect(MobEffectInstance effect) {
            this.effect = effect;
            return this;
        }

        /**
         * Adds mobs associated with the fluid type.
         */
        public Builder mobs(EntityType<?>... types) {
            for (EntityType<?> type : types)
                ForgeRegistries.ENTITY_TYPES.getHolder(type).ifPresent(mobs::add);
            return this;
        }

        /**
         * Adds status effects to the fluid type.
         */
        public Builder burning(int durationSeconds, float damagePerSecond) {
            new BurningData(durationSeconds, damagePerSecond);
            return this;
        }
        public Builder burning(BurningData burningData) {
            this.burning = Optional.ofNullable(burningData);
            return this;
        }

        public Builder extinguishing(int durationSeconds, float healthPerSecond) {
            new ExtinguishingData(durationSeconds, healthPerSecond);
            return this;
        }
        public Builder extinguishing(ExtinguishingData extinguishingData) {
            this.extinguishing = Optional.ofNullable(extinguishingData);
            return this;
        }

        public Builder drowning(int durationSeconds, float damagePerSecond) {
            new DrowningData(durationSeconds, damagePerSecond);
            return this;
        }
        public Builder drowning(DrowningData drowningData) {
            this.drowning = Optional.ofNullable(drowningData);
            return this;
        }

        public Builder freezing(int durationSeconds, float damagePerSecond, float slowAmount) {
            new FreezingData(durationSeconds, damagePerSecond, slowAmount);
            return this;
        }
        public Builder freezing(FreezingData freezingData) {
            this.freezing = Optional.ofNullable(freezingData);
            return this;
        }

        /**
         * Builds the SyringeFluidType instance.
         */
        public SyringeFluidType build() {
            return new SyringeFluidType(
                    fluidSets,
                    color,
                    glowing,
                    opaque,
                    damage,
                    attackSpeed,
                    food,
                    Optional.ofNullable(effect),
                    mobs.isEmpty() ? Optional.empty() : Optional.of(HolderSet.direct(mobs)),
                    burning,
                    extinguishing,
                    drowning,
                    freezing
            );
        }
    }
}