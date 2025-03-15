package net.electrisoma.bloodisfuel;

import com.mojang.brigadier.CommandDispatcher;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.providers.ProviderType;
import dev.architectury.injectables.annotations.ExpectPlatform;
//import net.electrisoma.bloodisfuel.infrastructure.data.lang.BLangGen;
import net.electrisoma.bloodisfuel.infrastructure.utils.Utils;
import net.electrisoma.bloodisfuel.config.BConfigs;
import net.electrisoma.bloodisfuel.registry.BCreativeTabs.Tabs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.Function;


@SuppressWarnings("unused")
public class BloodIsFuel {
    public static final String MOD_ID = "bloodisfuel";
    public static final String NAME = "Blood is Fuel!";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    static {
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
        Tabs.MAIN.use();
    }

    public static void init() {
        ModSetup.register();
        finalizeRegistrate();
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

//    public static void postRegistrationInit() {
//        ModSetupLate.registerPostRegistration();
//    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    @ExpectPlatform
    public static void finalizeRegistrate() {
        throw new AssertionError();
    }

//    public static void gatherData(DataGenerator.PackGenerator gen) {
//        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, BTagGen::generateBlockTags);
//        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, BTagGen::generateItemTags);
//        REGISTRATE.addDataGenerator(ProviderType.LANG, BLangGen::generate);
//        gen.addProvider(BSequencedAssemblyRecipeGen::new);
//        gen.addProvider(BStandardRecipeGen::new);
//        gen.addProvider(BAdvancements::new);
//        gen.addProvider(EmiExcludedTagGen::new);
//    }

//    @ExpectPlatform
//    public static void registerCommands(BiConsumer<CommandDispatcher<CommandSourceStack>, Boolean> consumer) {
//        throw new AssertionError();
//    }
}
