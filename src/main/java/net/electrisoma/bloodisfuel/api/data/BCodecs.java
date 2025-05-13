package net.electrisoma.bloodisfuel.api.data;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;


@SuppressWarnings("all")
public class BCodecs {

    /**
     * Codec for serializing MobEffectInstance objects with optional duration and amplifier fields.
     */
    public static final Codec<MobEffectInstance> MOB_EFFECT_INSTANCE = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.MOB_EFFECT.byNameCodec().fieldOf("effect").forGetter(MobEffectInstance::getEffect),
            Codec.INT.optionalFieldOf("duration", 1).forGetter(MobEffectInstance::getDuration),
            Codec.INT.optionalFieldOf("amplifier", 0).forGetter(MobEffectInstance::getAmplifier)
    ).apply(instance, MobEffectInstance::new));

    /**
     * Codec for serializing BurningData objects with optional duration and damage fields.
     */
    public static final Codec<BurningData> BURNING_DATA = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("duration_seconds", 4).forGetter(BurningData::durationSeconds),
            Codec.FLOAT.optionalFieldOf("damage_per_second", 1.0f).forGetter(BurningData::damagePerSecond)
    ).apply(instance, BurningData::new));

    /**
     * Codec for serializing ExtinguishingData objects with optional duration and health fields.
     */
    public static final Codec<ExtinguishingData> EXTINGUISHING_DATA = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("duration_seconds", 4).forGetter(ExtinguishingData::durationSeconds),
            Codec.FLOAT.optionalFieldOf("heal_per_second", 1.0f).forGetter(ExtinguishingData::healPerSecond)
    ).apply(instance, ExtinguishingData::new));

    /**
     * Codec for serializing FoodProperties objects with optional nutrition, saturation and health fields.
     */
    public static final Codec<FoodProperties> FOOD_PROPERTIES = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("nutrition").forGetter(FoodProperties::getNutrition),
            Codec.FLOAT.fieldOf("saturation").forGetter(FoodProperties::getSaturationModifier)
    ).apply(instance, (nutrition, saturation) -> new FoodProperties.Builder()
            .nutrition(nutrition).saturationMod(saturation)
            .build()
    ));
}
