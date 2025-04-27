package net.electrisoma.bloodisfuel.base.data.lang;

import net.electrisoma.bloodisfuel.registry.BAdvancements;

import com.tterrag.registrate.providers.RegistrateLangProvider;

import java.util.function.BiConsumer;


public class BLangGen {
    public static void generate(RegistrateLangProvider provider) {
        BiConsumer<String, String> langConsumer = provider::add;

        provideDefaultLang(langConsumer);

        BAdvancements.provideLang(langConsumer);
    }

    private static void provideDefaultLang(BiConsumer<String, String> consumer) {
        defaultLangGen.provideLang(consumer);
    }
}
