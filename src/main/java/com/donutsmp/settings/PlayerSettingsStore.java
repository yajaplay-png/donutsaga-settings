package com.donutsmp.settings;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PlayerSettingsStore {

    private final Map<UUID, Map<String, Integer>> data = new ConcurrentHashMap<>();

    public int get(UUID player, SettingDef def) {
        return data
                .computeIfAbsent(player, k -> new ConcurrentHashMap<>())
                .getOrDefault(def.key(), def.defaultIndex());
    }

    public void cycle(UUID player, SettingDef def) {
        Map<String, Integer> playerData = data.computeIfAbsent(player, k -> new ConcurrentHashMap<>());
        int current = playerData.getOrDefault(def.key(), def.defaultIndex());
        List<SettingDef.Option> options = def.options();
        int next = (current + 1) % options.size();
        playerData.put(def.key(), next);
    }
}
