package net.electrisoma.bloodisfuel.multiloader;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;


public class CommonTags {
    public static final List<CommonTag<Item>> ALL_ITEMS = new ArrayList<>();
    public static final List<CommonTag<Block>> ALL_BLOCKS = new ArrayList<>();

    public static final CommonTag<Item>
            STRING = item("string"),
            IRON_NUGGETS = item("nuggets/iron_nuggets", "iron_nuggets", "nuggets/iron"),
            ZINC_NUGGETS = item("nuggets/zinc_nuggets", "zinc_nuggets", "nuggets/zinc"),
            BRASS_NUGGETS = item("nuggets/brass_nuggets", "brass_nuggets", "nuggets/brass"),
            IRON_PLATES = item("plates/iron_plates", "iron_plates", "plates/iron"),
            GOLD_PLATES = item("plates/gold_plates", "gold_plates", "plates/gold"),
            COPPER_INGOTS = item("ingots/copper_ingots", "copper_ingots", "ingots/copper"),
            IRON_INGOTS = item("ingots/iron_ingots", "iron_ingots", "ingots/iron");

    ;

    /**
     * Only for writing TO, not for testing
     */
    public static final CommonTag<Block>
            RELOCATION_NOT_SUPPORTED = block("relocation_not_supported");

    public static CommonTag<Block> block(String path) {
        CommonTag<Block> tag = CommonTag.conventional(Registries.BLOCK, path);
        ALL_BLOCKS.add(tag);
        return tag;
    }

    public static CommonTag<Item> item(String common, String fabric, String forge) {
        CommonTag<Item> tag = CommonTag.conventional(Registries.ITEM, common, fabric, forge);
        ALL_ITEMS.add(tag);
        return tag;
    }

    public static CommonTag<Item> item(String path) {
        return item(path, path, path);
    }
}
