package net.electrisoma.bloodisfuel.foundation.events;

import net.electrisoma.bloodisfuel.BClient;
import net.electrisoma.bloodisfuel.api.equipment.SyringeItemColor;
import net.electrisoma.bloodisfuel.registry.BItems;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.createmod.ponder.PonderClient.isGameActive;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (!isGameActive()) return;

        BClient.SYRINGE_GUN_RENDER_HANDLER.tick();
    }
}