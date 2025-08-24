package net.electrisoma.bloodisfuel.api.equipment.syringe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.Create;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record SyringeExtractionType(
        List<HolderSet<EntityType<?>>> mobs,
        HolderSet<Fluid> fluid,
        Optional<Potion> potion,
        int amount) {
    public static final Codec<SyringeExtractionType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE))
                    .fieldOf("mobs")
                    .forGetter(SyringeExtractionType::mobs),
            RegistryCodecs.homogeneousList(Registries.FLUID)
                    .fieldOf("fluid")
                    .forGetter(SyringeExtractionType::fluid),
            ForgeRegistries.POTIONS.getCodec().optionalFieldOf("potion").forGetter(SyringeExtractionType::potion),
            Codec.INT.optionalFieldOf("amount", 1000)
                    .forGetter(SyringeExtractionType::amount)
    ).apply(instance, SyringeExtractionType::new));

    public boolean isPotionType() {
        ResourceLocation potionFluidId = new ResourceLocation(Create.ID, "potion");
        return fluid.stream()
                .map(Holder::value)
                .anyMatch(f -> Objects.equals(ForgeRegistries.FLUIDS.getKey(f), potionFluidId));
    }

    public static class Builder {
        private final List<HolderSet<EntityType<?>>> mobSets = new ArrayList<>();
        private HolderSet<Fluid> fluidSet = null;
        private Optional<Potion> potion = Optional.empty();
        private int amount = 1000;

        public Builder addMobs(EntityType<?>... types) {
            List<Holder<EntityType<?>>> holders = new ArrayList<>();
            for (EntityType<?> type : types)
                ForgeRegistries.ENTITY_TYPES.getHolder(type).ifPresent(holders::add);
            if (!holders.isEmpty())
                mobSets.add(HolderSet.direct(holders));
            return this;
        }

        public Builder addMobs(String... ids) {
            for (String idStr : ids) {
                ResourceLocation id = new ResourceLocation(idStr);
                EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(id);
                if (type != null) addMobs(type);
                else BloodIsFuel.LOGGER.info("Unknown entity type: {}", idStr);
            }
            return this;
        }

        public Builder mobTag(String namespace, String tag, HolderLookup.RegistryLookup<EntityType<?>> lookup) {
            return mobTag(new ResourceLocation(namespace, tag), lookup);
        }

        public Builder mobTag(ResourceLocation tagId, HolderLookup.RegistryLookup<EntityType<?>> lookup) {
            TagKey<EntityType<?>> tag = TagKey.create(Registries.ENTITY_TYPE, tagId);
            HolderSet.Named<EntityType<?>> namedSet = lookup.getOrThrow(tag);
            mobSets.add(namedSet);
            return this;
        }

        public Builder fluid(String fluidId) {
            ResourceLocation id = new ResourceLocation(fluidId);
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(id);
            if (fluid != null)
                ForgeRegistries.FLUIDS.getHolder(fluid)
                        .ifPresent(holder -> this.fluidSet = HolderSet.direct(List.of(holder)));
            else BloodIsFuel.LOGGER.info("Unknown fluid: {}", fluidId);
            return this;
        }

        public Builder fluid(Fluid fluid) {
            ForgeRegistries.FLUIDS.getHolder(fluid)
                    .ifPresent(holder -> this.fluidSet = HolderSet.direct(List.of(holder)));
            return this;
        }

        public Builder fluidTag(String namespace, String tag, HolderLookup.RegistryLookup<Fluid> lookup) {
            return fluidTag(new ResourceLocation(namespace, tag), lookup);
        }

        public Builder fluidTag(ResourceLocation tagId, HolderLookup.RegistryLookup<Fluid> lookup) {
            TagKey<Fluid> tag = TagKey.create(Registries.FLUID, tagId);
            this.fluidSet = lookup.getOrThrow(tag);
            return this;
        }

        public Builder potion(Potion potion) {
            this.potion = Optional.of(potion);
            return this;
        }
        public Builder potion(String potionId) {
            Potion potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(potionId));
            if (potion == null)
                throw new IllegalArgumentException("Unknown potion: " + potionId);
            this.potion = Optional.of(potion);
            return this;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public SyringeExtractionType build() {
            if (mobSets.isEmpty())
                throw new IllegalStateException("No mobs defined for SyringeExtractionType.");
            if (fluidSet == null)
                throw new IllegalStateException("No fluid or fluid tag defined for SyringeExtractionType.");

            return new SyringeExtractionType(
                    List.copyOf(mobSets),
                    fluidSet,
                    potion,
                    amount
            );
        }
    }
}
