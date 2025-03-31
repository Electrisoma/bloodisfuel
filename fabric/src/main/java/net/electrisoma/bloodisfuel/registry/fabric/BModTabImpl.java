package net.electrisoma.bloodisfuel.registry.fabric;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BBlocks;
import net.electrisoma.bloodisfuel.registry.BModTab.Tabs;
import net.electrisoma.bloodisfuel.registry.BModTab.TabInfo;
import net.electrisoma.bloodisfuel.registry.BModTab.RegistrateDisplayItemsGenerator;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Supplier;


public class BModTabImpl {

    @SuppressWarnings("Convert2MethodRef")
    private static final TabInfo MAIN_TAB = register("main",
            () -> FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup.bloodisfuel.main"))
                    .icon(() -> BBlocks.EXAMPLE_BLOCK.asStack())
                    .displayItems(new RegistrateDisplayItemsGenerator(Tabs.MAIN))
                    .build());

    public static CreativeModeTab getBaseTab() {
        return MAIN_TAB.tab();
    }

    public static ResourceKey<CreativeModeTab> getBaseTabKey() {
        return MAIN_TAB.key();
    }

    private static TabInfo register(String name, Supplier<CreativeModeTab> supplier) {
        ResourceLocation id = BloodIsFuel.asResource(name);
        ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
        CreativeModeTab tab = supplier.get();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
        return new TabInfo(key, tab);
    }
}
