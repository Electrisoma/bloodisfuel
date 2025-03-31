package net.electrisoma.bloodisfuel.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;

import net.createmod.catnip.lang.Lang;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;


public class BTags {
    public static <T> TagKey<T> optionalTag(Registry<T> registry, ResourceLocation id) {
        return TagKey.create(registry.key(), id);
    }

    public static <T> TagKey<T> forgeTag(Registry<T> registry, String path) {
        return optionalTag(registry, new ResourceLocation("c", path));
    }

    public static TagKey<Block> forgeBlockTag(String path) {
        return forgeTag(BuiltInRegistries.BLOCK, path);
    }

    public static TagKey<Item> forgeItemTag(String path) {
        return forgeTag(BuiltInRegistries.ITEM, path);
    }

    public static TagKey<Fluid> forgeFluidTag(String path) {
        return forgeTag(BuiltInRegistries.FLUID, path);
    }

    public enum NameSpace {

        MOD(BloodIsFuel.MOD_ID, false, true),
        FORGE("forge"),

        ;

        public final String id;
        public final boolean optionalDefault;
        public final boolean alwaysDatagenDefault;

        NameSpace(String id) {
            this(id, true, false);
        }

        NameSpace(String id, boolean optionalDefault, boolean alwaysDatagenDefault) {
            this.id = id;
            this.optionalDefault = optionalDefault;
            this.alwaysDatagenDefault = alwaysDatagenDefault;
        }
    }

    public enum AllBlockTags {
        CARBOHYDRATES,
        MEATS,
        EXAMPLE
        ;

        public final TagKey<Block> tag;
        public final boolean alwaysDatagen;


        AllBlockTags() {
            this(NameSpace.MOD);
        }

        AllBlockTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        AllBlockTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        AllBlockTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        AllBlockTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(BuiltInRegistries.BLOCK, id);
            } else {
                tag = TagKey.create(Registries.BLOCK, id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }

        @SuppressWarnings("deprecation")
        public boolean matches(Block block) {
            return block.builtInRegistryHolder()
                    .is(tag);
        }

        public boolean matches(ItemStack stack) {
            return stack != null && stack.getItem() instanceof BlockItem blockItem && matches(blockItem.getBlock());
        }

        public boolean matches(BlockState state) {
            return state.is(tag);
        }

        public static void register() {
        }
    }

    public enum AllItemTags {
        EXAMPLE,
        CARBOHYDRATES,
        FISHES,
        MEATS,
        MODDED_FISHES,
        MODDED_MEATS,
        RAW_MEATS,
        SYRINGE_BLADE;

        public final TagKey<Item> tag;
        public final boolean alwaysDatagen;

        AllItemTags() {
            this(NameSpace.MOD);
        }

        AllItemTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        AllItemTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        AllItemTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        AllItemTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(BuiltInRegistries.ITEM, id);
            } else {
                tag = TagKey.create(Registries.ITEM, id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }

        @SuppressWarnings("deprecation")
        public boolean matches(Item item) {
            return item.builtInRegistryHolder()
                    .is(tag);
        }

        public boolean matches(ItemStack stack) {
            return stack.is(tag);
        }

        public static void register() {
        }
    }

    public enum AllFluidTags {
        BLOOD,
        CRUDE_OIL(NameSpace.FORGE),
        DIESEL_INFUSED_BLOOD,
        ENRICHED_BLOOD,
        FUEL,
        GASOLINE_INFUSED_BLOOD,
        LIQUID_CARBOHYDRATES,
        OIL_ENRICHED_BLOOD,
        VISCERA;

        public final TagKey<Fluid> tag;
        public final boolean alwaysDatagen;

        AllFluidTags() {
            this(NameSpace.MOD);
        }

        AllFluidTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        AllFluidTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        AllFluidTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        AllFluidTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
            tag = optionalTag(BuiltInRegistries.FLUID, id);
            this.alwaysDatagen = alwaysDatagen;
        }

        @SuppressWarnings("deprecation")
        public boolean matches(Fluid fluid) {
            return fluid.builtInRegistryHolder()
                    .is(tag);
        }

        public boolean matches(FluidState state) {
            return state.is(tag);
        }

        public static void register() {
        }
    }

    public static void register() {
        AllBlockTags.register();
        AllItemTags.register();
        AllFluidTags.register();
    }
}
