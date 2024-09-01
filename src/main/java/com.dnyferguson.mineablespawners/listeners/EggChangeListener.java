package com.dnyferguson.mineablespawners.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.dnyferguson.mineablespawners.MineableSpawners;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.annotation.Target;

public class EggChangeListener implements Listener {
    private final MineableSpawners plugin;

    public EggChangeListener(MineableSpawners plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEggChange(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Player player = e.getPlayer();
        ItemStack itemInHand = e.getItem();

        if (itemInHand == null || itemInHand.getType().equals(Material.AIR)) {
            return;
        }

        String itemName = itemInHand.getType().name();
        Material targetBlock = e.getClickedBlock().getType();

        if (!itemName.contains("SPAWN_EGG")) {
            return;
        }


        if (targetBlock==XMaterial.SPAWNER.parseMaterial())  {
            e.setCancelled(true);
            e.setUseItemInHand(Event.Result.DENY);
            e.setUseInteractedBlock(Event.Result.DENY);
        } else   if (targetBlock==Material.TRIAL_SPAWNER)  {
            e.setCancelled(true);
            e.setUseItemInHand(Event.Result.DENY);
            e.setUseInteractedBlock(Event.Result.DENY);
        }
    }
}
