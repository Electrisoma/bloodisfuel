package net.electrisoma.bloodisfuel.base.data;

import net.electrisoma.bloodisfuel.registry.BTags;
import net.electrisoma.bloodisfuel.registry.BTags.*;
import net.electrisoma.bloodisfuel.registry.BFluids;
import net.electrisoma.bloodisfuel.registry.items.BItems;

import com.tterrag.registrate.providers.RegistrateTagsProvider;

import dev.architectury.injectables.annotations.ExpectPlatform;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.data.tags.TagsProvider.TagAppender;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;


@SuppressWarnings("all")
public class BTagGen {

    public static void generateBlockTags(RegistrateTagsProvider<Block> prov) {


        /**
         * end of block tags
         */

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

        prov.addTag(AllItemTags.CARBOHYDRATES.tag)

                //Vanilla
                .add(
                        Items.CAKE,
                        Items.SUGAR,
                        Items.APPLE,
                        Items.COOKIE,
                        Items.HONEYCOMB,
                        Items.COCOA_BEANS,
                        Items.HONEY_BLOCK,
                        Items.GLOW_BERRIES,
                        Items.HONEY_BOTTLE,
                        Items.SWEET_BERRIES,
                        Items.HONEYCOMB_BLOCK
                )

                //Create
                .addOptional(new ResourceLocation("create:sweet_roll"))
                .addOptional(new ResourceLocation("create:honeyed_apple"))
                .addOptional(new ResourceLocation("create:bar_of_chocolate"))
                .addOptional(new ResourceLocation("create:chocolate_glazed_berries"))

                //Biomancy
                .addOptional(new ResourceLocation("biomancy:nutrient_bar"))
                .addOptional(new ResourceLocation("biomancy:nutrient_paste"))

        ;

        prov.addTag(BTags.AllItemTags.MEATS.tag)

                //Vanilla
                .add(
                        Items.BEEF,
                        Items.MUTTON,
                        Items.RABBIT,
                        Items.CHICKEN,
                        Items.PORKCHOP,
                        Items.SPIDER_EYE,
                        Items.ROTTEN_FLESH,
                        Items.FERMENTED_SPIDER_EYE
                )

                //BIF
                .add(BItems.DRAINED_MEAT.getId())

                //Farmer's Delight
                .addOptional(new ResourceLocation("farmersdelight:ham"))
                .addOptional(new ResourceLocation("farmersdelight:bacon"))
                .addOptional(new ResourceLocation("farmersdelight:minced_beef"))
                .addOptional(new ResourceLocation("farmersdelight:chicken_cuts"))
                .addOptional(new ResourceLocation("farmersdelight:mutton_chops"))
        ;

        prov.addTag(BTags.AllItemTags.FISHES.tag)

                //Vanilla
                .add(
                        Items.COD,
                        Items.SALMON,
                        Items.PUFFERFISH,
                        Items.TROPICAL_FISH
                )

                //Farmer's Delight
                .addOptional(new ResourceLocation("farmersdelight:cod_slice"))
                .addOptional(new ResourceLocation("farmersdelight:salmon_slice"))
        ;

        //Biomancy meat
        prov.addTag(BTags.AllItemTags.RAW_MEATS.tag)
                .add(BItems.DRAINED_MEAT.getId())
        ;


        /**
         * end of item tags
         */

        for (AllItemTags tag : AllItemTags.values()) {
            if (tag.alwaysDatagen)
                tagAppender(prov, tag);
        }
    }

    public static void generateFluidTags(RegistrateTagsProvider<Fluid> prov){

        prov.addTag(BTags.AllFluidTags.FUEL.tag)

                //BIF
                .add(BFluids.VISCERA.getId())
                .add(BFluids.BLOOD.getId())
                .add(BFluids.ENRICHED_BLOOD.getId())
                .add(BFluids.OIL_ENRICHED_BLOOD.getId())
                .add(BFluids.DIESEL_INFUSED_BLOOD.getId())
                .add(BFluids.GASOLINE_INFUSED_BLOOD.getId())
        ;

        prov.addTag(BTags.AllFluidTags.LIQUID_CARBOHYDRATES.tag)

                //Create
                .addOptional(new ResourceLocation("create:tea"))
                .addOptional(new ResourceLocation("create:honey"))
                .addOptional(new ResourceLocation("create:chocolate"))

                //Garnished
                .addOptional(new ResourceLocation("garnished:garnish"))
                .addOptional(new ResourceLocation("garnished:sweet_tea"))
                .addOptional(new ResourceLocation("garnished:apple_cider"))

                //Confectionery
                .addOptional(new ResourceLocation("create_confectionery:caramel"))
                .addOptional(new ResourceLocation("create_confectionery:hot_chocolate"))
                .addOptional(new ResourceLocation("create_confectionery:ruby_chocolate"))
                .addOptional(new ResourceLocation("create_confectionery:black_chocolate"))
                .addOptional(new ResourceLocation("create_confectionery:white_chocolate"))
                .addOptional(new ResourceLocation("create_confectionery:soothing_hot_chocolate"))

                //Biofactory
                .addOptional(new ResourceLocation("biofactory:nutrients_fluid"))
        ;


        /**
         * end of fluid tags
         */

        for (AllFluidTags tag : AllFluidTags.values()) {
            if (tag.alwaysDatagen)
                tagAppender(prov, tag);
        }
    }

    /**
     * end of all tags
     */

    private static final Map<TagKey<Block>, List<ResourceLocation>> OPTIONAL_TAGS = new HashMap<>();

    @SafeVarargs
    public static void addOptionalTag(ResourceLocation id, TagKey<Block>... tags) {
        for (TagKey<Block> tag : tags) {
            OPTIONAL_TAGS.computeIfAbsent(tag, (e) -> new ArrayList<>()).add(id);
        }
    }

    public static TagAppender<Item>
    tagAppender(RegistrateTagsProvider<Item> prov, AllItemTags tag) {
        return tagAppender(prov, tag.tag);
    }

    public static TagAppender<Fluid>
    tagAppender(RegistrateTagsProvider<Fluid> prov, AllFluidTags tag) {
        return tagAppender(prov, tag.tag);
    }

    public static TagAppender<Block>
    tagAppender(RegistrateTagsProvider<Block> prov, AllBlockTags tag) {
        return tagAppender(prov, tag.tag);
    }

    @ExpectPlatform // this has to be platformed out because addTag on fabric has a signature that includes FabricTagProvider$FabricTagBuilder
    public static <T> TagAppender<T>
    tagAppender(RegistrateTagsProvider<T> prov, TagKey<T> tag) {
        throw new AssertionError();
    }
}
