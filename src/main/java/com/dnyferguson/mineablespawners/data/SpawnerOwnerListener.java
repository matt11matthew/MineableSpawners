package com.dnyferguson.mineablespawners.data;

import com.cryptomorin.xseries.XMaterial;
import com.dnyferguson.mineablespawners.MineableSpawners;
import com.google.gson.JsonParseException;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created by Matthew E on 6/30/2024 at 3:51 PM for the project MineableSpawners
 */
public class SpawnerOwnerListener implements Listener {
    private final MineableSpawners mineableSpawners;

    public SpawnerOwnerListener(MineableSpawners mineableSpawners) {

        this.mineableSpawners = mineableSpawners;
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Item item = event.getItem();
        if (item.getItemStack().getType()!= XMaterial.SPAWNER.parseMaterial())return;
        ItemStack itemStack = item.getItemStack();
        NBTItem nbtItem = new NBTItem(itemStack);
        if (!nbtItem.hasTag("ms_owner")) {
            return;
        }
        UUID uuid = UUID.fromString(nbtItem.getString("ms_owner"));
        if (event.getPlayer().getUniqueId().equals(uuid)){
            return;
        }

        event.setCancelled(true);

    }

    @EventHandler
    public void onSpawnerSpawn(SpawnerSpawnEvent event) {
        Location location = event.getSpawner().getLocation();

        MSpawnerRegistry spawnerRegistry = MSpawnerRegistry.get();
        if (!spawnerRegistry.isSpawner(location))return;


        MSpawner spawner = spawnerRegistry.getSpawner(location);

        Player ownerPlayer = null;
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            if (spawner.getOwner().equals(onlinePlayer.getUniqueId())){
                ownerPlayer=onlinePlayer;
                break;
            }
        }
        if (ownerPlayer==null) {
            event.setCancelled(true);
            Bukkit.getServer(). broadcastMessage("owner not found CANCEL");
            return;
        }

        if (ownerPlayer.getLocation().distance(location) > NewConfig.get().SPAWN_DISTANCE) {
            event.setCancelled(true);
            Bukkit.getServer(). broadcastMessage("CANCEL");
        }
    }
}
