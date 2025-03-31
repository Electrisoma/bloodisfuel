package net.electrisoma.bloodisfuel.registry.forge;


import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BBlocks;
import net.electrisoma.bloodisfuel.registry.BModTab.Tabs;
import net.electrisoma.bloodisfuel.registry.BModTab.RegistrateDisplayItemsGenerator;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;


@Mod.EventBusSubscriber(bus = Bus.MOD)
public class BModTabImpl {
    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BloodIsFuel.MOD_ID);

    @SuppressWarnings("Convert2MethodRef")
    public static final RegistryObject<CreativeModeTab> MAIN_TAB =
            TAB_REGISTER.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.bloodisfuel.main"))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(() -> BBlocks.EXAMPLE_BLOCK.asStack())
                    .displayItems(new RegistrateDisplayItemsGenerator(Tabs.MAIN))
                    .build());

    public static void register(IEventBus modEventBus) {
        TAB_REGISTER.register(modEventBus);
    }

    public static CreativeModeTab getBaseTab() {
        return MAIN_TAB.get();
    }

    public static ResourceKey<CreativeModeTab> getBaseTabKey() {
        return MAIN_TAB.getKey();
    }
}