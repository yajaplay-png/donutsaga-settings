package com.sagasetting;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class SettingsTABHook implements SettingChangeHook {

    private final PlayerSettingsStore store;
    private final JavaPlugin plugin;
    private final Map<UUID, Boolean> tabScoreboardOn = new ConcurrentHashMap<>();

    public SettingsTABHook(PlayerSettingsStore store, JavaPlugin plugin) {
        this.store = store;
        this.plugin = plugin;
    }

    @Override
    public void onChange(Player player, String category, String key, int newIndex) {
        if (!"scoreboard".equals(category) || !"scoreboard".equals(key)) return;
        boolean wantOn = newIndex == 1;
        boolean currentOn = tabScoreboardOn.getOrDefault(player.getUniqueId(), true);
        if (wantOn != currentOn) {
            player.performCommand("tab scoreboard toggle");
            tabScoreboardOn.put(player.getUniqueId(), wantOn);
        }
    }

    public void syncOnJoin(Player player) {
        SettingDef sbDef = findScoreboardDef();
        if (sbDef == null) return;
        boolean wantOn = store.get(player.getUniqueId(), sbDef) == 1;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline()) return;
            boolean currentOn = tabScoreboardOn.getOrDefault(player.getUniqueId(), true);
            if (wantOn != currentOn) {
                player.performCommand("tab scoreboard toggle");
            }
            tabScoreboardOn.put(player.getUniqueId(), wantOn);
        }, 40L);
    }

    public void onQuit(Player player) {
        tabScoreboardOn.remove(player.getUniqueId());
    }

    private SettingDef findScoreboardDef() {
        var list = SettingsRegistry.SETTINGS.get("scoreboard");
        if (list == null) return null;
        for (SettingDef d : list) {
            if ("scoreboard".equals(d.key())) return d;
        }
        return null;
    }
}
