package net.electrisoma.bloodisfuel;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


@SuppressWarnings("unused")
public class BClient {
    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(BClient::clientInit);


    }

    public static void clientInit(final FMLClientSetupEvent event) {


        //PonderIndex.addPlugin(new BPonderPlugin());
    }

}
