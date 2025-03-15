package net.electrisoma.bloodisfuel.config;

import com.simibubi.create.foundation.config.ConfigBase;


public class BServer extends ConfigBase {

    public final ConfigGroup infrastructure =
            group(0, "infrastructure", Comments.infrastructure);


    public final ConfigGroup TOOL_CAPACITY =
            group(200, "tool_capacity", Comments.tool_capacity);

    public final ConfigGroup TOOL_CAPACITY_ENCHANTMENT =
            group(200, "tool_capacity_enchanting", Comments.tool_capacity_enchanting);


    @Override
    public String getName() {
        return "server";
    }


    private static class Comments {
        static String infrastructure =
                "The Backbone of Create: Blood is Fuel!";

        static String tool_capacity =
                "Capacity of Tools requiring Fluids";

        static String tool_capacity_enchanting =
                "Capacity Addition of Tools with Capacity Enchantment";
    }
}
