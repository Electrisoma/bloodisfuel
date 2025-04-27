package net.electrisoma.bloodisfuel.base.data.forge;

import com.tterrag.registrate.providers.RegistrateTagsProvider;

import net.minecraft.tags.TagKey;
import net.minecraft.data.tags.TagsProvider.TagAppender;


public class BTagGenImpl {

    public static <T> TagAppender<T>
    tagAppender(RegistrateTagsProvider<T> prov, TagKey<T> tag) {
        return prov.addTag(tag);
    }
}
