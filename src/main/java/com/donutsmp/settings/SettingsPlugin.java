package com.donutsmp.settings;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class SettingsPlugin extends JavaPlugin {

    private final PlayerSettingsStore store = new PlayerSettingsStore();
    private SettingsMenu menu;

    @Override
    public void onEnable() {
        GeneralFeatureListener generalFeatures = new GeneralFeatureListener(store);
        getServer().getPluginManager().registerEvents(generalFeatures, this);

        this.menu = new SettingsMenu(store, generalFeatures);
        getLogger().info("DonutSettings enabled — /settings is ready.");
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
