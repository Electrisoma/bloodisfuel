package net.electrisoma.bloodisfuel;

import net.electrisoma.bloodisfuel.registry.BParticles;
import net.electrisoma.bloodisfuel.registry.particles.*;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;


@SuppressWarnings("unused")
public class BClient {

    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {

        modEventBus.addListener(BClient::clientInit);
        modEventBus.addListener(BClient::setupParticles);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        //PonderIndex.addPlugin(new BPonderPlugin());
    }

    public static void setupParticles(RegisterParticleProvidersEvent registry) {

        registry.registerSpriteSet(BParticles.BOILING_BLOOD_DROP.get(), BoilingBloodDropParticleData.Factory::new);
    }
}
