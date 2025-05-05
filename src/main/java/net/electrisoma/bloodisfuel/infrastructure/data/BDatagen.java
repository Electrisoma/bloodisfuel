package net.electrisoma.bloodisfuel.infrastructure.data;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.foundation.data.recipes.StandardRecipeGen;
import net.electrisoma.bloodisfuel.foundation.data.recipes.BProcessingRecipeGen;
import net.electrisoma.bloodisfuel.registry.BAdvancements;

import com.simibubi.create.foundation.data.CreateRegistrate;

import com.tterrag.registrate.providers.ProviderType;

import net.minecraft.data.PackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.BiConsumer;
import java.util.concurrent.CompletableFuture;


public class BDatagen {
	private static final CreateRegistrate REGISTRATE = BloodIsFuel.registrate();

	public static void gatherData(GatherDataEvent event) {
		addExtraRegistrateData();

		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		BEntriesProvider bEntriesProvider = new BEntriesProvider(output, lookupProvider);
		lookupProvider = bEntriesProvider.getRegistryProvider();
		generator.addProvider(event.includeServer(), bEntriesProvider);


		if (event.includeServer()) {
			generator.addProvider(true, new BAdvancements(output));
			generator.addProvider(true, new StandardRecipeGen(output));
			BProcessingRecipeGen.registerAll(generator, output);
		}
	}

	private static void addExtraRegistrateData() {
		BRegistrateTags.addGenerators();

		REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
			BiConsumer<String, String> langConsumer = provider::add;

			BAdvancements.provideLang(langConsumer);
			provideDefault(langConsumer);
		});

	}
	private static void provideDefault(BiConsumer<String, String> consumer) {
		defaultLang.provideLang(consumer);
	}
}