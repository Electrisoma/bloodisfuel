package net.electrisoma.bloodisfuel.infrastructure.data;

import net.electrisoma.bloodisfuel.BloodIsFuel;

import com.simibubi.create.foundation.pack.DynamicPack;

import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagEntry;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

import com.mojang.serialization.JsonOps;

import java.util.Set;
import java.util.List;
import java.util.HashSet;


public class SyringeFluidRuntimeDataGenerator {
    private static final ResourceLocation MOLTEN_TAG_ID =
            new ResourceLocation(BloodIsFuel.MOD_ID, "molten");
    private static int moltenTagsAdded = 0;

    public static void insertIntoPack(DynamicPack pack) {
        addMoltenFluidTag(pack);

        BloodIsFuel.LOGGER.info("Added {} to #bloodisfuel:molten", moltenTagsAdded);
    }

    private static void addMoltenFluidTag(DynamicPack pack) {
        Set<ResourceLocation> moltenTags = new HashSet<>();

        //noinspection deprecation
        BuiltInRegistries.FLUID.getTags().forEach(tagWithFluids -> {
            TagKey<Fluid> tag = tagWithFluids.getFirst();
            ResourceLocation tagId = tag.location();

            if (tagId.getPath().startsWith("molten_")) moltenTags.add(tagId);
        });

        if (moltenTags.isEmpty()) return;

        List<TagEntry> entries = moltenTags.stream().map(TagEntry::tag).toList();

        TagFile tagFile = new TagFile(entries, false);
        pack.put(
                MOLTEN_TAG_ID.withPrefix("tags/fluids/"),
                TagFile.CODEC.encodeStart(JsonOps.INSTANCE, tagFile)
                        .result()
                        .orElseThrow(() -> new IllegalStateException("Failed to encode tag file for #bloodisfuel:molten"))
        );

        moltenTagsAdded = entries.size();
    }
}

