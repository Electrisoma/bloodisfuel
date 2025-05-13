//package net.electrisoma.bloodisfuel.registry.particles;
//
//import com.mojang.brigadier.StringReader;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;
//import net.electrisoma.bloodisfuel.registry.BParticleTypes;
//import net.minecraft.client.particle.ParticleEngine;
//import net.minecraft.core.particles.ParticleOptions;
//import net.minecraft.core.particles.ParticleType;
//import net.minecraft.network.FriendlyByteBuf;
//import java.util.Locale;
//
//
//public class BloodFluidParticleData implements ParticleOptions, ICustomParticleDataWithSprite<BloodFluidParticleData> {
//
//    public static final Codec<BloodFluidParticleData> COLORABLE_DRIP_CODEC = RecordCodecBuilder.create(i -> i
//            .group(Codec.FLOAT.fieldOf("speed")
//                    .forGetter(p -> p.speed))
//            .apply(i, BloodFluidParticleData::new));
//
//    public static final ParticleOptions.Deserializer<BloodFluidParticleData> DESERIALIZER =
//            new ParticleOptions.Deserializer<>() {
//                public BloodFluidParticleData fromCommand(ParticleType<BloodFluidParticleData> particleTypeIn,
//                                                        StringReader reader) throws CommandSyntaxException {
//                    reader.expect(' ');
//                    float speed = reader.readFloat();
//                    return new BloodFluidParticleData(speed);
//                }
//
//                public BloodFluidParticleData fromNetwork(ParticleType<BloodFluidParticleData> particleTypeIn,
//                                                        FriendlyByteBuf buffer) {
//                    return new BloodFluidParticleData(buffer.readFloat());
//                }
//            };
//
//    float speed;
//
//    public BloodFluidParticleData(float speed) {
//        this.speed = speed;
//    }
//
//    public BloodFluidParticleData() {
//        this(0);
//    }
//
//    @Override
//    public ParticleType<?> getType() {
//        return BParticleTypes.BLOOD_DRIP.get();
//    }
//
//    @Override
//    public void writeToNetwork(FriendlyByteBuf buffer) {
//        buffer.writeFloat(speed);
//    }
//
//    @Override
//    public String writeToString() {
//        return String.format(Locale.ROOT, "%s %f", BParticleTypes.BLOOD_DRIP.parameter(), speed);
//    }
//
//    @Override
//    public Deserializer<BloodFluidParticleData> getDeserializer() {
//        return DESERIALIZER;
//    }
//
//    @Override
//    public Codec<BloodFluidParticleData> getCodec(ParticleType<BloodFluidParticleData> type) {
//        return COLORABLE_DRIP_CODEC;
//    }
//
//    @Override
//    public ParticleEngine.SpriteParticleRegistration<BloodFluidParticleData> getMetaFactory() {
//        return BoilingBloodDropParticle.Factory::new;
//    }
//}