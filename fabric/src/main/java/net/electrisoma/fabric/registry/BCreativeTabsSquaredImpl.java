package net.electrisoma.fabric.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BCreativeTabs;



public class BCreativeTabsSquaredImpl {
    public static void use(BCreativeTabs.Tabs tab) {
        BloodIsFuel.registrate().setCreativeTab(tab.getKey());
    }
}
