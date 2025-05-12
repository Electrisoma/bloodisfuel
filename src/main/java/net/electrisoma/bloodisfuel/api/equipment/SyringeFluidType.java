package net.electrisoma.bloodisfuel.api.equipment;

import net.electrisoma.bloodisfuel.api.BCodecs;

import net.electrisoma.bloodisfuel.api.BurningData;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.material.Fluid;

import net.minecraftforge.registries.ForgeRegistries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Represents a fluid type used in the Syringe system.
 * Can define which fluids match, display color, its effects on hit, and which mobs drop it.
 */
@SuppressWarnings("all")
public record SyringeFluidType(
        HolderSet<Fluid> fluids,
        int color,
        Optional<MobEffectInstance> onEntityHitEffect,
        Optional<HolderSet<EntityType<?>>> mobs,
        Optional<BurningData> burning) {
    public static final Codec<SyringeFluidType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.FLUID).fieldOf("fluids").forGetter(SyringeFluidType::fluids),
            Codec.INT.fieldOf("color").forGetter(SyringeFluidType::color),
            BCodecs.MOB_EFFECT_INSTANCE.optionalFieldOf("on_entity_hit").forGetter(SyringeFluidType::onEntityHitEffect),
            RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).optionalFieldOf("mobs").forGetter(SyringeFluidType::mobs),
            BCodecs.BURNING_DATA.optionalFieldOf("burning").forGetter(SyringeFluidType::burning)
    ).apply(instance, SyringeFluidType::new));

    /**
     * Returns true if this fluid type is a potion-based fluid.
     */
    public boolean isPotionType() {
        return fluids().stream()
                .anyMatch(holder -> holder.unwrapKey()
                        .map(key -> "potion".equals(key.location().getPath()))
                        .orElse(false));
    }

    /**
     * Returns true if this fluid type can burn.
     */
    public boolean hasBurning() {
        return burning.isPresent();
    }

    public static class Builder {
        private final List<Holder<Fluid>> fluids = new ArrayList<>();
        private int color = 0xFFFFFF;
        private MobEffectInstance onEntityHitEffect;
        private final List<Holder<EntityType<?>>> mobs = new ArrayList<>();
        private Optional<BurningData> burning = Optional.empty();

        /**
         * Adds one or more fluids that this type applies to.
         */
        public Builder addFluids(Fluid... fluids) {
            for (Fluid fluid : fluids) {
                this.fluids.add(fluid.builtInRegistryHolder());
            } return this;
        }

        /**
         * Sets the display color of the fluid type.
         */
        public Builder color(int color) {
            this.color = color;
            return this;
        }

        /**
         * Defines the effect applied when this fluid type hits an entity.
         */
        public Builder onEntityHitEffect(MobEffectInstance effect) {
            this.onEntityHitEffect = effect;
            return this;
        }

        /**
         * Adds mobs associated with this fluid type.
         */
        public Builder addMobs(EntityType<?>... types) {
            for (EntityType<?> type : types) {
                ForgeRegistries.ENTITY_TYPES.getHolder(type).ifPresent(mobs::add);
            } return this;
        }

        /**
         * Adds a burning effect to this fluid type.
         */
        public Builder burning(BurningData burningData) {
            this.burning = Optional.ofNullable(burningData);
            return this;
        }

        /**
         * Builds the SyringeFluidType instance.
         */
        public SyringeFluidType build() {
            return new SyringeFluidType(
                    HolderSet.direct(fluids),
                    color,
                    Optional.ofNullable(onEntityHitEffect),
                    mobs.isEmpty() ? Optional.empty() : Optional.of(HolderSet.direct(mobs)),
                    burning
            );
        }
    }
}