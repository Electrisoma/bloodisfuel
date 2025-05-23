package net.electrisoma.bloodisfuel.infrastructure.data;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidType;
import net.electrisoma.bloodisfuel.registry.BAdvancements;
import net.electrisoma.bloodisfuel.foundation.data.recipes.*;

import com.simibubi.create.foundation.data.CreateRegistrate;

import com.tterrag.registrate.providers.ProviderType;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.core.HolderLookup;

import net.minecraftforge.data.event.GatherDataEvent;

import java.util.function.BiConsumer;
import java.util.concurrent.CompletableFuture;


public class BDatagen {
	private static final CreateRegistrate REGISTRATE = BloodIsFuel.registrate();

	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		BEntriesProvider bEntriesProvider = new BEntriesProvider(output, lookupProvider);
		addExtraRegistrateData();
		if (event.includeServer()) {
			BProcessingRecipeGen.registerAll(generator, output);
			generator.addProvider(true, bEntriesProvider);
			generator.addProvider(true, new BAdvancements(output));
			generator.addProvider(true, new StandardRecipeGen(output));
			generator.addProvider(true, new BSequencedRecipeGen(output));
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