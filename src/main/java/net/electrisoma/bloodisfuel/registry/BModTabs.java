package net.electrisoma.bloodisfuel.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;

import it.unimi.dsi.fastutil.objects.*;

import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;

import net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator;
import net.electrisoma.bloodisfuel.registry.items.BItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import org.apache.commons.lang3.mutable.MutableObject;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.CreativeModeTab.Output;


public class BModTabs {

    private static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BloodIsFuel.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BASE_CREATIVE_TAB = REGISTER.register("base",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.bloodisfuel.base"))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(() -> BItems.DRAINED_MEAT.asStack())
                    .displayItems(new RegistrateDisplayItemsGenerator(true, BModTabs.BASE_CREATIVE_TAB))
                    .build());

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }

    private static class RegistrateDisplayItemsGenerator implements DisplayItemsGenerator {
        private static final Predicate<Item> IS_ITEM_3D_PREDICATE;

        static {
            MutableObject<Predicate<Item>> isItem3d = new MutableObject<>(item -> false);
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                isItem3d.setValue(item -> {
                    ItemRenderer itemRenderer = Minecraft.getInstance()
                            .getItemRenderer();
                    BakedModel model = itemRenderer.getModel(new ItemStack(item), null, null, 0);
                    return model.isGui3d();
                });
            });
            IS_ITEM_3D_PREDICATE = isItem3d.getValue();
        }

        @OnlyIn(Dist.CLIENT)
        private static Predicate<Item> makeClient3dItemPredicate() {
            return item -> {
                ItemRenderer itemRenderer = Minecraft.getInstance()
                        .getItemRenderer();
                BakedModel model = itemRenderer.getModel(new ItemStack(item), null, null, 0);
                return model.isGui3d();
            };
        }

        private final boolean addItems;
        private final RegistryObject<CreativeModeTab> tabFilter;

        public RegistrateDisplayItemsGenerator(boolean addItems, RegistryObject<CreativeModeTab> tabFilter) {
            this.addItems = addItems;
            this.tabFilter = tabFilter;
        }

        private static Predicate<Item> makeExclusionPredicate() {
            Set<Item> exclusions = new ReferenceOpenHashSet<>();

            List<ItemProviderEntry<?>> simpleExclusions = List.of(
                    );

//            for (ItemProviderEntry<?> entry : simpleExclusions) {
//                exclusions.add(entry.asItem());
//            }

            return exclusions::contains;
            }

        private static List<ItemOrdering> makeOrderings() {
            return new ReferenceArrayList<>();
        }

        private static Function<Item, ItemStack> makeStackFunc() {
            Map<Item, Function<Item, ItemStack>> factories = new Reference2ReferenceOpenHashMap<>();

            Map<ItemProviderEntry<?>, Function<Item, ItemStack>> simpleFactories = Map.of(
            );

//            simpleFactories.forEach((entry, factory) -> {
//                factories.put(entry.asItem(), factory);
//            });

            return item -> {
                Function<Item, ItemStack> factory = factories.get(item);
                if (factory != null) {
                    return factory.apply(item);
                }
                return new ItemStack(item);
            };
        }

        private static Function<Item, CreativeModeTab.TabVisibility> makeVisibilityFunc() {
            Map<Item, CreativeModeTab.TabVisibility> visibilities = new Reference2ObjectOpenHashMap<>();

            return item -> {
                CreativeModeTab.TabVisibility visibility = visibilities.get(item);
                return Objects.requireNonNullElse(visibility, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            };
        }

        private List<Item> collectBlocks(Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList<>();
            for (RegistryEntry<Block> entry : BloodIsFuel.registrate().getAll(Registries.BLOCK)) {
                if (!CreateRegistrate.isInCreativeTab(entry, tabFilter))
                    continue;
                Item item = entry.get()
                        .asItem();
                if (item == Items.AIR)
                    continue;
                if (!exclusionPredicate.test(item))
                    items.add(item);
            }
            items = new ReferenceArrayList<>(new ReferenceLinkedOpenHashSet<>(items));
            return items;
        }

        private List<Item> collectItems(Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList<>();
            for (RegistryEntry<Item> entry : BloodIsFuel.registrate().getAll(Registries.ITEM)) {
                if (!CreateRegistrate.isInCreativeTab(entry, tabFilter))
                    continue;
                Item item = entry.get();
                if (item instanceof BlockItem)
                    continue;
                if (!exclusionPredicate.test(item))
                    items.add(item);
            }
            return items;
        }

        private static void applyOrderings(List<Item> items, List<ItemOrdering> orderings) {
            for (ItemOrdering ordering : orderings) {
                int anchorIndex = items.indexOf(ordering.anchor());
                if (anchorIndex != -1) {
                    Item item = ordering.item();
                    int itemIndex = items.indexOf(item);
                    if (itemIndex != -1) {
                        items.remove(itemIndex);
                        if (itemIndex < anchorIndex) {
                            anchorIndex--;
                        }
                    }
                    if (ordering.type() == ItemOrdering.Type.AFTER) {
                        items.add(anchorIndex + 1, item);
                    } else {
                        items.add(anchorIndex, item);
                    }
                }
            }
        }

        @Override
        public void accept(ItemDisplayParameters parameters, Output output) {
            Predicate<Item> exclusionPredicate = makeExclusionPredicate();
            List<ItemOrdering> orderings = makeOrderings();
            Function<Item, ItemStack> stackFunc = makeStackFunc();
            Function<Item, TabVisibility> visibilityFunc = makeVisibilityFunc();

            List<Item> items = new LinkedList<>();
            if (addItems) {
                items.addAll(collectItems(exclusionPredicate.or(IS_ITEM_3D_PREDICATE.negate())));
            }
            items.addAll(collectBlocks(exclusionPredicate));
            if (addItems) {
                items.addAll(collectItems(exclusionPredicate.or(IS_ITEM_3D_PREDICATE)));
            }

            applyOrderings(items, orderings);
            outputAll(output, items, stackFunc, visibilityFunc);
        }

        private static void outputAll(Output output, List<Item> items, Function<Item, ItemStack> stackFunc, Function<Item, TabVisibility> visibilityFunc) {
            for (Item item : items) {
                output.accept(stackFunc.apply(item), visibilityFunc.apply(item));
            }
        }

        private record ItemOrdering(Item item, Item anchor, Type type) {
            public static ItemOrdering before(Item item, Item anchor) {
                return new ItemOrdering(item, anchor, Type.BEFORE);
            }

            public static ItemOrdering after(Item item, Item anchor) {
                return new ItemOrdering(item, anchor, Type.AFTER);
            }

            public enum Type {
                BEFORE,
                AFTER;
            }
        }
    }
}
