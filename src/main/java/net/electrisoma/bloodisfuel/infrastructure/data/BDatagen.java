package net.electrisoma.bloodisfuel.infrastructure.data;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.foundation.data.advancements.AllAdvancements;
import net.electrisoma.bloodisfuel.foundation.data.recipes.BProcessingRecipeGen;
import net.electrisoma.bloodisfuel.foundation.data.recipes.StandardRecipeGen;

import com.tterrag.registrate.providers.ProviderType;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.function.BiConsumer;


public class BDatagen {
	private static final CreateRegistrate REGISTRATE = BloodIsFuel.registrate();

	public static void gatherData(GatherDataEvent event) {
		addExtraRegistrateData();

		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();

		if (event.includeServer()) {

			generator.addProvider(true, new AllAdvancements(output));
			generator.addProvider(true, new StandardRecipeGen(output));
			BProcessingRecipeGen.registerAll(generator, output);
		}
	}

	private static void addExtraRegistrateData() {
		BRegistrateTags.addGenerators();

		REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
			BiConsumer<String, String> langConsumer = provider::add;

			AllAdvancements.provideLang(langConsumer);
			provideDefault(langConsumer);
		});

	}
	private static void provideDefault(BiConsumer<String, String> consumer) {
		defaultLang.provideLang(consumer);
	}
}