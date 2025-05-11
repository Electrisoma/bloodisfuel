package net.electrisoma.bloodisfuel.foundation.data.advancements.triggers;

import net.minecraft.advancements.CriteriaTriggers;

import java.util.LinkedList;
import java.util.List;

public class BTriggers {
    private static final List<BCriterionTrigger<?>> triggers = new LinkedList<>();

    public static BTrigger addTrigger(String id) {
        return add(new BTrigger(id));
    }

    private static <T extends BCriterionTrigger<?>> T add(T instance) {
        triggers.add(instance);
        return instance;
    }

    public static void register() {
        triggers.forEach(CriteriaTriggers::register);
    }
}
