package com.donutsmp.settings;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;

public record SettingDef(String key, String label, List<Option> options, int defaultIndex,
                          List<String> commandsPerOption) {

    public record Option(String text, TextColor color) {
    }

    public SettingDef(String key, String label, List<Option> options, int defaultIndex) {
        this(key, label, options, defaultIndex, null);
    }

    public boolean hasCommand(int index) {
        return commandsPerOption != null && commandsPerOption.get(index) != null;
    }

    public String commandFor(int index) {
        return commandsPerOption.get(index);
    }

    public static SettingDef onOff(String key, String label, boolean defaultOn) {
        return new SettingDef(key, label, List.of(
                new Option("OFF", NamedTextColor.RED),
                new Option("ON", NamedTextColor.GREEN)
        ), defaultOn ? 1 : 0);
    }

    public static SettingDef onOffLinked(String key, String label, boolean defaultOn,
                                          String commandOff, String commandOn) {
        return new SettingDef(key, label, List.of(
                new Option("OFF", NamedTextColor.RED),
                new Option("ON", NamedTextColor.GREEN)
        ), defaultOn ? 1 : 0, List.of(commandOff, commandOn));
    }

    public static SettingDef onOffToggleCommand(String key, String label, boolean defaultOn, String toggleCommand) {
        return new SettingDef(key, label, List.of(
                new Option("OFF", NamedTextColor.RED),
                new Option("ON", NamedTextColor.GREEN)
        ), defaultOn ? 1 : 0, List.of(toggleCommand, toggleCommand));
    }

    public static SettingDef anyoneFriendsOff(String key, String label, int defaultIndex) {
        return new SettingDef(key, label, List.of(
                new Option("Anyone", NamedTextColor.GREEN),
                new Option("Friends/Followed", NamedTextColor.YELLOW),
                new Option("Off", NamedTextColor.RED)
        ), defaultIndex, null);
    }
}
