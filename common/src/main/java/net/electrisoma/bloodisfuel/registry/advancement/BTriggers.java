package net.electrisoma.bloodisfuel.registry.advancement;

import net.minecraft.advancements.CriteriaTriggers;

import java.util.List;
import java.util.LinkedList;


public class BTriggers {

    public static void register() {
        triggers.forEach(CriteriaTriggers::register);
    }

    private static <T extends CriterionTriggerBase<?>> T add(T instance) {
        triggers.add(instance);
        return instance;
    }

    private static final List<CriterionTriggerBase<?>> triggers = new LinkedList<>();

    public static SimpleBloodisFuelTrigger
    addSimple(String id) {
        return add(new SimpleBloodisFuelTrigger(id));
    }
}
