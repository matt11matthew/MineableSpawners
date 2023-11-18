package com.dnyferguson.mineablespawners.utils;

import com.dnyferguson.mineablespawners.MineableSpawners;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.logging.Level;

public class Utils {

    private static MineableSpawners plugin = MineableSpawners.getPlugin();

    public static void addItemToInventory(Player player, ItemStack itemToAdd, String mobFormatted) {
        HashMap<Integer, ItemStack> leftOver = player.getInventory().addItem(itemToAdd);

        if (!leftOver.isEmpty()) {
            for (ItemStack item : leftOver.values()) {
                if (item.getAmount() <= item.getMaxStackSize()) {
                    player.getWorld().dropItem(player.getLocation(), itemToAdd);

                } else {
                    int stacks = (int) Math.floor((double) item.getAmount() / item.getMaxStackSize());
                    int singles = item.getAmount() - (stacks * item.getMaxStackSize());

                    ItemStack clone = item.clone();
                    clone.setAmount(item.getMaxStackSize());
                    for (int i = 0; i < stacks; i++) {
                        player.getWorld().dropItem(player.getLocation(), clone);
                    }

                    clone.setAmount(singles);
                    if (singles == 0) continue;

                    player.getWorld().dropItem(player.getLocation(), clone);
                }

                // 2 ticks later to ensure this is the very last message seen
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin.getConfigurationHandler().sendMessage("give", "inventory-full", player);
                        plugin.getLogger().log(Level.INFO, "Dropped " + item.getAmount() + "x " + mobFormatted + " spawners at " + player.getName() + "'s feet since their inventory was full");
                    }
                }.runTaskLater(plugin, 2l);
            }
        }
    }
}
