package net.electrisoma.bloodisfuel.forge.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.electrisoma.bloodisfuel.registry.BCreativeTabs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.RegistryObject;

public class BCreativeTabsRegistrateDisplayGenImpl {

    public static RegistryObject<CreativeModeTab> getTabObject(ResourceKey<CreativeModeTab> tab) {
        RegistryObject<CreativeModeTab> tabObject;
        if (tab == BCreativeTabs.getBaseTabKey()) {
            tabObject = BCreativeTabsImpl.MAIN_TAB;
        } else {
            tabObject = BCreativeTabsImpl.MAIN_TAB;
        }
        return tabObject;
    }

    public static boolean isInCreativeTab(RegistryEntry<?> entry, ResourceKey<CreativeModeTab> tab) {
        return CreateRegistrate.isInCreativeTab(entry, getTabObject(tab));
    }
}
