package net.electrisoma.bloodisfuel.infrastructure.data;

import net.electrisoma.bloodisfuel.BloodIsFuel;

import com.simibubi.create.foundation.pack.DynamicPack;

import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.core.registries.BuiltInRegistries;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.HashMap;


public class SyringeFluidRuntimeDataGenerator {

    public static void insertIntoPack(DynamicPack dynamicPack) {
        final int moltenColor = 0xFF4500;
        final double damagePerSecond = 4.0;
        final int durationSeconds = 15;

        Map<ResourceLocation, JsonObject> generatedJsons = new HashMap<>();

        BuiltInRegistries.FLUID.getTags().forEach(pair -> {
            TagKey<Fluid> tag = pair.getFirst();
            ResourceLocation tagId = tag.location();

            if (!tagId.getNamespace().equals("forge") || !tagId.getPath().startsWith("molten_"))
                return;

            String moltenSuffix = tagId.getPath().substring("molten_".length());

            JsonObject syringeJson = createSyringeFluidJson(tagId, moltenColor, damagePerSecond, durationSeconds);

            ResourceLocation jsonId = new ResourceLocation(
                    "bloodisfuel",
                    "bloodisfuel/syringe_fluids/molten_" + moltenSuffix
            );

            generatedJsons.put(jsonId, syringeJson);
        });

        generatedJsons.forEach(dynamicPack::put);
        BloodIsFuel.LOGGER.info("Created {} syringe fluids which will be injected into the game", generatedJsons.size());
    }

    private static JsonObject createSyringeFluidJson(ResourceLocation tagId, int color, double dps, int duration) {
        JsonObject root = new JsonObject();

        JsonObject burning = new JsonObject();
        burning.addProperty("damage_per_second", dps);
        burning.addProperty("duration_seconds", duration);
        root.add("burning", burning);

        root.addProperty("color", color);

        JsonArray fluidsArray = new JsonArray();
        fluidsArray.add("#" + tagId);
        root.add("fluids", fluidsArray);

        root.addProperty("glowing", true);
        root.addProperty("opaque", true);

        return root;
    }
}
