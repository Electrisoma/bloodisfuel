package net.electrisoma.bloodisfuel.foundation.data.advancements;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BAdvancements;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.google.common.collect.Maps;


public class BAdvancement {

    static final String LANG = "advancement." + BloodIsFuel.MOD_ID + ".";
    static final String SECRET_SUFFIX = "\n§7(Hidden Advancement)";

    private final String id;
    private final ItemLike icon;
    private final FrameType frameType;

    private final Advancement.Builder builder;
    private final BAdvancement parent;

    private final boolean announces;
    private final boolean toasts;
    private final boolean hidden;

    private String title;
    private String description;

    Advancement datagenResult;

    public BAdvancement(Builder builder) {

        this.builder = Advancement.Builder.advancement();

        this.id = builder.id;
        this.icon = builder.icon;
        this.parent = builder.parent;
        this.frameType = FrameType.byName(builder.frame);
        this.announces = builder.announces;
        this.toasts = builder.toasts;
        this.hidden = builder.hidden;

        this.title = builder.name;
        this.description = builder.description;

        if (this.hidden) description += SECRET_SUFFIX;

        this.builder.display(this.icon, (Component) Component.translatable(titleKey()),
                Component.translatable(descriptionKey()).withStyle(s -> s.withColor(0xFFFFFF)),
                id.equals("root") ? BAdvancements.getBackground() : null,
                this.frameType, this.toasts, this.announces, this.hidden);

        builder.criteriaTriggers.forEach(this.builder::addCriterion);

        BAdvancements.ENTRIES.add(this);
    }

    public void save(Consumer<Advancement> t) {
        if (parent != null) builder.parent(parent.datagenResult);
        datagenResult = this.builder.save(t, BloodIsFuel.asResource(this.id).toString());
    }

    public String titleKey() {
        return LANG + id;
    }

    public String descriptionKey() {
        return LANG + id + ".desc";
    }

    public void provideLang(BiConsumer<String, String> consumer) {
        consumer.accept(titleKey(), title);
        consumer.accept(descriptionKey(), description);
    }

    public static class Builder {

        public final String id;
        public final ItemLike icon;

        public BAdvancement parent;

        public boolean announces = false;
        public boolean toasts = true;
        public boolean hidden = false;

        public String frame = "task";
        public String name;
        public String description;

        private final Map<String, CriterionTriggerInstance> criteriaTriggers = Maps.newLinkedHashMap();

        public Builder(String id, ItemLike icon) {
            this.id = id;
            this.icon = icon;
        }

        public Builder after(BAdvancement after) {
            this.parent = after;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder goal() {
            this.announce();
            this.frame = "goal";
            return this;
        }

        public Builder challenge() {
            this.announce();
            this.frame = "challenge";
            return this;
        }

        public Builder silent() {
            this.toasts = false;
            return this;
        }

        public Builder announce() {
            this.announces = true;
            return this;
        }

        public Builder secret() {
            this.hidden = true;
            return this;
        }

        public Builder free() {
            return this.criterion(this.icon.asItem().toString(), InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] {}));
        }

        public Builder onItemCollected(TagKey<Item> tag) {
            return this.criterion(tag.location().toString(),
                    InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(tag).build()
                    )
            );
        }

        public Builder onItemCollected(ItemLike item) {
            return this.criterion(item.asItem().toString(), InventoryChangeTrigger.TriggerInstance.hasItems(item));
        }

        public Builder onIconCollected() {
            return this.criterion(this.icon.asItem().toString(), InventoryChangeTrigger.TriggerInstance.hasItems(icon.asItem()));
        }

        public Builder onItemConsumed(TagKey<Item> tag) {
            return this.criterion(tag.location().toString(),
                    ConsumeItemTrigger.TriggerInstance.usedItem(
                            ItemPredicate.Builder.item().of(tag).build()
                    )
            );
        }

        public Builder onItemConsumed(ItemLike item) {
            return this.criterion(item.asItem().toString(), ConsumeItemTrigger.TriggerInstance.usedItem(item));
        }

        public Builder onIconConsumed() {
            return this.criterion(this.icon.asItem().toString(), ConsumeItemTrigger.TriggerInstance.usedItem(icon.asItem()));
        }

        public Builder criterion(String key, CriterionTriggerInstance trigger) {
            this.criteriaTriggers.put(key, trigger);
            return this;
        }

        public BAdvancement build() {
            return new BAdvancement(this);
        }
    }
}