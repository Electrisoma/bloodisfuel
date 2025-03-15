package net.electrisoma.bloodisfuel;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.electrisoma.bloodisfuel.registry.BCreativeTabs;
import net.electrisoma.bloodisfuel.registry.items.BItems;


public class ModSetup {
    public static void register() {
        BCreativeTabs.register();
        BItems.register();
        //BTags.register();
    }
}
