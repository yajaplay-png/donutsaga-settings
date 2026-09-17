package com.donutsmp.settings;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class SettingsPlugin extends JavaPlugin {

    private final PlayerSettingsStore store = new PlayerSettingsStore();
    private SettingsMenu menu;
    private SettingsTABHook tabHook;

    @Override
    public void onEnable() {
        GeneralFeatureListener generalFeatures = new GeneralFeatureListener(store);
        getServer().getPluginManager().registerEvents(generalFeatures, this);

        this.tabHook = new SettingsTABHook(store, this);

        this.menu = new SettingsMenu(store, generalFeatures, tabHook);

        getServer().getPluginManager().registerEvents(new org.bukkit.event.Listener() {
            @org.bukkit.event.EventHandler
            public void onJoin(org.bukkit.event.player.PlayerJoinEvent e) {
                tabHook.syncOnJoin(e.getPlayer());
            }
            @org.bukkit.event.EventHandler
            public void onQuit(org.bukkit.event.player.PlayerQuitEvent e) {
                tabHook.onQuit(e.getPlayer());
            }
        }, this);

        getLogger().info("SagaSetting enabled — /settings is ready.");

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new SettingsPlaceholder(store).register();
            getLogger().info("PlaceholderAPI expansion 'sagasetting' registered.");
        } else {
            getLogger().warning("PlaceholderAPI not found — scoreboard placeholders disabled.");
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                              @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can open the settings menu.");
            return true;
        }
        menu.openMain(player);
        return true;
    }
}
