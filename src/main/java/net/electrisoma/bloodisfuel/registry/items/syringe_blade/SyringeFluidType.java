package net.electrisoma.bloodisfuel.registry.items.syringe_blade;

import net.electrisoma.bloodisfuel.api.BCodecs;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.material.Fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record SyringeFluidType(
        HolderSet<Fluid> fluids,
        int color,
        Optional<MobEffectInstance> onEntityHitEffect) {

    public static final Codec<SyringeFluidType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.FLUID).fieldOf("fluids").forGetter(SyringeFluidType::fluids),
            Codec.INT.fieldOf("color").forGetter(SyringeFluidType::color),
            BCodecs.MOB_EFFECT_INSTANCE.optionalFieldOf("on_entity_hit").forGetter(SyringeFluidType::onEntityHitEffect)
    ).apply(instance, SyringeFluidType::new));

    public static class Builder {
        private final List<Holder<Fluid>> fluids = new ArrayList<>();
        private int color = 0xFFFFFF;
        private MobEffectInstance onEntityHitEffect;

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder onEntityHitEffect(MobEffectInstance effect) {
            this.onEntityHitEffect = effect;
            return this;
        }

        public Builder addFluids(Fluid... fluids) {
            for (Fluid fluid : fluids)
                this.fluids.add(fluid.builtInRegistryHolder());
            return this;
        }

        public SyringeFluidType build() {
            return new SyringeFluidType(
                    HolderSet.direct(fluids),
                    color,
                    Optional.ofNullable(onEntityHitEffect)
            );
        }
    }

    public boolean isPotionType() {
        return fluids().stream().anyMatch(holder ->
                holder.unwrapKey()
                        .map(key -> key.location().getPath().equals("potion"))
                        .orElse(false)
        );
    }
}