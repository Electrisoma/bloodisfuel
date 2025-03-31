package net.electrisoma.bloodisfuel.base.data;


import net.electrisoma.bloodisfuel.registry.BTags;
import net.electrisoma.bloodisfuel.registry.BTags.AllBlockTags;
import net.electrisoma.bloodisfuel.registry.BTags.AllItemTags;
import net.electrisoma.bloodisfuel.registry.BTags.AllFluidTags;
import net.electrisoma.bloodisfuel.registry.BBlocks;

import com.tterrag.registrate.providers.RegistrateTagsProvider;

import dev.architectury.injectables.annotations.ExpectPlatform;

import net.minecraft.data.tags.TagsProvider.TagAppender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BTagGen {
    private static final Map<TagKey<Block>, List<ResourceLocation>> OPTIONAL_TAGS = new HashMap<>();

    @SafeVarargs
    public static void addOptionalTag(ResourceLocation id, TagKey<Block>... tags) {
        for (TagKey<Block> tag : tags) {
            OPTIONAL_TAGS.computeIfAbsent(tag, (e) -> new ArrayList<>()).add(id);
        }
    }

    public static void generateBlockTags(RegistrateTagsProvider<Block> prov) {

        prov.addTag(BTags.AllBlockTags.EXAMPLE.tag)
                .add(BBlocks.EXAMPLE_BLOCK.get())
        ;


        for (BTags.AllBlockTags tag : BTags.AllBlockTags.values()) {
            if (tag.alwaysDatagen) {
                tagAppender(prov, tag);
            }
        }
        for (TagKey<Block> tag : OPTIONAL_TAGS.keySet()) {
            var appender = tagAppender(prov, tag);
            for (ResourceLocation loc : OPTIONAL_TAGS.get(tag))
                appender.addOptional(loc);
        }
    }

    public static void generateItemTags(RegistrateTagsProvider<Item> prov) {

        prov.addTag(AllItemTags.EXAMPLE.tag)

        ;

        prov.addTag(AllItemTags.CARBOHYDRATES.tag)
                //Vanilla
                .add(
                        Items.SUGAR,
                        Items.COOKIE,
                        Items.CAKE,
                        Items.HONEYCOMB,
                        Items.HONEY_BLOCK,
                        Items.HONEYCOMB_BLOCK,
                        Items.HONEY_BOTTLE,
                        Items.SWEET_BERRIES,
                        Items.GLOW_BERRIES,
                        Items.COCOA_BEANS,
                        Items.APPLE
                )

                //Create
                .addOptional(new ResourceLocation("create:sweet_roll"))
                .addOptional(new ResourceLocation("create:chocolate_glazed_berries"))
                .addOptional(new ResourceLocation("create:honeyed_apple"))
                .addOptional(new ResourceLocation("create:bar_of_chocolate"))

        ;



        for (AllItemTags tag : AllItemTags.values()) {
            if (tag.alwaysDatagen)
                tagAppender(prov, tag);
        }
    }

    public static void generateFluidTags(RegistrateTagsProvider<Fluid> prov){

        prov.addTag(AllFluidTags.BLOOD.tag)

        ;

        for (AllFluidTags tag : AllFluidTags.values()) {
            if (tag.alwaysDatagen)
                tagAppender(prov, tag);
        }
    }


    public static TagAppender<Fluid> tagAppender(RegistrateTagsProvider<Fluid> prov, AllFluidTags tag) {
        return tagAppender(prov, tag.tag);
    }


    public static TagAppender<Item> tagAppender(RegistrateTagsProvider<Item> prov, AllItemTags tag) {
        return tagAppender(prov, tag.tag);
    }

    public static TagAppender<Block> tagAppender(RegistrateTagsProvider<Block> prov, AllBlockTags tag) {
        return tagAppender(prov, tag.tag);
    }

    @ExpectPlatform // this has to be platformed out because addTag on fabric has a signature that includes FabricTagProvider$FabricTagBuilder
    public static <T> TagAppender<T> tagAppender(RegistrateTagsProvider<T> prov, TagKey<T> tag) {
        throw new AssertionError();
    }
}
