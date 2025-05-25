package net.electrisoma.bloodisfuel.api.equipment.engine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.List;

public record EngineFluidType(
        List<HolderSet<Fluid>> fluids,
        float speed,
        float strength,
        float burnRate) {
    public static final Codec<EngineFluidType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(RegistryCodecs.homogeneousList(Registries.FLUID)).fieldOf("fluids").forGetter(EngineFluidType::fluids),
            Codec.FLOAT.fieldOf("speed").forGetter(EngineFluidType::speed),
            Codec.FLOAT.fieldOf("strength").forGetter(EngineFluidType::strength),
            Codec.FLOAT.fieldOf("burnRate").forGetter(EngineFluidType::burnRate)
    ).apply(instance, EngineFluidType::new));

    @SuppressWarnings("unused")
    public static class Builder {
        private final List<HolderSet<Fluid>> fluidSets = new ArrayList<>();
        private float speed;
        private float strength;
        private float burnRate;
        /**
         * Adds specific fluids that this type applies to.
         */
        public EngineFluidType.Builder fluids(Fluid... fluids) {
            List<Holder<Fluid>> holders = new ArrayList<>();
            for (Fluid fluid : fluids)
                //noinspection deprecation
                holders.add(fluid.builtInRegistryHolder());
            fluidSets.add(HolderSet.direct(holders));
            return this;
        }
        /**
         * Adds fluid tags that this type applies to.
         */
        public EngineFluidType.Builder fluidTag(String namespace, String tag, HolderLookup.RegistryLookup<Fluid> lookup) {
            ResourceLocation tagLocation = new ResourceLocation(namespace, tag);
            return fluidTag(tagLocation, lookup);
        }
        public EngineFluidType.Builder fluidTag(String path, HolderLookup.RegistryLookup<Fluid> lookup) {
            ResourceLocation tagLocation = new ResourceLocation(path);
            return fluidTag(tagLocation, lookup);
        }
        public EngineFluidType.Builder fluidTag(ResourceLocation tagId, HolderLookup.RegistryLookup<Fluid> lookup) {
            TagKey<Fluid> tag = TagKey.create(Registries.FLUID, tagId);
            HolderSet.Named<Fluid> tagSet = lookup.getOrThrow(tag);
            fluidSets.add(tagSet);
            return this;
        }
        /**
         * Adds fuel stats to this type.
         */
        public EngineFluidType.Builder stats(float speed, float strength, float burnRate) {
            this.speed = speed;
            this.strength = strength;
            this.burnRate = burnRate;
            return this;
        }
        public EngineFluidType.Builder speed(float speed) {
            this.speed = speed;
            return this;
        }
        public EngineFluidType.Builder strength(float strength) {
            this.strength = strength;
            return this;
        }
        public EngineFluidType.Builder burnRate(float burnRate) {
            this.burnRate = burnRate;
            return this;
        }
        /**
         * Builds the EngineFluidType instance.
         */
        public EngineFluidType build() {
            return new EngineFluidType(
                    fluidSets,
                    speed,
                    strength,
                    burnRate
            );
        }
    }
}