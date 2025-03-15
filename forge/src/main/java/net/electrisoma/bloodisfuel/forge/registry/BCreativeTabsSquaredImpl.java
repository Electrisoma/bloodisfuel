package net.electrisoma.bloodisfuel.forge.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BCreativeTabs;

import static net.electrisoma.bloodisfuel.forge.registry.BCreativeTabsRegistrateDisplayGenImpl.getTabObject;

public class BCreativeTabsSquaredImpl {
    public static void use(BCreativeTabs.Tabs tab) {
        BloodIsFuel.registrate().setCreativeTab(getTabObject(tab.getKey()));
    }
}
