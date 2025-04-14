package net.electrisoma.bloodisfuel.registry.advancement;

import com.google.gson.JsonObject;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;

import java.util.List;
import java.util.function.Supplier;


@SuppressWarnings("all")
public class SimpleBloodisFuelTrigger extends CriterionTriggerBase<SimpleBloodisFuelTrigger.Instance>{

    public SimpleBloodisFuelTrigger(String id) {
        super(id);
    }

    @Override
    public SimpleBloodisFuelTrigger.Instance
    createInstance(JsonObject json, DeserializationContext context) {
        return new SimpleBloodisFuelTrigger.Instance(getId());
    }

    public void trigger(ServerPlayer player) {
        super.trigger(player, null);
    }

    public SimpleBloodisFuelTrigger.Instance
    instance() {
        return new SimpleBloodisFuelTrigger.Instance(getId());
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
