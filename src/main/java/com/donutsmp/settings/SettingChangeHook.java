package com.donutsmp.settings;

import org.bukkit.entity.Player;

public interface SettingChangeHook {
    void onChange(Player player, String category, String key, int newIndex);
}
