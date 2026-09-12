package com.donutsmp.settings;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory storage of each player's chosen option index per setting key.
 * Swap this out for a database/YAML-backed implementation if you want
 * settings to persist across restarts — the dialog code doesn't care
 * how this is implemented, only that get/set/cycle work.
 */
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
