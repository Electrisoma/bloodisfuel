package net.electrisoma.bloodisfuel.api.data;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.electrisoma.bloodisfuel.registry.BParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

/**
 * Holds RGB color data for the colorable drip particle.
 */
@SuppressWarnings("deprecation")
public record ColorableDripParticleData(float r, float g, float b) implements ParticleOptions {

    public static final Codec<ColorableDripParticleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("r").forGetter(ColorableDripParticleData::r),
            Codec.FLOAT.fieldOf("g").forGetter(ColorableDripParticleData::g),
            Codec.FLOAT.fieldOf("b").forGetter(ColorableDripParticleData::b)
    ).apply(instance, ColorableDripParticleData::new));

    public static final Deserializer<ColorableDripParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public ColorableDripParticleData fromCommand(ParticleType<ColorableDripParticleData> type, StringReader reader)
                throws CommandSyntaxException {
            float r = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float b = reader.readFloat();
            return new ColorableDripParticleData(r, g, b);
        }

        @Override
        public ColorableDripParticleData fromNetwork(ParticleType<ColorableDripParticleData> type, FriendlyByteBuf buf) {
            return new ColorableDripParticleData(buf.readFloat(), buf.readFloat(), buf.readFloat());
        }
    };

    @Override
    public ParticleType<?> getType() {
        return BParticles.COLORABLE_DRIPPING.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeFloat(r);
        buf.writeFloat(g);
        buf.writeFloat(b);
    }

    @Override
    public String writeToString() {
        return String.format("%.3f %.3f %.3f", r, g, b);
    }
}
