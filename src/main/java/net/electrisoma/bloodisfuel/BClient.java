package net.electrisoma.bloodisfuel;

import net.electrisoma.bloodisfuel.api.equipment.SyringeItemColor;
import net.electrisoma.bloodisfuel.registry.BItems;
import net.electrisoma.bloodisfuel.registry.BParticles;
import net.electrisoma.bloodisfuel.registry.items.syringe_gun.SyringeGunRenderHandler;
import net.electrisoma.bloodisfuel.registry.particles.*;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


@SuppressWarnings({"unused", "deprecation"})
public class BClient {
    public static final SyringeGunRenderHandler SYRINGE_GUN_RENDER_HANDLER = new SyringeGunRenderHandler();

    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(BClient::clientInit);
        modEventBus.addListener(BClient::setupParticles);
        modEventBus.addListener(BClient::onRegisterItemColors);

        SYRINGE_GUN_RENDER_HANDLER.registerListeners(forgeEventBus);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        //PonderIndex.addPlugin(new BPonderPlugin());
    }

    public static void setupParticles(RegisterParticleProvidersEvent registry) {
        registry.registerSpriteSet(BParticles.BLOOD_DROP.get(), BloodDropParticle.Factory::new);
        registry.registerSpriteSet(BParticles.BOILING_BLOOD_DROP.get(), BoilingBloodDropParticle.Factory::new);
        registry.registerSpriteSet(BParticles.COLORABLE_DRIPPING.get(), ColorableDripParticle.Factory::new);
    }

    public static void onRegisterItemColors(FMLClientSetupEvent event) {
        // Register the color handler
        Minecraft.getInstance().getItemColors().register(
                new SyringeItemColor(),
                BItems.SYRINGE_BLADE.get(),
                BItems.SYRINGE_GUN.get()
        );
    }
}
