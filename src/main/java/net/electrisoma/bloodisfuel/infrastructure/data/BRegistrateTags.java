package net.electrisoma.bloodisfuel.infrastructure.data;

import net.electrisoma.bloodisfuel.registry.*;
import net.electrisoma.bloodisfuel.BloodIsFuel;

import com.simibubi.create.foundation.data.TagGen;
import com.simibubi.create.foundation.data.CreateRegistrate;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;

import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;


//tw: this file contains horrors beyond human comprehension, you will be scarred
@SuppressWarnings({"unused", "deprecation", "RedundantSuppression"})
public class BRegistrateTags {
	private static final CreateRegistrate REGISTRATE = BloodIsFuel.registrate();
	
	public static void addGenerators() {
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, BRegistrateTags::genItemTags);
		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, BRegistrateTags::genBlockTags);
		REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, BRegistrateTags::genFluidTags);
		REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, BRegistrateTags::genEntityTags);
	}

	private static void genItemTags(RegistrateTagsProvider<Item> provIn) {
		TagGen.CreateTagsProvider<Item> prov = new TagGen.CreateTagsProvider<>(provIn, Item::builtInRegistryHolder);

		prov.tag(BTags.BItemTags.CARBOHYDRATES.tag)

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
				.addOptional(BloodIsFuel.path("create:sweet_roll"))
				.addOptional(BloodIsFuel.path("create:chocolate_glazed_berries"))
				.addOptional(BloodIsFuel.path("create:honeyed_apple"))
				.addOptional(BloodIsFuel.path("create:bar_of_chocolate"))

				//CC&A
				.addOptional(BloodIsFuel.path("createaddition:chocolate_cake"))

				//Garnished
				.addOptionalTag(BloodIsFuel.path("garnished:garnished_foods"))
				.addOptionalTag(BloodIsFuel.path("garnished:aversion_foods"))

				//Confectionery
				.addOptional(BloodIsFuel.path("create_confectionery:bar_of_caramel"))
				.addOptional(BloodIsFuel.path("create_confectionery:bar_of_black_chocolate"))
				.addOptional(BloodIsFuel.path("create_confectionery:bar_of_white_chocolate"))
				.addOptional(BloodIsFuel.path("create_confectionery:bar_of_ruby_chocolate"))
				.addOptional(BloodIsFuel.path("create_confectionery:full_chocolate_bar"))
				.addOptional(BloodIsFuel.path("create_confectionery:full_black_chocolate_bar"))
				.addOptional(BloodIsFuel.path("create_confectionery:full_white_chocolate_bar"))
				.addOptional(BloodIsFuel.path("create_confectionery:full_ruby_chocolate_bar"))
				.addOptional(BloodIsFuel.path("create_confectionery:caramel_glazed_berries"))
				.addOptional(BloodIsFuel.path("create_confectionery:dark_chocolate_glazed_berries"))
				.addOptional(BloodIsFuel.path("create_confectionery:white_chocolate_glazed_berries"))
				.addOptional(BloodIsFuel.path("create_confectionery:ruby_chocolate_glazed_berries"))
				.addOptional(BloodIsFuel.path("create_confectionery:marshmallow"))
				.addOptional(BloodIsFuel.path("create_confectionery:marshmallow_on_a_stick"))
				.addOptional(BloodIsFuel.path("create_confectionery:caramelized_marshmellow_on_a_stick"))
				.addOptional(BloodIsFuel.path("create_confectionery:chocolate_glazed_marshmallow"))
				.addOptional(BloodIsFuel.path("create_confectionery:black_chocolate_glazed_marshmallow"))
				.addOptional(BloodIsFuel.path("create_confectionery:white_chocolate_glazed_marshmallow"))
				.addOptional(BloodIsFuel.path("create_confectionery:ruby_chocolate_glazed_marshmallow"))
				.addOptional(BloodIsFuel.path("create_confectionery:candy_cane"))
				.addOptional(BloodIsFuel.path("create_confectionery:crushed_cocoa"))
				.addOptional(BloodIsFuel.path("create_confectionery:cocoa_butter"))
				.addOptional(BloodIsFuel.path("create_confectionery:cocoa_powder"))
				.addOptional(BloodIsFuel.path("create_confectionery:gingerbread"))
				.addOptional(BloodIsFuel.path("create_confectionery:gingerbread_man"))
				.addOptional(BloodIsFuel.path("create_confectionery:honey_candy"))
				.addOptional(BloodIsFuel.path("create_confectionery:chocolate_candy"))
				.addOptional(BloodIsFuel.path("create_confectionery:chocolate_candy_1"))
				.addOptional(BloodIsFuel.path("create_confectionery:chocolate_candy_2"))
				.addOptional(BloodIsFuel.path("create_confectionery:chocolate_candy_3"))
				.addOptional(BloodIsFuel.path("create_confectionery:dark_chocolate_candy"))
				.addOptional(BloodIsFuel.path("create_confectionery:dark_chocolate_candy_1"))
				.addOptional(BloodIsFuel.path("create_confectionery:dark_chocolate_candy_2"))
				.addOptional(BloodIsFuel.path("create_confectionery:dark_chocolate_candy_3"))
				.addOptional(BloodIsFuel.path("create_confectionery:white_chocolate_candy"))
				.addOptional(BloodIsFuel.path("create_confectionery:white_chocolate_candy_1"))
				.addOptional(BloodIsFuel.path("create_confectionery:white_chocolate_candy_2"))
				.addOptional(BloodIsFuel.path("create_confectionery:white_chocolate_candy_3"))
				.addOptional(BloodIsFuel.path("create_confectionery:ruby_chocolate_candy"))
				.addOptional(BloodIsFuel.path("create_confectionery:ruby_chocolate_candy_1"))
				.addOptional(BloodIsFuel.path("create_confectionery:ruby_chocolate_candy_2"))
				.addOptional(BloodIsFuel.path("create_confectionery:ruby_chocolate_candy_3"))

				//Biomancy
				.addOptional(BloodIsFuel.path("biomancy:nutrient_paste"))
				.addOptional(BloodIsFuel.path("biomancy:nutrient_bar"))

				//BiC
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:mint_candy"))
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:mint_ice_cream"))
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:coffee_candy"))
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:gummy_vampire_teeth"))
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:chocolate_heart"))
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:caramel_pepper"))
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:holiday_candy"))
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:magical_holiday_candy"))
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:eternal_candy"))
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:creepy_cookies_with_milk"))
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:spiritual_gingerbread"))
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:spiritual_dust"))
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:intoxicating_decoction"))
		;

		prov.tag(BTags.BItemTags.MEATS.tag)

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
				.add(TagEntry.element(BItems.DRAINED_MEAT.getId()))

				//BiC
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:monster_flesh"))
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:bloody_gadfly_eye"))
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:corpse_maggot"))
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:ethereal_spirit"))

				//Farmer's Delight
				.addOptional(BloodIsFuel.path("farmersdelight:bacon"))
				.addOptional(BloodIsFuel.path("farmersdelight:minced_beef"))
				.addOptional(BloodIsFuel.path("farmersdelight:chicken_cuts"))
				.addOptional(BloodIsFuel.path("farmersdelight:mutton_chops"))
				.addOptional(BloodIsFuel.path("farmersdelight:ham"))
		;

		prov.tag(BTags.BItemTags.FISHES.tag)

				//Vanilla
				.add(
						Items.COD,
						Items.SALMON,
						Items.TROPICAL_FISH,
						Items.PUFFERFISH
				)

				//Farmer's Delight
				.addOptional(BloodIsFuel.path("farmersdelight:cod_slice"))
				.addOptional(BloodIsFuel.path("farmersdelight:salmon_slice"))

				//BiC
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:rotten_fish"))
				.addOptional(BloodIsFuel.path("born_in_chaos_v1:sea_terror_eye"))
		;

		//Biomancy meat
		prov.tag(BTags.BItemTags.RAW_MEATS.tag).add(TagEntry.element(BItems.DRAINED_MEAT.getId()));
	}
	private static void genBlockTags(RegistrateTagsProvider<Block> provIn) {
		TagGen.CreateTagsProvider<Block> prov = new TagGen.CreateTagsProvider<>(provIn, Block::builtInRegistryHolder);

		prov.tag(BTags.BBlockTags.CARBOHYDRATES.tag)

				//AC
				.addOptional(BloodIsFuel.path("alexscaves:block_of_chocolate"))
				.addOptional(BloodIsFuel.path("alexscaves:block_of_polished_chocolate"))
				.addOptional(BloodIsFuel.path("alexscaves:block_of_chiseled_chocolate"))
				.addOptional(BloodIsFuel.path("alexscaves:block_of_frosted_chocolate"))
				.addOptional(BloodIsFuel.path("alexscaves:block_of_frosting"))
				.addOptional(BloodIsFuel.path("alexscaves:block_of_vanilla_frosting"))
				.addOptional(BloodIsFuel.path("alexscaves:block_of_chocolate_frosting"))
				.addOptional(BloodIsFuel.path("alexscaves:cake_layer"))
				.addOptional(BloodIsFuel.path("alexscaves:cookie_block"))
				.addOptional(BloodIsFuel.path("alexscaves:wafer_cookie_block"))
				.addOptional(BloodIsFuel.path("alexscaves:dough_block"))
				.addOptional(BloodIsFuel.path("alexscaves:confection_oven"))
				.addOptionalTag(BloodIsFuel.path("alexscaves:rock_candies"))
		;
	}
	private static void genEntityTags(RegistrateTagsProvider<EntityType<?>> provIn) {
		TagGen.CreateTagsProvider<EntityType<?>> prov = new TagGen.CreateTagsProvider<>(provIn, EntityType::builtInRegistryHolder);

		prov.tag(BTags.BEntityTags.DOES_NOT_DROP_FLUID.tag)
				.add(EntityType.SILVERFISH)
				.add(EntityType.IRON_GOLEM)
				.addOptionalTag(EntityTypeTags.SKELETONS.location())
		;

		prov.tag(BTags.BEntityTags.ALLOW_FLUID_DROP.tag)
				.add(EntityType.SNOW_GOLEM)
		;

		prov.tag(BTags.BEntityTags.UNDEAD.tag)
				.add(EntityType.ZOMBIE)
				.add(EntityType.ZOMBIE_HORSE)
				.add(EntityType.ZOMBIE_VILLAGER)
				.add(EntityType.ZOMBIFIED_PIGLIN)
				.add(EntityType.ZOGLIN)
		;

		prov.tag(BTags.BEntityTags.ENDER.tag)
				.add(EntityType.ENDERMITE)
				.add(EntityType.ENDERMAN)
				.add(EntityType.ENDER_DRAGON)
				.add(EntityType.SHULKER)
				.add(EntityType.PHANTOM)
		;

		prov.tag(BTags.BEntityTags.SNOWY.tag)
				.add(EntityType.SNOW_GOLEM)
		;
	}
	private static void genFluidTags(RegistrateTagsProvider<Fluid> provIn) {
		TagGen.CreateTagsProvider<Fluid> prov = new TagGen.CreateTagsProvider<>(provIn, Fluid::builtInRegistryHolder);

		prov.tag(BTags.BFluidTags.VISCERA.tag)

				//BoP
				.addOptional(BloodIsFuel.path("biomesoplenty:blood"))

				//TConstruct
				.addOptional(BloodIsFuel.path("tconstruct:meat_soup"))
		;

		prov.tag(BTags.BFluidTags.FUEL.tag)

				//BIF
				.add(TagEntry.element(BFluids.VISCERA.getId()))
				.add(TagEntry.element(BFluids.BLOOD.getId()))
				.add(TagEntry.element(BFluids.ENRICHED_BLOOD.getId()))
				.add(TagEntry.element(BFluids.OIL_ENRICHED_BLOOD.getId()))
				.add(TagEntry.element(BFluids.DIESEL_INFUSED_BLOOD.getId()))
				.add(TagEntry.element(BFluids.GASOLINE_INFUSED_BLOOD.getId()))
		;

		prov.tag(BTags.BFluidTags.LIQUID_CARBOHYDRATES.tag)

				//Forge
				.addOptionalTag(BloodIsFuel.path("forge:honey"))
				.addOptionalTag(BloodIsFuel.path("forge:chocolate"))
				.addOptionalTag(BloodIsFuel.path("forge:tea"))

				//Garnished
				.addOptional(BloodIsFuel.path("garnished:apple_cider"))
				.addOptional(BloodIsFuel.path("garnished:garnish"))
				.addOptional(BloodIsFuel.path("garnished:sweet_tea"))

				//Confectionery
				.addOptional(BloodIsFuel.path("create_confectionery:caramel"))
				.addOptional(BloodIsFuel.path("create_confectionery:black_chocolate"))
				.addOptional(BloodIsFuel.path("create_confectionery:white_chocolate"))
				.addOptional(BloodIsFuel.path("create_confectionery:ruby_chocolate"))
				.addOptional(BloodIsFuel.path("create_confectionery:hot_chocolate"))
				.addOptional(BloodIsFuel.path("create_confectionery:soothing_hot_chocolate"))

				//Biofactory
				.addOptional(BloodIsFuel.path("biofactory:nutrients_fluid"))
		;

		// compat tag (for syringe system)
		prov.tag(BTags.BFluidTags.NUTRIENTS_FLUID.tag).addOptional(BloodIsFuel.path("biofactory:nutrients_fluid"));
	}
}