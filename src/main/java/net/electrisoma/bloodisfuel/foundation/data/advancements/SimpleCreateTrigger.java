package net.electrisoma.bloodisfuel.foundation.data.advancements;

import com.google.gson.JsonObject;

import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.function.Supplier;


@SuppressWarnings("all")
public class SimpleCreateTrigger extends CriterionTriggerBase<SimpleCreateTrigger.Instance> {

    public SimpleCreateTrigger(String id) {
        super(id);
    }

    @Override
    public Instance createInstance(JsonObject json, DeserializationContext context) {
        return new Instance(getId());
    }

    public void trigger(ServerPlayer player) {
        super.trigger(player, null);
    }

    public Instance instance() {
        return new Instance(getId());
    }

    public static class Instance extends CriterionTriggerBase.Instance {

        public Instance(ResourceLocation idIn) {
            super(idIn, ContextAwarePredicate.ANY);
        }

        @Override
        protected boolean test(List<Supplier<Object>> suppliers) {
            return true;
        }
    }
}