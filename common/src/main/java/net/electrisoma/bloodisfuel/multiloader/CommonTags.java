package net.electrisoma.bloodisfuel.multiloader;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.Registries;

import java.util.List;
import java.util.ArrayList;


@SuppressWarnings("unused")
public class CommonTags {

    public static final List<CommonTag<Item>> ALL_ITEMS = new ArrayList<>();
    public static final List<CommonTag<Block>> ALL_BLOCKS = new ArrayList<>();

    public static final CommonTag<Item>
            STRING = item("leather")
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
