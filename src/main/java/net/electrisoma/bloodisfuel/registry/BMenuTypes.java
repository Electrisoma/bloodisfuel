package net.electrisoma.bloodisfuel.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.content.equipment.syringe_blade.SyringeFluidSelectMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, BloodIsFuel.MOD_ID);

    public static final RegistryObject<MenuType<SyringeFluidSelectMenu>> SYRINGE_FLUID_SELECT =
            MENUS.register("syringe_fluid_select", () ->
                    IForgeMenuType.create(SyringeFluidSelectMenu::new));

    public static void register() {
        MENUS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
