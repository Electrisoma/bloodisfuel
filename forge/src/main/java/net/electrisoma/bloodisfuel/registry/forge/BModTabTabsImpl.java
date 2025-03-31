package net.electrisoma.bloodisfuel.registry.forge;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BModTab;

import static net.electrisoma.bloodisfuel.registry.forge.BModTabRegistrateDisplayItemsGeneratorImpl.getTabObject;


public class BModTabTabsImpl {
    public static void use(BModTab.Tabs tab) {
        BloodIsFuel.registrate().setCreativeTab(getTabObject(tab.getKey()));
    }
}
