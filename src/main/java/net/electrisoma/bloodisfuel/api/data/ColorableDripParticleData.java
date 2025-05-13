package net.electrisoma.bloodisfuel.api.data;

import net.electrisoma.bloodisfuel.registry.BParticles;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * Holds RGB color data for the colorable drip particle.
 */
@SuppressWarnings({"deprecation", "RedundantSuppression"})
public record ColorableDripParticleData(float r, float g, float b) implements ParticleOptions {

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
