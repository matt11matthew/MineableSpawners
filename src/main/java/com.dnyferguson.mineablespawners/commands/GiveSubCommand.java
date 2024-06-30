package com.dnyferguson.mineablespawners.commands;

import com.cryptomorin.xseries.XMaterial;
import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.data.NewConfig;
import com.dnyferguson.mineablespawners.utils.Chat;
import com.dnyferguson.mineablespawners.utils.Utils;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class GiveSubCommand {

    public GiveSubCommand() {}

    public void execute(MineableSpawners plugin, CommandSender sender, String target, String type, String amt) {
        Player targetPlayer = Bukkit.getPlayer(target);
        if (target == null || targetPlayer == null) {
            plugin.getConfigurationHandler().sendMessage("give", "player-does-not-exist", sender);
            return;
        }

        EntityType entityType;
        try {
            entityType = EntityType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getConfigurationHandler().sendMessage("give", "invalid-type", sender);
            return;
        }

        int amount = 0;
        try {
            amount = Integer.parseInt(amt);
        } catch (NumberFormatException e) {
            plugin.getConfigurationHandler().sendMessage("give", "invalid-amount", sender);
            return;
        }

        ItemStack item = new ItemStack(XMaterial.SPAWNER.parseMaterial());
        ItemMeta meta = item.getItemMeta();
        item.setAmount(amount);

        String mobFormatted = Chat.uppercaseStartingLetters(entityType.name());
        boolean excluded = false;
        for (String s : MineableSpawners.getNewC().EXCLUDED) {
            if (s.equalsIgnoreCase(entityType.toString())) {
                excluded=true;
                break;
            }
        }
        meta.setDisplayName(Chat.format(plugin.getConfigurationHandler().getMessage("global", "name").replace("%mob%", mobFormatted)));
        List<String> newLore = new ArrayList<>();
        if (plugin.getConfigurationHandler().getList("global", "lore") != null && plugin.getConfigurationHandler().getBoolean("global", "lore-enabled")) {
            for (String line : plugin.getConfigurationHandler().getList("global", "lore")) {
                if (line.toLowerCase().contains("%owner%") && excluded)continue;
                newLore.add(Chat.format(line.replace("%owner%", targetPlayer.getName())).replace("%mob%", mobFormatted));
            }
            meta.setLore(newLore);
        }
        item.setItemMeta(meta);

        NBTItem nbti = new NBTItem(item);
        nbti.setString("ms_mob", entityType.name());
        if (!excluded) {
            nbti.setString("ms_owner", targetPlayer.getUniqueId().toString());

        }

        item = nbti.getItem();
        Utils.addItemToInventory(targetPlayer, item, mobFormatted);

        sender.sendMessage(plugin.getConfigurationHandler().getMessage("give", "success").replace("%mob%", mobFormatted).replace("%target%", targetPlayer.getName()).replace("%amount%", amount + ""));
        targetPlayer.sendMessage(plugin.getConfigurationHandler().getMessage("give", "received").replace("%mob%", mobFormatted).replace("%amount%", amount + ""));

        /*if (targetPlayer.getInventory().firstEmpty() == -1) {
            if (!plugin.getConfigurationHandler().getBooleanOrDefault("give", "drop-if-full", true)) {
                plugin.getConfigurationHandler().sendMessage("give", "inventory-full", sender);
                return;
            }

            plugin.getLogger().log(Level.INFO, "Dropped " + amount + "x " + mobFormatted + " Spawners at " + targetPlayer.getName() + "'s feet since their inventory was full!");
            targetPlayer.getWorld().dropItem(targetPlayer.getLocation(), item);

            return;
        }

        targetPlayer.getInventory().addItem(item);
        plugin.getConfigurationHandler().getMessage("give", "success").replace("%mob%", mobFormatted).replace("%target%", targetPlayer.getName()).replace("%amount%", amount + "");
        targetPlayer.sendMessage(plugin.getConfigurationHandler().getMessage("give", "received").replace("%mob%", mobFormatted).replace("%target%", targetPlayer.getName()).replace("%amount%", amount + ""));*/
    }
}
