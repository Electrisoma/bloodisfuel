package net.electrisoma.bloodisfuel.infrastructure.data;

import com.mojang.serialization.JsonOps;
import com.simibubi.create.foundation.pack.DynamicPack;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SyringeFluidRuntimeDataGenerator {
    private static final ResourceLocation BLOODISFUEL_MOLTEN_TAG =
            new ResourceLocation(BloodIsFuel.MOD_ID, "molten");

    public static void insertIntoPack(DynamicPack pack) {
        Set<ResourceLocation> foundMoltenTags = new HashSet<>();
        Set<ResourceLocation> allTagIds = new HashSet<>();

        // Inspect all tags
        BuiltInRegistries.FLUID.getTags().forEach(tagWithFluids -> {
            TagKey<Fluid> tag = tagWithFluids.getFirst();
            ResourceLocation tagId = tag.location();
            allTagIds.add(tagId);

            BloodIsFuel.LOGGER.debug("Found fluid tag: {}", tagId);

            if (tagId.getPath().startsWith("molten_")) {
                foundMoltenTags.add(tagId);
                BloodIsFuel.LOGGER.debug("Included in #bloodisfuel:molten: {}", tagId);
            }
        });

        // DEBUG OUTPUT
        BloodIsFuel.LOGGER.info("All fluid tags discovered ({}): {}", allTagIds.size(),
                allTagIds.stream().map(ResourceLocation::toString).collect(Collectors.joining(", "))
        );
        BloodIsFuel.LOGGER.info("Molten tags matched ({}): {}", foundMoltenTags.size(),
                foundMoltenTags.stream().map(ResourceLocation::toString).collect(Collectors.joining(", "))
        );

        if (foundMoltenTags.isEmpty()) {
            BloodIsFuel.LOGGER.warn("No molten fluid tags were found to include in #bloodisfuel:molten");
            return;
        }

        List<TagEntry> entries = foundMoltenTags.stream()
                .map(TagEntry::tag)
                .toList();

        TagFile tagFile = new TagFile(entries, false);
        pack.put(
                BLOODISFUEL_MOLTEN_TAG.withPrefix("tags/fluids/"),
                TagFile.CODEC.encodeStart(JsonOps.INSTANCE, tagFile)
                        .result()
                        .orElseThrow(() -> new IllegalStateException("Failed to encode tag file for #bloodisfuel:molten"))
        );

        BloodIsFuel.LOGGER.info("Generated #bloodisfuel:molten with {} entries: {}",
                entries.size(),
                foundMoltenTags.stream().map(ResourceLocation::toString).collect(Collectors.joining(", "))
        );
    }
}
