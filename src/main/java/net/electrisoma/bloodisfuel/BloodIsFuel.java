package net.electrisoma.bloodisfuel;

import net.electrisoma.bloodisfuel.registry.*;
import net.electrisoma.bloodisfuel.infrastructure.data.BDatagen;
import net.electrisoma.bloodisfuel.foundation.data.advancements.triggers.BTriggers;

import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.lang.LangBuilder;

import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.data.CreateRegistrate;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.server.ServerStartedEvent;

import com.mojang.logging.LogUtils;

import org.slf4j.Logger;


@SuppressWarnings("unused")
@Mod(BloodIsFuel.MOD_ID)
public class BloodIsFuel {
    public static final String NAME = "Create: Blood is Fuel!";
    public static final String MOD_ID = "bloodisfuel";
    public static final String SERVER_START = "HELL IS FULL";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null)
            .setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
            );

    public BloodIsFuel() {
        onCtor();
    }

    public static void onCtor() {

        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        REGISTRATE.registerEventListeners(modEventBus);

        ModSetup.register();

        modEventBus.addListener(BloodIsFuel::init);
        modEventBus.addListener(EventPriority.LOWEST, BDatagen::gatherData);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                BClient.onCtorClient(modEventBus, forgeEventBus)
        );
    }

    public static void init(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BAdvancements.register();
            BTriggers.register();
        });
    }

    public static LangBuilder lang() {
        return new LangBuilder(MOD_ID);
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartedEvent event) {
        LOGGER.info(BloodIsFuel.SERVER_START);
    }
}