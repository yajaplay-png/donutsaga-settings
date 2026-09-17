package com.sagasetting;

import org.bukkit.entity.Player;

public interface SettingChangeHook {
    void onChange(Player player, String category, String key, int newIndex);
}
