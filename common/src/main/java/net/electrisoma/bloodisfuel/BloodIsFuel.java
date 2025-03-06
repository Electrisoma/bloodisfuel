package net.electrisoma.bloodisfuel;

import com.simibubi.create.CreateBuildInfo;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.providers.ProviderType;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.lang.LangBuilder;
import net.electrisoma.bloodisfuel.infrastructure.data.BTagGen;
import net.electrisoma.bloodisfuel.infrastructure.data.lang.BLangGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BloodIsFuel {
    public static final String MOD_ID = "bloodisfuel";
    public static final String NAME = "Blood is Fuel";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    private static final CreateRegistrate REGISTRATE =
            CreateRegistrate.create(MOD_ID);

    static {
        REGISTRATE.setTooltipModifierFactory(item -> new
                ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }

    public static void init() {
        LOGGER.info("{} initializing! Create version: {} on platform: {}", NAME, CreateBuildInfo.VERSION, ExampleExpectPlatform.platformName());

        ModSetup.register();
//        finalizeRegistrate();

    }

    public static void gatherData(DataGenerator.PackGenerator gen) {
        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, BTagGen::generateBlockTags);
        //REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, BTagGen::generateItemTags);
        //REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, BTagGen::generateFluidTags);
        REGISTRATE.addDataGenerator(ProviderType.LANG, BLangGen::generate);
        //gen.addProvider(NumismaticsSequencedAssemblyRecipeGen::new);
        //gen.addProvider(NumismaticsStandardRecipeGen::new);
        //gen.addProvider(NumismaticsAdvancements::new);
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

//    @ExpectPlatform
//    public static void finalizeRegistrate() {
//        throw new AssertionError();
//    }
}
