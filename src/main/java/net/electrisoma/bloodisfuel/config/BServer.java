package net.electrisoma.bloodisfuel.config;

import net.createmod.catnip.config.ConfigBase;


@SuppressWarnings({"unused"})
public class BServer extends ConfigBase {

    public final ConfigGroup infrastructure =
            group(0, "infrastructure", Comments.infrastructure);

    public final ConfigGroup enchantments =
            group(1, "enchantments", Comments.enchantments);


    public final ConfigInt BLOOD_CAPACITY = i(1000, "blood_capacity",
            Comments.blood_capacity);

    public final ConfigInt BLOOD_CAPACITY_ENCHANTMENT = i(1000, "blood_capacity_enchanting",
            Comments.blood_capacity_enchanting);

    @Override
    public String getName() {
        return "server";
    }

    private static class Comments {
        static String infrastructure =
                "The Backbone of Create: Blood is Fuel!";

        static String enchantments =
                "Capacity of Tools requiring Fluids";

        static String blood_capacity =
                "Capacity of Tools requiring Fluids";

        static String blood_capacity_enchanting =
                "Capacity Addition of Tools with Capacity Enchantment";
    }

}
