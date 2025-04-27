package net.electrisoma.bloodisfuel.base.data.fabric;

import com.tterrag.registrate.providers.RegistrateTagsProvider;

import net.minecraft.tags.TagKey;
import net.minecraft.data.tags.TagsProvider;


public class BTagGenImpl {

    public static <T> TagsProvider.TagAppender<T>
    tagAppender(RegistrateTagsProvider<T> prov, TagKey<T> tag) {
        return prov.addTag(tag);
    }
}
