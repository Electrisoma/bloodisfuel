package net.electrisoma.bloodisfuel.foundation.events;

import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.electrisoma.bloodisfuel.BClient;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import java.util.function.Supplier;

import static net.createmod.ponder.PonderClient.isGameActive;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (!isGameActive()) return;
        BClient.SYRINGE_GUN_RENDER_HANDLER.tick();
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {

        @SubscribeEvent
        public static void onLoadComplete(FMLLoadCompleteEvent event) {
            ModContainer createContainer = ModList.get()
                    .getModContainerById(BloodIsFuel.MOD_ID)
                    .orElseThrow(() -> new IllegalStateException("Blood is Fuel mod container missing on LoadComplete"));
            createContainer.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory(
                            (mc, previousScreen) -> new BaseConfigScreen(previousScreen, BloodIsFuel.MOD_ID)));
        }
    }

}