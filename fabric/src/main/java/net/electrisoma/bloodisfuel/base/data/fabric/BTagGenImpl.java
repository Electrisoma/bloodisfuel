package net.electrisoma.bloodisfuel.base.data.fabric;

import com.tterrag.registrate.providers.RegistrateTagsProvider;

import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;


public class BTagGenImpl {
    public static <T> TagsProvider.TagAppender<T> tagAppender(RegistrateTagsProvider<T> prov, TagKey<T> tag) {
        return prov.addTag(tag);
    }
}
