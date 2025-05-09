package net.electrisoma.bloodisfuel.registry.items.syringe_blade;

import net.electrisoma.bloodisfuel.api.BCodecs;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.material.Fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@SuppressWarnings("all")
public record SyringeFluidType(
        HolderSet<Fluid> fluids, int color,
        Optional<MobEffectInstance> onEntityHitEffect,
        HolderSet<EntityType<?>> mobs) {

    public static final Codec<SyringeFluidType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.FLUID).fieldOf("fluids").forGetter(SyringeFluidType::fluids),
            Codec.INT.fieldOf("color").forGetter(SyringeFluidType::color),
            BCodecs.MOB_EFFECT_INSTANCE.optionalFieldOf("on_entity_hit").forGetter(SyringeFluidType::onEntityHitEffect),
            RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).fieldOf("mobs").forGetter(SyringeFluidType::mobs)
    ).apply(instance, SyringeFluidType::new));

    public Optional<Holder<EntityType<?>>> getEntityTypeHolder(EntityType<?> entityType) {
        return ForgeRegistries.ENTITY_TYPES.getHolder(entityType);
    }

    public static class Builder {
        private final List<Holder<Fluid>> fluids = new ArrayList<>();
        private int color = 0xFFFFFF;
        private MobEffectInstance onEntityHitEffect;
        private final List<Holder<EntityType<?>>> mobs = new ArrayList<>();

        // fluid
        public Builder addFluids(Fluid... fluids) {
            for (Fluid fluid : fluids)
                this.fluids.add(fluid.builtInRegistryHolder());
            return this;
        }

        // bar color
        public Builder color(int color) {
            this.color = color;
            return this;
        }

        // fluid effects
        public Builder onEntityHitEffect(MobEffectInstance effect) {
            this.onEntityHitEffect = effect;
            return this;
        }

        // mob association
        public Builder addMobs(EntityType<?>... types) {
            for (EntityType<?> type : types) {
                ForgeRegistries.ENTITY_TYPES.getHolder(type).ifPresent(mobs::add);
            }
            return this;
        }

        public SyringeFluidType build() {
            return new SyringeFluidType(
                    HolderSet.direct(fluids),
                    color,
                    Optional.ofNullable(onEntityHitEffect),
                    HolderSet.direct(mobs)
            );
        }
    }

    // potion type check
    public boolean isPotionType() {
        return fluids().stream().anyMatch(holder ->
                holder.unwrapKey()
                        .map(key -> key.location().getPath().equals("potion"))
                        .orElse(false)
        );
    }
}