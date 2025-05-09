package net.electrisoma.bloodisfuel.api;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;


public class BCodecs {

    public static final Codec<MobEffectInstance> MOB_EFFECT_INSTANCE = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.MOB_EFFECT.byNameCodec().fieldOf("effect").forGetter(MobEffectInstance::getEffect),
            Codec.INT.optionalFieldOf("duration", 0).forGetter(MobEffectInstance::getDuration),
            Codec.INT.optionalFieldOf("amplifier", 0).forGetter(MobEffectInstance::getAmplifier)
    ).apply(instance, MobEffectInstance::new));
}
