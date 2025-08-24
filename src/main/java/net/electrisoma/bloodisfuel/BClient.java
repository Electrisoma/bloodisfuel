package net.electrisoma.bloodisfuel;

import net.electrisoma.bloodisfuel.content.equipment.syringe_blade.SyringeFluidSelectScreen;
import net.electrisoma.bloodisfuel.registry.BItems;
import net.electrisoma.bloodisfuel.registry.BMenuTypes;
import net.electrisoma.bloodisfuel.registry.BParticles;
import net.electrisoma.bloodisfuel.registry.particles.*;
import net.electrisoma.bloodisfuel.content.equipment.syringe_gun.SyringeGunRenderHandler;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeItemColor;

import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings({"unused", "deprecation", "RedundantSuppression"})
public class BClient {
    public static final SyringeGunRenderHandler SYRINGE_GUN_RENDER_HANDLER = new SyringeGunRenderHandler();

    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(BClient::clientInit);
        modEventBus.addListener(BClient::setupParticles);
        modEventBus.addListener(BClient::onRegisterItemColors);
        SYRINGE_GUN_RENDER_HANDLER.registerListeners(forgeEventBus);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(BMenuTypes.SYRINGE_FLUID_SELECT.get(), SyringeFluidSelectScreen::new));
    }


    public static void setupParticles(RegisterParticleProvidersEvent registry) {
        registry.registerSpriteSet(BParticles.BLOOD_DROP.get(), BloodDropParticle.Factory::new);
        registry.registerSpriteSet(BParticles.BOILING_BLOOD_DROP.get(), BoilingBloodDropParticle.Factory::new);
        registry.registerSpriteSet(BParticles.COLORABLE_DRIPPING.get(), ColorableDripParticle.Factory::new);
    }

    public static void onRegisterItemColors(FMLClientSetupEvent event) {
        Minecraft.getInstance().getItemColors().register(
                new SyringeItemColor(),
                BItems.SYRINGE_BLADE.get(),
                BItems.SYRINGE_GUN.get()
        );
    }
}