package net.electrisoma.bloodisfuel.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;

import com.tterrag.registrate.util.entry.BlockEntry;

import net.electrisoma.bloodisfuel.registry.blocks.engine.portable_engine.PortableEngineBlock;
import net.electrisoma.bloodisfuel.registry.blocks.engine.portable_engine.PortableEngineBlockItem;
import net.electrisoma.bloodisfuel.registry.blocks.engine.portable_engine.PortableEngineGenerator;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;


public class BBlocks {
    private static final CreateRegistrate REGISTRATE = BloodIsFuel.registrate();

    static {REGISTRATE.setCreativeTab(BModTabs.BASE_CREATIVE_TAB);}

    public static void register() {
        BloodIsFuel.LOGGER.info("Registering blocks for " + BloodIsFuel.NAME);
    }

    public static final BlockEntry<PortableEngineBlock> PORTABLE_ENGINE =
            REGISTRATE.block("portable_engine", PortableEngineBlock::new)
            .properties(p -> p
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .noOcclusion()
                    .strength(3f))
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .blockstate(new PortableEngineGenerator()::generate)
            .loot((lt, block) -> {
                LootTable.Builder builder = LootTable.lootTable();
                LootItemCondition.Builder survivesExplosion = ExplosionCondition.survivesExplosion();
                lt.add(block, builder.withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1)).when(survivesExplosion)
                        .add(LootItem.lootTableItem(block)
                                .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                        .copy("Tanks", "BlockEntityTag.Tanks")
                                        .copy("Enchantments", "Enchantments")
                                ).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)))));
            })
            .item(PortableEngineBlockItem::new)
            .transform(customItemModel())
            .register();
}
