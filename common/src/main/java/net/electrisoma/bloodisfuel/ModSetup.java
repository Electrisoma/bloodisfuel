package net.electrisoma.bloodisfuel;

import net.electrisoma.bloodisfuel.registry.*;
import net.electrisoma.bloodisfuel.registry.items.BItems;


public class ModSetup {
    public static void register() {
        BModTab.register();
        BTags.register();
        BBlocks.register();
        //BItems.register();
        BFluids.register();
    }
}
