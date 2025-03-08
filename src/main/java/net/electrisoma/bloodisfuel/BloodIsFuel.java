package net.electrisoma.bloodisfuel;

import net.electrisoma.bloodisfuel.config.BloodConfigs;
import net.electrisoma.bloodisfuel.infrastructure.data.BloodDatagen;
import net.electrisoma.bloodisfuel.registry.*;
import net.electrisoma.bloodisfuel.registry.blocks.BIF_Blocks;
import net.electrisoma.bloodisfuel.registry.fluids.BIF_Fluids;
import net.electrisoma.bloodisfuel.registry.items.BIF_Items;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.mojang.logging.LogUtils;

import org.slf4j.Logger;


@SuppressWarnings({"removal","all"})
@Mod(BloodIsFuel.MOD_ID)
public class BloodIsFuel
{
    public static final String NAME = "Create: Blood is Fuel!";
    public static final String MOD_ID = "bloodisfuel";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    static {
        REGISTRATE.setTooltipModifierFactory(item -> {
            return new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item)));
        });
    }

    public BloodIsFuel()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        REGISTRATE.registerEventListeners(modEventBus);

        BIF_Tags.init();
        CreativeTabs.register(modEventBus);
        BIF_Blocks.register();
        BIF_Items.register();
        BIF_Fluids.register();

        BloodConfigs.register(modLoadingContext);

        modEventBus.addListener(BloodIsFuel::init);
        modEventBus.addListener(EventPriority.LOWEST, BloodDatagen::gatherData);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> BloodClient::new);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void init(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("yippie! :3");
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}