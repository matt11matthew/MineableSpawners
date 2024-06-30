package com.dnyferguson.mineablespawners.data;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Matthew E on 6/30/2024 at 3:51 PM for the project MineableSpawners
 */
public class SpawnerOwnerListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Item item = event.getItem();
        if (item.getItemStack().getType()!= XMaterial.SPAWNER.parseMaterial())return;
        ItemStack itemStack = item.getItemStack();
        NBTItem nbtItem = new NBTItem(itemStack);
        if (!nbtItem.hasTag("ms_owner")) {
            return;
        }

    }
}
