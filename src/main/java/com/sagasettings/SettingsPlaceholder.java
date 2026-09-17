package com.sagasetting;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class SettingsPlaceholder extends PlaceholderExpansion {

    private final PlayerSettingsStore store;

    public SettingsPlaceholder(PlayerSettingsStore store) {
        this.store = store;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "sagasetting";
    }

    @Override
    public @NotNull String getAuthor() {
        return "SagaSMP";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null || !player.isOnline()) return "";
        Player p = player.getPlayer();
        if (p == null) return "";

        for (var entry : SettingsRegistry.SETTINGS.entrySet()) {
            for (SettingDef def : entry.getValue()) {
                if (def.key().equals(params)) {
                    return store.get(p.getUniqueId(), def) == 1 ? "true" : "false";
                }
            }
        }
        return null;
    }
}
