package net.electrisoma.bloodisfuel.foundation.data.advancements.triggers;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;


public class BTrigger extends BCriterionTrigger<BTrigger.Instance>{
    public BTrigger(String id) {
        super(id);
    }

    public void trigger(ServerPlayer player) {
        super.trigger(player, null);
    }

    @Override
    public Instance createInstance(JsonObject jsonObject, DeserializationContext deserializationContext) {
        return new Instance(getId());
    }
    public Instance instance() {
        return new Instance(getId());
    }

    public static class Instance extends BCriterionTrigger.Instance {
        public Instance(ResourceLocation idIn) {
            super(idIn, ContextAwarePredicate.ANY);
        }

        @Override
        protected boolean test(@Nullable List<Supplier<Object>> suppliers) {
            return true;
        }
    }

}
