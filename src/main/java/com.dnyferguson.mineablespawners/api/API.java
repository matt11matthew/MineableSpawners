package com.dnyferguson.mineablespawners.api;

import com.cryptomorin.xseries.XMaterial;
import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class API {
    private final MineableSpawners plugin;

    public API(MineableSpawners plugin) {
        this.plugin = plugin;
    }

    public UUID getOwnerFromItemStack(ItemStack item) {
        NBTItem nbti = new NBTItem(item);
        return nbti.hasKey("ms_owner") ? UUID.fromString(nbti.getString("ms_owner")) : null;
    }
    public EntityType getEntityTypeFromItemStack(ItemStack item) {
        EntityType entityType = null;

        // v3 compatibility
        try {
            NBTItem nbti = new NBTItem(item);
            if (nbti.hasKey("ms_mob")) {
                entityType = EntityType.valueOf(nbti.getString("ms_mob"));
                return entityType;
            }
        } catch (Exception ignore) {}

        if (plugin.getConfigurationHandler().getBoolean("global", "backwards-compatibility")) {
            // v2 compatibility
            try {
                entityType = EntityType.valueOf(ChatColor.stripColor(item.getItemMeta().getDisplayName()).split(" Spawner")[0].replace("[", "").replace(" ", "_").toUpperCase());
                return entityType;
            } catch (Exception ignore) {}

            // v1 compatibility
            try {
                entityType = EntityType.valueOf(item.getItemMeta().getLore().toString().split(": §7")[1].split("]")[0].toUpperCase());
            } catch (Exception ignore) {}
        }

        return entityType;
    }

//    public ItemStack getSpawnerFromEntityName(String entityName) {[
//        EntityType entityType = EntityType.valueOf(entityName.toUpperCase().replace(" ","_"));
//        return getSpawnerFromEntityType(entityType);
//    }

    public ItemStack getSpawnerFromEntityType(EntityType entityType, UUID owner) {
        ItemStack item = new ItemStack(Objects.requireNonNull(XMaterial.SPAWNER.parseMaterial()));
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.values());
        String mobFormatted = Chat.uppercaseStartingLetters(entityType.name().toString());
        meta.setDisplayName(plugin.getConfigurationHandler().getMessage("global", "name").replace("%mob%", mobFormatted));

        item.setItemMeta(meta);


        boolean soundbound = MineableSpawners.getNewC().EXCLUDED.contains(entityType.name());
//        Bukkit.getServer().broadcastMessage(soundbound+"");

        NBTItem nbti = new NBTItem(item);
        nbti.setString("ms_mob", entityType.name());
        if (soundbound && owner!=null) {
            nbti.setString("ms_owner", owner.toString());

        }

        item = nbti.getItem();
        meta = item.getItemMeta();

        List<String> newLore = new ArrayList<>();
        if (plugin.getConfigurationHandler().getList("global", "lore") != null && plugin.getConfigurationHandler().getBoolean("global", "lore-enabled")) {
            for (String line : plugin.getConfigurationHandler().getList("global", "lore")) {
                if (line.toLowerCase().contains("%owner%")){

                    if (owner==null) {
//                        Bukkit.getServer().broadcastMessage("NULL OWNER");
                    } else {
//                        Bukkit.getServer().broadcastMessage("NOT NULL OWNER");
                        if (soundbound) {
//                            Bukkit.getServer().broadcastMessage(line);
//                            Bukkit.getServer().broadcastMessage("-------------------");
//                            Bukkit.getServer().broadcastMessage("SB");

                            newLore.add(Chat.format(line).replace("%owner%",getUsername(owner)).replace("%mob%", mobFormatted));
//                            Bukkit.getServer().broadcastMessage("-------------------");
                        }
                    }
                } else {
//                    Bukkit.getServer().broadcastMessage("OTHER LINE " + line);

                    newLore.add(Chat.format(line).replace("%mob%", mobFormatted));
                }
            }
            meta.setLore(newLore);
        }

        item.setItemMeta(meta);
        return item;
    }

    private String getUsername(UUID owner) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(owner);
        if (offlinePlayer==null)return "ERROR";
        if (!offlinePlayer.hasPlayedBefore())return "ERROR";
        return offlinePlayer.getName();
    }
}
