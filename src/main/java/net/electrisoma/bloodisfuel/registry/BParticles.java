package net.electrisoma.bloodisfuel.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.api.data.BCodecs;
import net.electrisoma.bloodisfuel.api.data.ColorableDripParticleData;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import com.mojang.serialization.Codec;


public class BParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, BloodIsFuel.MOD_ID);

    public static void register(IEventBus modEventBus) {
        PARTICLES.register(modEventBus);
        BloodIsFuel.LOGGER.info("Registering particles for " + BloodIsFuel.NAME);
    }

    public static final RegistryObject<SimpleParticleType> BOILING_BLOOD_DROP =
            PARTICLES.register("boiling_blood_drop", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BLOOD_DROP =
            PARTICLES.register("blood_drop", () -> new SimpleParticleType(false));
    public static final RegistryObject<ParticleType<ColorableDripParticleData>> COLORABLE_DRIPPING =
            PARTICLES.register("colorable_dripping", () -> new ParticleType<>(false,
                    ColorableDripParticleData.DESERIALIZER) {
                @Override
                public Codec<ColorableDripParticleData> codec() {
                    return BCodecs.COLORABLE_DRIP_CODEC;
                }
            });
}