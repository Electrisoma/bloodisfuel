package net.electrisoma.bloodisfuel.foundation.data.advancements.triggers;

import net.minecraft.advancements.CriteriaTriggers;

import java.util.List;
import java.util.LinkedList;


public class BTriggers {
    private static final List<BCriterionTrigger<?>> triggers = new LinkedList<>();
    public static void register() {
        triggers.forEach(CriteriaTriggers::register);
    }

    public static BTrigger addTrigger(String id) {
        return add(new BTrigger(id));
    }
    private static <T extends BCriterionTrigger<?>> T add(T instance) {
        triggers.add(instance);
        return instance;
    }
}
