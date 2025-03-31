package net.electrisoma.bloodisfuel;

import net.electrisoma.bloodisfuel.base.data.lang.BLangGen;
import net.electrisoma.bloodisfuel.base.data.BTagGen;
import net.electrisoma.bloodisfuel.multiloader.Loader;
import net.electrisoma.bloodisfuel.registry.BModTab.Tabs;

import com.simibubi.create.CreateBuildInfo;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;

import net.createmod.catnip.lang.FontHelper.Palette;

import com.tterrag.registrate.providers.ProviderType;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

import dev.architectury.injectables.annotations.ExpectPlatform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BloodIsFuel {
    public static final String MOD_ID = "bloodisfuel";
    public static final String NAME = "Blood is Fuel";
    public static final String VERSION = findVersion();
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    private static final CreateRegistrate REGISTRATE =
            CreateRegistrate.create(MOD_ID);

    static {
        REGISTRATE.setTooltipModifierFactory(item -> new
                ItemDescription.Modifier(item, Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
        Tabs.MAIN.use();
    }

    public static void init() {
        LOGGER.info("{} {} initializing! Create version: {} on platform: {}", NAME, VERSION, CreateBuildInfo.VERSION, Loader.getCurrent());

        ModSetup.register();
        finalizeRegistrate();
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    @ExpectPlatform
    public static void finalizeRegistrate() {
        throw new AssertionError();
    }

    public static void gatherData(DataGenerator.PackGenerator gen) {
        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, BTagGen::generateBlockTags);
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, BTagGen::generateItemTags);
        REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, BTagGen::generateFluidTags);

        REGISTRATE.addDataGenerator(ProviderType.LANG, BLangGen::generate);
        //gen.addProvider(NumismaticsSequencedAssemblyRecipeGen::new);

        //gen.addProvider(BStandardRecipeGen::new);
        //gen.addProvider(NumismaticsAdvancements::new);
    }

    @ExpectPlatform
    public static String findVersion() {
        throw new AssertionError();
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
