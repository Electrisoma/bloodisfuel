package net.electrisoma.bloodisfuel.foundation.events;

import net.electrisoma.bloodisfuel.BClient;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static net.createmod.ponder.PonderClient.isGameActive;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (!isGameActive()) return;
        BClient.SYRINGE_GUN_RENDER_HANDLER.tick();
    }
}