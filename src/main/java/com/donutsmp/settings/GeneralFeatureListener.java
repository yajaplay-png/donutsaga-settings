package com.sagasetting;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public final class GeneralFeatureListener implements Listener, SettingChangeHook {

    private static final double NEARBY_RADIUS = 32.0;

    private final PlayerSettingsStore store;
    private final SettingDef mobSpawnsDef;
    private final SettingDef phantomSpawningDef;
    private final SettingDef nightVisionDef;
    private final SettingDef destroyPearlDef;

    public GeneralFeatureListener(PlayerSettingsStore store) {
        this.store = store;
        List<SettingDef> general = SettingsRegistry.SETTINGS.get("general");
        this.mobSpawnsDef = find(general, "mob_spawns");
        this.phantomSpawningDef = find(general, "phantom_spawning");
        this.nightVisionDef = find(general, "night_vision");
        this.destroyPearlDef = find(general, "destroy_pearl_on_death");
    }

    private static SettingDef find(List<SettingDef> defs, String key) {
        for (SettingDef d : defs) {
            if (d.key().equals(key)) return d;
        }
        throw new IllegalStateException("Missing setting definition: " + key);
    }

    private boolean isOn(Player player, SettingDef def) {
        return store.get(player.getUniqueId(), def) == 1;
    }

    @Override
    public void onChange(Player player, String category, String key, int newIndex) {
        if (!"general".equals(category)) return;
        if ("night_vision".equals(key)) {
            applyNightVision(player);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        applyNightVision(event.getPlayer());
    }

    private void applyNightVision(Player player) {
        if (isOn(player, nightVisionDef)) {
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();
        if (reason != CreatureSpawnEvent.SpawnReason.NATURAL
                && reason != CreatureSpawnEvent.SpawnReason.DEFAULT) {
            return;
        }

        Player nearest = findNearestPlayer(event.getLocation());
        if (nearest == null) return;

        LivingEntity entity = event.getEntity();

        if (entity instanceof Phantom) {
            if (!isOn(nearest, phantomSpawningDef)) {
                event.setCancelled(true);
            }
            return;
        }

        if (entity instanceof Monster) {
            if (!isOn(nearest, mobSpawnsDef)) {
                event.setCancelled(true);
            }
        }
    }

    private Player findNearestPlayer(Location location) {
        if (location.getWorld() == null) return null;
        return location.getWorld().getPlayers().stream()
                .filter(p -> p.getLocation().distanceSquared(location) <= NEARBY_RADIUS * NEARBY_RADIUS)
                .min(Comparator.comparingDouble(p -> p.getLocation().distanceSquared(location)))
                .orElse(null);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!isOn(player, destroyPearlDef)) return;

        Iterator<ItemStack> it = event.getDrops().iterator();
        while (it.hasNext()) {
            ItemStack stack = it.next();
            if (stack != null && stack.getType() == Material.ENDER_PEARL) {
                it.remove();
            }
        }
    }
}
