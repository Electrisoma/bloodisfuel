package net.electrisoma.bloodisfuel.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.objects.*;
import net.electrisoma.bloodisfuel.infrastructure.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


@SuppressWarnings("unused")
public class BCreativeTabs {
    @ExpectPlatform
    public static ResourceKey<CreativeModeTab> getBaseTabKey() {
        throw new AssertionError();
    }

    public static void register() {
        // just to load class
    }

    public enum Tabs {
        MAIN(BCreativeTabs::getBaseTabKey),
        ;

        private final Supplier<ResourceKey<CreativeModeTab>> keySupplier;

        Tabs(Supplier<ResourceKey<CreativeModeTab>> keySupplier) {
            this.keySupplier = keySupplier;
        }

        public ResourceKey<CreativeModeTab> getKey() {
            return keySupplier.get();
        }

        public void use() {
            use(this);
        }

        @ExpectPlatform
        private static void use(Tabs tab) {
            throw new AssertionError();
        }
    }

    public static final class RegistrateDisplayItemsGenerator implements CreativeModeTab.DisplayItemsGenerator {

        private final Tabs tab;

        public RegistrateDisplayItemsGenerator(Tabs tab) {
            this.tab = tab;
        }

        private static Predicate<Item> makeExclusionPredicate() {
            Set<Item> exclusions = new ReferenceOpenHashSet<>();

            List<ItemProviderEntry<?>> simpleExclusions = List.of(

            );

            for (ItemProviderEntry<?> entry : simpleExclusions) {

            }

            return (item) -> exclusions.contains(item) || item instanceof SequencedAssemblyItem;
        }

        private static List<ItemOrdering> makeOrderings() {
            return new ReferenceArrayList<>();
        }

        private static Function<Item, ItemStack> makeStackFunc() {
            Map<Item, Function<Item, ItemStack>> factories = new Reference2ReferenceOpenHashMap<>();

            Map<ItemProviderEntry<?>, Function<Item, ItemStack>> simpleFactories = Map.of(
                /*AllItems.COPPER_BACKTANK, item -> {
                    ItemStack stack = new ItemStack(item);
                    stack.getOrCreateTag().putInt("Air", BacktankUtil.maxAirWithoutEnchants());
                    return stack;
                },
                AllItems.NETHERITE_BACKTANK, item -> {
                    ItemStack stack = new ItemStack(item);
                    stack.getOrCreateTag().putInt("Air", BacktankUtil.maxAirWithoutEnchants());
                    return stack;
                }*/
            );

            simpleFactories.forEach((entry, factory) -> {
                factories.put(entry.asItem(), factory);
            });

            return item -> {
                Function<Item, ItemStack> factory = factories.get(item);
                if (factory != null) {
                    return factory.apply(item);
                }
                return new ItemStack(item);
            };
        }

        private static Function<Item, TabVisibility> makeVisibilityFunc() {
            Map<Item, TabVisibility> visibilities = new Reference2ObjectOpenHashMap<>();

            Map<ItemProviderEntry<?>, TabVisibility> simpleVisibilities = Map.of(

            );

            simpleVisibilities.forEach((entry, factory) -> {
                visibilities.put(entry.asItem(), factory);
            });

            return item -> {
                CreativeModeTab.TabVisibility visibility = visibilities.get(item);
                return Objects.requireNonNullElse(visibility, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            };
        }

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters pParameters, CreativeModeTab.Output output) {
            Predicate<Item> exclusionPredicate = makeExclusionPredicate();
            List<ItemOrdering> orderings = makeOrderings();
            Function<Item, ItemStack> stackFunc = makeStackFunc();
            Function<Item, TabVisibility> visibilityFunc = makeVisibilityFunc();
            ResourceKey<CreativeModeTab> tab = this.tab.getKey();

            List<Item> items = new LinkedList<>();
            Predicate<Item> is3d = Env.unsafeRunForDist(
                    () -> () -> item -> Minecraft.getInstance().getItemRenderer().getModel(new ItemStack(item), null, null, 0).isGui3d(),
                    () -> () -> item -> false // don't crash servers
            );
            items.addAll(collectItems(tab, is3d, true, exclusionPredicate));
            items.addAll(collectBlocks(tab, exclusionPredicate));
            items.addAll(collectItems(tab, is3d, false, exclusionPredicate));

            applyOrderings(items, orderings);
            outputAll(output, items, stackFunc, visibilityFunc);
        }

        private List<Item> collectBlocks(ResourceKey<CreativeModeTab> tab, Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList<>();
            for (RegistryEntry<Block> entry : BloodIsFuel.registrate().getAll(Registries.BLOCK)) {
                if (!isInCreativeTab(entry, tab))
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

        private List<Item> collectItems(ResourceKey<CreativeModeTab> tab, Predicate<Item> is3d, boolean special,
                                        Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList<>();

            for (RegistryEntry<Item> entry : BloodIsFuel.registrate().getAll(Registries.ITEM)) {
                if (!isInCreativeTab(entry, tab))
                    continue;
                Item item = entry.get();
                if (item instanceof BlockItem)
                    continue;
                if (is3d.test(item) != special)
                    continue;
                if (!exclusionPredicate.test(item))
                    items.add(item);
            }
            return items;
        }

        @ExpectPlatform
        private static boolean isInCreativeTab(RegistryEntry<?> entry, ResourceKey<CreativeModeTab> tab) {
            throw new AssertionError();
        }

        private static void applyOrderings(List<Item> items, List<ItemOrdering> orderings) {
            for (RegistrateDisplayItemsGenerator.ItemOrdering ordering : orderings) {
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
                    if (ordering.type() == RegistrateDisplayItemsGenerator.ItemOrdering.Type.AFTER) {
                        items.add(anchorIndex + 1, item);
                    } else {
                        items.add(anchorIndex, item);
                    }
                }
            }
        }

        private static void outputAll(CreativeModeTab.Output output, List<Item> items, Function<Item, ItemStack> stackFunc, Function<Item, CreativeModeTab.TabVisibility> visibilityFunc) {
            for (Item item : items) {
                output.accept(stackFunc.apply(item), visibilityFunc.apply(item));
            }
        }

        private record ItemOrdering(Item item, Item anchor, RegistrateDisplayItemsGenerator.ItemOrdering.Type type) {
            public static RegistrateDisplayItemsGenerator.ItemOrdering before(Item item, Item anchor) {
                return new RegistrateDisplayItemsGenerator.ItemOrdering(item, anchor, RegistrateDisplayItemsGenerator.ItemOrdering.Type.BEFORE);
            }

            public static RegistrateDisplayItemsGenerator.ItemOrdering after(Item item, Item anchor) {
                return new RegistrateDisplayItemsGenerator.ItemOrdering(item, anchor, RegistrateDisplayItemsGenerator.ItemOrdering.Type.AFTER);
            }

            public enum Type {
                BEFORE,
                AFTER;
            }
        }
    }

    public record TabInfo(ResourceKey<CreativeModeTab> key, CreativeModeTab tab) {
    }
}
