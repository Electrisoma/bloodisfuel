package net.electrisoma.bloodisfuel.infrastructure.data;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.infrastructure.LangPartial;
import net.electrisoma.bloodisfuel.infrastructure.data.advancements.AllAdvancements;
import net.electrisoma.bloodisfuel.infrastructure.data.recipes.BloodProcessingRecipeGen;
import net.electrisoma.bloodisfuel.infrastructure.data.recipes.StandardRecipeGen;
import net.electrisoma.bloodisfuel.infrastructure.data.tags.BloodRegistrateTags;

import com.tterrag.registrate.providers.ProviderType;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.function.BiConsumer;


public class BloodDatagen {
	public static void gatherData(GatherDataEvent event) {
		addExtraRegistrateData();

		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();

		if (event.includeServer()) {

			generator.addProvider(true, new AllAdvancements(output));
			generator.addProvider(true, new StandardRecipeGen(output));
			BloodProcessingRecipeGen.registerAll(generator, output);
		}
	}

	private static void addExtraRegistrateData() {
		BloodRegistrateTags.addGenerators();

		BloodIsFuel.REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
			BiConsumer<String, String> langConsumer = provider::add;

			AllAdvancements.provideLang(langConsumer);
			providePartialLang(langConsumer);
		});

	}
	private static void providePartialLang(BiConsumer<String, String> consumer) {
		LangPartial.provideLang(consumer);
	}
}