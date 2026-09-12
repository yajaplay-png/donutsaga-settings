package com.donutsmp.settings;

import org.bukkit.entity.Player;

/**
 * Implement this to make a settings toggle DO something server-side
 * (as opposed to settings that are purely cosmetic or linked to another
 * plugin's own command via SettingDef.onOffLinked / onOffToggleCommand).
 */
public interface SettingChangeHook {
    void onChange(Player player, String category, String key, int newIndex);
}
