package net.electrisoma.bloodisfuel.multiloader;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.base.data.BTagGen;

import com.tterrag.registrate.providers.RegistrateTagsProvider;

import net.minecraft.core.Registry;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.function.Consumer;


public class CommonTag<T> {
    public final TagKey<T> tag, fabric, forge;

    public CommonTag(TagKey<T> common, TagKey<T> fabric, TagKey<T> forge) {
        this.tag = common;
        this.fabric = fabric;
        this.forge = forge;
    }

    public CommonTag(ResourceKey<? extends Registry<T>> registry, ResourceLocation common, ResourceLocation fabric, ResourceLocation forge) {
        this(TagKey.create(registry, common), TagKey.create(registry, fabric), TagKey.create(registry, forge));
    }

    public static <T> CommonTag<T> conventional(ResourceKey<? extends Registry<T>> registry, String common, String fabric, String forge) {
        return new CommonTag<>(
                registry,
                BloodIsFuel.asResource("internal/" + common),
                new ResourceLocation("c", fabric),
                new ResourceLocation("forge", forge)
        );
    }

    public static <T> CommonTag<T> conventional(ResourceKey<? extends Registry<T>> registry, String path) {
        return conventional(registry, path, path, path);
    }

    public CommonTag<T> generateBoth(RegistrateTagsProvider<T> tags, Consumer<TagsProvider.TagAppender<T>> consumer) {
        consumer.accept(BTagGen.tagAppender(tags, fabric));
        consumer.accept(BTagGen.tagAppender(tags, forge));
        return this;
    }

    public CommonTag<T> generateCommon(RegistrateTagsProvider<T> tags) {
        BTagGen.tagAppender(tags, tag)
                .addOptionalTag(fabric.location())
                .addOptionalTag(forge.location());
        return this;
    }
}