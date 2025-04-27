package net.electrisoma.bloodisfuel;

import net.electrisoma.bloodisfuel.registry.BAdvancements;
import net.electrisoma.bloodisfuel.registry.advancement.BTriggers;


public class ModSetupLate {

    public static void registerPostRegistration() {
        BAdvancements.register();
        BTriggers.register();
    }
}
