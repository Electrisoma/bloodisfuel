package net.electrisoma.bloodisfuel.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;

import com.tterrag.registrate.util.entry.BlockEntry;

import net.electrisoma.bloodisfuel.content.equipment.bloodextractor.BloodExtractorBlock;
import net.electrisoma.bloodisfuel.content.equipment.engine.portable_engine.*;
import net.electrisoma.bloodisfuel.content.equipment.engine.vampire_engine.*;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

// TODO: finish the engine
public class BBlocks {
    private static final CreateRegistrate REGISTRATE = BloodIsFuel.registrate();
    static {REGISTRATE.setCreativeTab(BModTabs.BASE_CREATIVE_TAB);}

    public static BlockEntry<PortableEngineBlock> PORTABLE_ENGINE;
    public static BlockEntry<VampireEngineBlock> VAMPIRE_ENGINE;
    public static BlockEntry<BloodExtractorBlock> BLOOD_EXTRACTOR;

    public static void register() {
        BloodIsFuel.LOGGER.info("Registering blocks for " + BloodIsFuel.NAME);

        PORTABLE_ENGINE = REGISTRATE
                .block("portable_engine", PortableEngineBlock::new)
                .properties(p -> p
                        .mapColor(MapColor.METAL)
                        .sound(SoundType.NETHERITE_BLOCK)
                        .noOcclusion()
                        .strength(3f))
                .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                .blockstate(new PortableEngineGenerator()::generate)
                .item(PortableEngineBlockItem::new)
                .transform(customItemModel())
                .register();

        VAMPIRE_ENGINE = REGISTRATE
                .block("vampire_engine", VampireEngineBlock::new)
                .properties(p -> p
                        .mapColor(MapColor.METAL)
                        .sound(SoundType.NETHERITE_BLOCK)
                        .noOcclusion()
                        .strength(3f))
                .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                .blockstate(new VampireEngineGenerator()::generate)
                .item()
                .transform(customItemModel())
                .register();

        BLOOD_EXTRACTOR = REGISTRATE
                .block("blood_extractor", BloodExtractorBlock::new)
                .properties(p -> p
                        .mapColor(MapColor.METAL)
                        .sound(SoundType.NETHERITE_BLOCK)
                        .noOcclusion()
                        .strength(3f))
                .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                .item()
                .transform(customItemModel())
                .register();
    }

//    public static final BlockEntry<PortableEngineBlock> PORTABLE_ENGINE = REGISTRATE
//            .block("portable_engine", PortableEngineBlock::new)
//            .properties(p -> p
//                    .mapColor(MapColor.METAL)
//                    .sound(SoundType.NETHERITE_BLOCK)
//                    .noOcclusion()
//                    .strength(3f))
//            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
//            .blockstate(new PortableEngineGenerator()::generate)
//            .loot((lt, block) -> {
//                LootTable.Builder builder = LootTable.lootTable();
//                LootItemCondition.Builder survivesExplosion = ExplosionCondition.survivesExplosion();
//                lt.add(block, builder.withPool(LootPool.lootPool()
//                        .setRolls(ConstantValue.exactly(1)).when(survivesExplosion)
//                        .add(LootItem.lootTableItem(block)
//                                .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
//                                        .copy("Tanks", "BlockEntityTag.Tanks")
//                                        .copy("Enchantments", "Enchantments")
//                                ).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)))));
//            })
//            .item(PortableEngineBlockItem::new)
//            .transform(customItemModel())
//            .register();
}
