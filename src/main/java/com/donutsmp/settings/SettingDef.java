package com.donutsmp.settings;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;

/**
 * Defines one toggleable/cyclable row in a settings category.
 * A plain ON/OFF switch is just a SettingDef with 2 options.
 * A row like "Private Messages: Anyone / Friends-Followed / Off" is 3 options.
 */
public record SettingDef(String key, String label, List<Option> options, int defaultIndex) {

    public record Option(String text, TextColor color) {
    }

    public static SettingDef onOff(String key, String label, boolean defaultOn) {
        return new SettingDef(key, label, List.of(
                new Option("OFF", NamedTextColor.RED),
                new Option("ON", NamedTextColor.GREEN)
        ), defaultOn ? 1 : 0);
    }

    public static SettingDef anyoneFriendsOff(String key, String label, int defaultIndex) {
        return new SettingDef(key, label, List.of(
                new Option("Anyone", NamedTextColor.GREEN),
                new Option("Friends/Followed", NamedTextColor.YELLOW),
                new Option("Off", NamedTextColor.RED)
        ), defaultIndex);
    }
}
