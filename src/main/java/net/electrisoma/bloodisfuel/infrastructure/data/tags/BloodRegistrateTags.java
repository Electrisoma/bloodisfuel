package net.electrisoma.bloodisfuel.infrastructure.data.tags;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.infrastructure.utils.Utils;
import net.electrisoma.bloodisfuel.registry.fluids.BIF_Fluids;
import net.electrisoma.bloodisfuel.registry.items.BIF_Items;
import net.electrisoma.bloodisfuel.registry.BIF_Tags;

import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;

import net.minecraft.tags.TagEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;


//tw: this file contains horrors beyond human comprehension, you will be scarred
@SuppressWarnings({"unused", "deprecation"})
public class BloodRegistrateTags  {
	public static void addGenerators() {
		BloodIsFuel.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, BloodRegistrateTags::genItemTags);
		BloodIsFuel.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, BloodRegistrateTags::genBlockTags);
		BloodIsFuel.REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, BloodRegistrateTags::genFluidTags);
		BloodIsFuel.REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, BloodRegistrateTags::genEntityTags);
	}

	private static void genItemTags(RegistrateTagsProvider<Item> provIn) {
		TagGen.CreateTagsProvider<Item> prov =
				new TagGen.CreateTagsProvider<>(provIn, Item::builtInRegistryHolder);

		prov.tag(BIF_Tags.AllItemTags.CARBOHYDRATES.tag)
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
				.addOptional(Utils.location("create:sweet_roll"))
				.addOptional(Utils.location("create:chocolate_glazed_berries"))
				.addOptional(Utils.location("create:honeyed_apple"))
				.addOptional(Utils.location("create:bar_of_chocolate"))

				//CC&A
				.addOptional(Utils.location("createaddition:chocolate_cake"))

				//Garnished
				.addOptionalTag(Utils.location("garnished:garnished_foods"))
				.addOptionalTag(Utils.location("garnished:aversion_foods"))

				//Confectionery
				.addOptional(Utils.location("create_confectionery:bar_of_caramel"))
				.addOptional(Utils.location("create_confectionery:bar_of_black_chocolate"))
				.addOptional(Utils.location("create_confectionery:bar_of_white_chocolate"))
				.addOptional(Utils.location("create_confectionery:bar_of_ruby_chocolate"))
				.addOptional(Utils.location("create_confectionery:full_chocolate_bar"))
				.addOptional(Utils.location("create_confectionery:full_black_chocolate_bar"))
				.addOptional(Utils.location("create_confectionery:full_white_chocolate_bar"))
				.addOptional(Utils.location("create_confectionery:full_ruby_chocolate_bar"))
				.addOptional(Utils.location("create_confectionery:caramel_glazed_berries"))
				.addOptional(Utils.location("create_confectionery:dark_chocolate_glazed_berries"))
				.addOptional(Utils.location("create_confectionery:white_chocolate_glazed_berries"))
				.addOptional(Utils.location("create_confectionery:ruby_chocolate_glazed_berries"))
				.addOptional(Utils.location("create_confectionery:marshmallow"))
				.addOptional(Utils.location("create_confectionery:marshmallow_on_a_stick"))
				.addOptional(Utils.location("create_confectionery:caramelized_marshmellow_on_a_stick"))
				.addOptional(Utils.location("create_confectionery:chocolate_glazed_marshmallow"))
				.addOptional(Utils.location("create_confectionery:black_chocolate_glazed_marshmallow"))
				.addOptional(Utils.location("create_confectionery:white_chocolate_glazed_marshmallow"))
				.addOptional(Utils.location("create_confectionery:ruby_chocolate_glazed_marshmallow"))
				.addOptional(Utils.location("create_confectionery:candy_cane"))
				.addOptional(Utils.location("create_confectionery:crushed_cocoa"))
				.addOptional(Utils.location("create_confectionery:cocoa_butter"))
				.addOptional(Utils.location("create_confectionery:cocoa_powder"))
				.addOptional(Utils.location("create_confectionery:gingerbread"))
				.addOptional(Utils.location("create_confectionery:gingerbread_man"))
				.addOptional(Utils.location("create_confectionery:honey_candy"))
				.addOptional(Utils.location("create_confectionery:chocolate_candy"))
				.addOptional(Utils.location("create_confectionery:chocolate_candy_1"))
				.addOptional(Utils.location("create_confectionery:chocolate_candy_2"))
				.addOptional(Utils.location("create_confectionery:chocolate_candy_3"))
				.addOptional(Utils.location("create_confectionery:dark_chocolate_candy"))
				.addOptional(Utils.location("create_confectionery:dark_chocolate_candy_1"))
				.addOptional(Utils.location("create_confectionery:dark_chocolate_candy_2"))
				.addOptional(Utils.location("create_confectionery:dark_chocolate_candy_3"))
				.addOptional(Utils.location("create_confectionery:white_chocolate_candy"))
				.addOptional(Utils.location("create_confectionery:white_chocolate_candy_1"))
				.addOptional(Utils.location("create_confectionery:white_chocolate_candy_2"))
				.addOptional(Utils.location("create_confectionery:white_chocolate_candy_3"))
				.addOptional(Utils.location("create_confectionery:ruby_chocolate_candy"))
				.addOptional(Utils.location("create_confectionery:ruby_chocolate_candy_1"))
				.addOptional(Utils.location("create_confectionery:ruby_chocolate_candy_2"))
				.addOptional(Utils.location("create_confectionery:ruby_chocolate_candy_3"))

				//Biomancy
				.addOptional(Utils.location("biomancy:nutrient_paste"))
				.addOptional(Utils.location("biomancy:nutrient_bar"))

				//BiC
				.addOptional(Utils.location("born_in_chaos_v1:mint_candy"))
				.addOptional(Utils.location("born_in_chaos_v1:mint_ice_cream"))
				.addOptional(Utils.location("born_in_chaos_v1:coffee_candy"))
				.addOptional(Utils.location("born_in_chaos_v1:gummy_vampire_teeth"))
				.addOptional(Utils.location("born_in_chaos_v1:chocolate_heart"))
				.addOptional(Utils.location("born_in_chaos_v1:caramel_pepper"))
				.addOptional(Utils.location("born_in_chaos_v1:holiday_candy"))
				.addOptional(Utils.location("born_in_chaos_v1:magical_holiday_candy"))
				.addOptional(Utils.location("born_in_chaos_v1:eternal_candy"))
				.addOptional(Utils.location("born_in_chaos_v1:creepy_cookies_with_milk"))
				.addOptional(Utils.location("born_in_chaos_v1:spiritual_gingerbread"))
				.addOptional(Utils.location("born_in_chaos_v1:spiritual_dust"))
				.addOptional(Utils.location("born_in_chaos_v1:intoxicating_decoction"))
		;

		prov.tag(BIF_Tags.AllItemTags.MEATS.tag)
				//Vanilla
				.add(
						Items.BEEF,
						Items.PORKCHOP,
						Items.MUTTON,
						Items.CHICKEN,
						Items.RABBIT,
						Items.ROTTEN_FLESH,
						Items.SPIDER_EYE,
						Items.FERMENTED_SPIDER_EYE
						)

				//BIF
				.add(TagEntry.element(BIF_Items.DRAINED_MEAT.getId()))

				//BiC
				.addOptional(Utils.location("born_in_chaos_v1:monster_flesh"))
				.addOptional(Utils.location("born_in_chaos_v1:bloody_gadfly_eye"))
				.addOptional(Utils.location("born_in_chaos_v1:corpse_maggot"))
				.addOptional(Utils.location("born_in_chaos_v1:ethereal_spirit"))

				//Farmer's Delight
				.addOptional(Utils.location("farmersdelight:bacon"))
				.addOptional(Utils.location("farmersdelight:minced_beef"))
				.addOptional(Utils.location("farmersdelight:chicken_cuts"))
				.addOptional(Utils.location("farmersdelight:mutton_chops"))
				.addOptional(Utils.location("farmersdelight:ham"))
		;

		prov.tag(BIF_Tags.AllItemTags.FISHES.tag)
				//Vanilla
				.add(
						Items.COD,
						Items.SALMON,
						Items.TROPICAL_FISH,
						Items.PUFFERFISH
				)
				//Farmer's Delight
				.addOptional(Utils.location("farmersdelight:cod_slice"))
				.addOptional(Utils.location("farmersdelight:salmon_slice"))

				//BiC
				.addOptional(Utils.location("born_in_chaos_v1:rotten_fish"))
				.addOptional(Utils.location("born_in_chaos_v1:sea_terror_eye"))
		;

		//Biomancy meat
		prov.tag(BIF_Tags.AllItemTags.RAW_MEATS.tag)
				.add(TagEntry.element(BIF_Items.DRAINED_MEAT.getId()))
		;
	}

	private static void genBlockTags(RegistrateTagsProvider<Block> provIn) {
		TagGen.CreateTagsProvider<Block> prov =
				new TagGen.CreateTagsProvider<>(provIn, Block::builtInRegistryHolder);
	}

	private static void genEntityTags(RegistrateTagsProvider<EntityType<?>> provIn) {
		TagGen.CreateTagsProvider<EntityType<?>> prov =
				new TagGen.CreateTagsProvider<>(provIn, EntityType::builtInRegistryHolder);
	}

	private static void genFluidTags(RegistrateTagsProvider<Fluid> provIn) {
		TagGen.CreateTagsProvider<Fluid> prov =
				new TagGen.CreateTagsProvider<>(provIn, Fluid::builtInRegistryHolder);

		prov.tag(BIF_Tags.AllFluidTags.VISCERA.tag)
				//BIF
				.add(TagEntry.element(BIF_Fluids.VISCERA.getId()))

				//BoP
				.addOptional(Utils.location("biomesoplenty:blood"))

				//TConstruct
				.addOptional(Utils.location("tconstruct:meat_soup"))
		;

		prov.tag(BIF_Tags.AllFluidTags.FUEL.tag)
				//BIF
				.add(TagEntry.element(BIF_Fluids.VISCERA.getId()))
				.add(TagEntry.element(BIF_Fluids.BLOOD.getId()))
				.add(TagEntry.element(BIF_Fluids.ENRICHED_BLOOD.getId()))
				.add(TagEntry.element(BIF_Fluids.OIL_ENRICHED_BLOOD.getId()))
				.add(TagEntry.element(BIF_Fluids.DIESEL_INFUSED_BLOOD.getId()))
				.add(TagEntry.element(BIF_Fluids.GASOLINE_INFUSED_BLOOD.getId()))
		;

		prov.tag(BIF_Tags.AllFluidTags.LIQUID_CARBOHYDRATES.tag)
				//Create
				.addOptional(Utils.location("create:honey"))
				.addOptional(Utils.location("create:chocolate"))
				.addOptional(Utils.location("create:tea"))

				//Garnished
				.addOptional(Utils.location("garnished:apple_cider"))
				.addOptional(Utils.location("garnished:garnish"))
				.addOptional(Utils.location("garnished:sweet_tea"))

				//Confectionery
				.addOptional(Utils.location("create_confectionery:caramel"))
				.addOptional(Utils.location("create_confectionery:black_chocolate"))
				.addOptional(Utils.location("create_confectionery:white_chocolate"))
				.addOptional(Utils.location("create_confectionery:ruby_chocolate"))
				.addOptional(Utils.location("create_confectionery:hot_chocolate"))
				.addOptional(Utils.location("create_confectionery:soothing_hot_chocolate"))

				//Biofactory
				.addOptional(Utils.location("biofactory:nutrients_fluid"))
		;
	}
}