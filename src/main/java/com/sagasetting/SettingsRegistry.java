package com.sagasetting;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class SettingsRegistry {

    public static final LinkedHashMap<String, String> CATEGORIES = new LinkedHashMap<>();
    static {
        CATEGORIES.put("chat", "Chat");
        CATEGORIES.put("notifications", "Notifications");
        CATEGORIES.put("pvp", "PvP");
        CATEGORIES.put("visuals", "Visuals");
        CATEGORIES.put("privacy", "Privacy");
        CATEGORIES.put("scoreboard", "Scoreboard");
        CATEGORIES.put("general", "General");
    }

    public static final Map<String, List<SettingDef>> SETTINGS = new LinkedHashMap<>();
    static {
        SETTINGS.put("chat", List.of(
                SettingDef.onOff("public_chat", "Public Chat", true),
                SettingDef.anyoneFriendsOff("private_messages", "Private Messages", 1),
                SettingDef.onOff("server_chat_messages", "Server Chat Messages", true),
                SettingDef.onOff("server_hotbar_messages", "Server Hotbar Messages", true),
                SettingDef.anyoneFriendsOff("death_messages", "Death Messages", 1),
                SettingDef.anyoneFriendsOff("advancement_messages", "Advancement Messages", 1),
                SettingDef.onOff("join_leave_messages", "Join/Leave Messages", false)
        ));

        SETTINGS.put("notifications", List.of(
                SettingDef.onOff("pay_alerts", "Pay Alerts", true),
                SettingDef.onOff("teleport_alerts", "Teleport Alerts", true),
                SettingDef.onOff("bounty_alerts", "Bounty Alerts", true),
                SettingDef.onOffLinked("auction_alerts", "Auction Alerts", true,
                        "ah alert off", "ah alert on"),
                SettingDef.onOffToggleCommand("order_alerts", "Order Alerts", true,
                        "toggleshopmessages"),
                SettingDef.onOff("server_sounds", "Server Sounds", true),
                SettingDef.onOff("follow_alerts", "Follow Alerts", true)
        ));

        SETTINGS.put("pvp", List.of(
                SettingDef.onOff("fast_crystals", "Fast Crystals", true),
                SettingDef.onOff("totem_particles", "Totem Particles", true),
                SettingDef.onOff("explosion_particles", "Explosion Particles", true),
                SettingDef.onOff("explosion_sounds", "Explosion Sounds", true),
                SettingDef.onOff("combat_timer", "Combat Timer", true)
        ));

        SETTINGS.put("visuals", List.of(
                SettingDef.onOff("display_donut_plus", "Display Donut+", true),
                SettingDef.onOff("money_nametags", "Money Nametags", true),
                SettingDef.onOffToggleCommand("item_worth_lore", "Item Worth Lore", true,
                        "worthtoggle"),
                SettingDef.onOff("teleport_confirm_menus", "Teleport Confirm Menus", true)
        ));

        SETTINGS.put("privacy", List.of(
                SettingDef.anyoneFriendsOff("teleport_requests", "Teleport Requests", 0),
                SettingDef.anyoneFriendsOff("teleport_here_requests", "Teleport-Here Requests", 0),
                SettingDef.anyoneFriendsOff("allow_payments", "Allow Payments", 0),
                SettingDef.onOff("randomized_coords", "Randomized Coords", false),
                SettingDef.onOff("private_transactions", "Private Transactions", false)
        ));

        SETTINGS.put("scoreboard", List.of(
                SettingDef.onOff("scoreboard", "Scoreboard", true),
                SettingDef.onOff("show_money", "Show Money", true),
                SettingDef.onOff("show_shards", "Show Shards", true),
                SettingDef.onOff("show_kills", "Show Kills", true),
                SettingDef.onOff("show_deaths", "Show Deaths", true),
                SettingDef.onOff("show_playtime", "Show Playtime", true)
        ));

        SETTINGS.put("general", List.of(
                SettingDef.onOff("auction_quick_buy", "Auction Quick Buy", false),
                SettingDef.onOff("auction_quick_sell", "Auction Quick Sell", false),
                SettingDef.onOff("mob_spawns", "Mob Spawns", true),
                SettingDef.onOff("phantom_spawning", "Phantom Spawning", true),
                SettingDef.onOff("night_vision", "Night Vision", true),
                SettingDef.onOff("destroy_pearl_on_death", "Destroy Pearl on Death", false)
        ));
    }

    private SettingsRegistry() {
    }
}
