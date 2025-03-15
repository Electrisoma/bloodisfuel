package net.electrisoma.bloodisfuel.config;

import com.simibubi.create.foundation.config.ConfigBase;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;

import org.jetbrains.annotations.ApiStatus;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


@SuppressWarnings("unused")
public class BConfigs {

    @ApiStatus.Internal
    public static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);

    private static BClient client;
    private static BServer server;

    public static BClient client() {
        return client;
    }

    public static BServer server() {
        return server;
    }

    public static ConfigBase byType(ModConfig.Type type) {
        return CONFIGS.get(type);
    }

    private static <T extends ConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
        Pair<T, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(builder -> {
            T config = factory.get();
            config.registerAll(builder);
            return config;
        });

        T config = specPair.getLeft();
        config.specification = specPair.getRight();
        CONFIGS.put(side, config);
        return config;
    }

    @ApiStatus.Internal
    public static void registerCommon() {
        client = register(BClient::new, ModConfig.Type.CLIENT);
        server = register(BServer::new, ModConfig.Type.SERVER);
    }

    public static void onLoad(ModConfig modConfig) {
        for (ConfigBase config : CONFIGS.values())
            if (config.specification == modConfig
                    .getSpec())
                config.onLoad();
    }

    public static void onReload(ModConfig modConfig) {
        for (ConfigBase config : CONFIGS.values())
            if (config.specification == modConfig
                    .getSpec())
                config.onReload();
    }

    public static String migrateClient(String contents) {
        Map<String, String> m = new HashMap<>();
        m.put("viscera", "visceraTransparencyMultiplier");
        m.put("blood", "bloodTransparencyMultiplier");
        m.put("enriched_blood", "enrichedBloodTransparencyMultiplier");
        m.put("oil_enriched_blood", "oilEnrichedBloodTransparencyMultiplier");
        m.put("diesel_infused_blood", "dieselInfusedBloodTransparencyMultiplier");
        m.put("gasoline_infused_blood", "gasolineInfusedBloodTransparencyMultiplier");

        Map<String, String> trueMap = new HashMap<>();
        for (Map.Entry<String, String> entry : m.entrySet()) {
            trueMap.put("general."+entry.getKey(), "client."+entry.getValue());
        }
        return migrate(contents, trueMap);
    }

    private static class TomlGroup {
        private final Map<String, TomlGroup> subgroups = new HashMap<>();
        private final Map<String, String> entries = new HashMap<>();
        private final String path;

        private static TomlGroup root() {
            return new TomlGroup("");
        }

        private TomlGroup(String path) {
            this.path = path;
        }

        public boolean isRoot() {
            return path.isEmpty();
        }

        public void add(String key, String value) {
            if (!isRoot())
                throw new NotImplementedException();

            String[] pieces = key.split("\\.");
            String subKey = pieces[pieces.length - 1];
            TomlGroup targetedGroup = this;
            for (int i = 0; i < pieces.length - 1; i++) {
                targetedGroup = targetedGroup.getOrCreateSubGroup(pieces[i]);
            }
            targetedGroup.entries.put(subKey, value);
        }

        private TomlGroup getOrCreateSubGroup(String subKey) {
            return subgroups.computeIfAbsent(subKey, (sk) -> new TomlGroup(path.isEmpty() ? sk : path + "." + sk));
        }

        private void write(StringBuilder b) {
            if (!isRoot()) {
                b.append("\n[").append(path).append("]");
            }

            for (Map.Entry<String, String> entry : entries.entrySet()) {
                b.append("\n").append(entry.getKey()).append(" = ").append(entry.getValue());
            }

            for (TomlGroup subGroup : subgroups.values()) {
                subGroup.write(b);
            }
        }

        private String write() {
            StringBuilder b = new StringBuilder();
            b.append("# Automatically written by a converter");
            write(b);
            b.append("\n");
            return b.toString();
        }
    }

    @ApiStatus.Internal
    public static String migrate(String contents, Map<String, String> pathMap) {
        TomlGroup root = TomlGroup.root();
        String keyPrefix = "";
        List<String> lines = contents.lines().toList();
        for (String line : lines) {
            if (line.isEmpty())
                continue;
            int commentIdx = line.indexOf('#');
            if (commentIdx != -1)
                line = line.substring(0, commentIdx);
            line = line
                    .trim()
                    .replaceAll(" ", "");

            if (line.isEmpty())
                continue;

            if (line.startsWith("[") && line.endsWith("]")) {
                keyPrefix = line.substring(1, line.length() - 1) + ".";
            } else if (line.contains("=")) {
                String[] pieces = line.split("=", 2);
                String key = keyPrefix + pieces[0].trim();
                if (!pathMap.containsKey(key))
                    continue;
                root.add(pathMap.get(key), pieces[1].trim());
            }
        }

        return root.write();
    }
}
