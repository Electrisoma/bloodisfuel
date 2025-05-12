package net.electrisoma.bloodisfuel;

import net.electrisoma.bloodisfuel.registry.BEntityTypes;
import net.electrisoma.bloodisfuel.registry.BParticles;
import net.electrisoma.bloodisfuel.registry.items.syringe_gun.SyringeGunRenderHandler;
import net.electrisoma.bloodisfuel.registry.items.syringe_gun.SyringeProjectileRenderer;
import net.electrisoma.bloodisfuel.registry.particles.*;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


@SuppressWarnings("unused")
public class BClient {
    public static final SyringeGunRenderHandler SYRINGE_GUN_RENDER_HANDLER = new SyringeGunRenderHandler();

    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(BClient::clientInit);
        modEventBus.addListener(BClient::setupParticles);

        SYRINGE_GUN_RENDER_HANDLER.registerListeners(forgeEventBus);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        //PonderIndex.addPlugin(new BPonderPlugin());
    }

    public static void setupParticles(RegisterParticleProvidersEvent registry) {
        registry.registerSpriteSet(BParticles.BLOOD_DROP.get(), BloodDropParticle.Factory::new);
        registry.registerSpriteSet(BParticles.BOILING_BLOOD_DROP.get(), BoilingBloodDropParticle.Factory::new);
    }
}
