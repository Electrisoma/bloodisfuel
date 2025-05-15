package net.electrisoma.bloodisfuel.foundation.data.advancements;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.foundation.data.advancements.triggers.BTrigger;
import net.electrisoma.bloodisfuel.foundation.data.advancements.triggers.BTriggers;
import net.electrisoma.bloodisfuel.registry.BAdvancements;

import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.google.common.collect.Maps;


@SuppressWarnings("all")
public class BAdvancement {

    private static final String LANG = "advancement." + BloodIsFuel.MOD_ID + ".";
    private static final String SECRET_SUFFIX = "\n§7(Hidden Advancement)";

    private final String id;
    private final ItemLike icon;
    private final FrameType frameType;
    private final Advancement.Builder builder;
    private final BTrigger builtinTrigger;
    private final BAdvancement parent;

    private final boolean announces;
    private final boolean toasts;
    private final boolean hidden;

    private final String title;
    private final String description;

    Advancement datagenResult;

    public BAdvancement(Builder builder) {
        this.id = builder.id;
        this.icon = builder.icon;
        this.parent = builder.parent;
        this.frameType = FrameType.byName(builder.frame);
        this.announces = builder.announces;
        this.toasts = builder.toasts;
        this.hidden = builder.hidden;
        this.builtinTrigger = builder.builtinTrigger;

        this.title = builder.name;
        this.description = builder.hidden
                ? builder.description + SECRET_SUFFIX
                : builder.description;

        this.builder = Advancement.Builder.advancement()
                .display(
                        icon,
                        Component.translatable(titleKey()),
                        Component.translatable(descriptionKey()).withStyle(s -> s.withColor(0xFFFFFF)),
                        id.equals("root") ? BAdvancements.getBackground() : null,
                        frameType, toasts, announces, hidden
                );

        builder.criteriaTriggers.forEach(this.builder::addCriterion);
        BAdvancements.ENTRIES.add(this);
    }

    public void save(Consumer<Advancement> output) {
        if (parent != null) builder.parent(parent.datagenResult);
        datagenResult = builder.save(output, BloodIsFuel.asResource(id).toString());
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

    public boolean isAlreadyAwardedTo(Player player) {
        if (!(player instanceof ServerPlayer sp)) return true;
        Advancement advancement = sp.getServer()
                .getAdvancements()
                .getAdvancement(BloodIsFuel.asResource(id));
        if (advancement == null) return true;
        return sp.getAdvancements()
                .getOrStartProgress(advancement)
                .isDone();
    }

    /**
     * Awards this advancement to the given player, if not already completed.
     */
    public void awardTo(ServerPlayer player) {
        if (builtinTrigger == null)
            throw new UnsupportedOperationException("Advancement " + id + " uses external Triggers, it cannot be awarded directly");
        builtinTrigger.trigger(player);
    }

    public static class Builder {
        private final String id;
        private final ItemLike icon;

        private String frame = "task";
        private String name;
        private String description;

        private boolean announces = false;
        private boolean toasts = true;
        private boolean hidden = false;

        private final Map<String, CriterionTriggerInstance> criteriaTriggers = Maps.newLinkedHashMap();
        private BAdvancement parent;

        private BTrigger builtinTrigger;
        private boolean externalTriggerAdded = false;
        private int keyIndex = 0;

        public Builder(String id, ItemLike icon) {
            this.id = id;
            this.icon = icon;
        }

        public Builder externalTrigger(CriterionTriggerInstance trigger) {
            String externalKey = String.valueOf(keyIndex);
            criteriaTriggers.put(externalKey, trigger);
            externalTriggerAdded = true;
            keyIndex++;
            return this;
        }

        public Builder withBuiltinTrigger(BTrigger trigger) {
            this.builtinTrigger = trigger;
            return this;
        }

        public Builder after(BAdvancement parent) {
            this.parent = parent;
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

        public Builder frame(String frame) {
            this.frame = frame;
            return this;
        }

        public Builder goal() {
            this.announce();
            return frame("goal");
        }

        public Builder challenge() {
            this.announce();
            return frame("challenge");
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
            return externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] {}));
        }

        public Builder onItemCollected(TagKey<Item> tag) {
            return criterion(tag.location().toString(),
                    InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(tag).build()));
        }

        public Builder onItemCollected(ItemLike item) {
            externalTriggerAdded = true;
            return criterion(item.asItem().toString(),
                    InventoryChangeTrigger.TriggerInstance.hasItems(item));
        }

        public Builder onIconCollected() {
            return onItemCollected(icon);
        }

        public Builder onItemConsumed(TagKey<Item> tag) {
            return criterion(tag.location().toString(),
                    ConsumeItemTrigger.TriggerInstance.usedItem(
                            ItemPredicate.Builder.item().of(tag).build()));
        }

        public Builder onItemConsumed(ItemLike item) {
            return criterion(item.asItem().toString(),
                    ConsumeItemTrigger.TriggerInstance.usedItem(item));
        }

        public Builder onIconConsumed() {
            return onItemConsumed(icon);
        }

        public Builder criterion(String key, CriterionTriggerInstance trigger) {
            criteriaTriggers.put(key, trigger);
            externalTriggerAdded = true;
            return this;
        }

        public BAdvancement build() {
            if (!externalTriggerAdded && builtinTrigger == null) {
                builtinTrigger = BTriggers.addTrigger(id + "_builtin");
                criteriaTriggers.put("0", builtinTrigger.instance());
            }
            return new BAdvancement(this);
        }
    }
}
