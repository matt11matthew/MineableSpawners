package com.dnyferguson.mineablespawners.data;

import com.cryptomorin.xseries.XMaterial;
import com.destroystokyo.paper.event.entity.PreSpawnerSpawnEvent;
import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.api.API;
import com.dnyferguson.mineablespawners.utils.SpawnerTypeUtil;
import com.google.gson.JsonParseException;
import de.tr7zw.changeme.nbtapi.NBTItem;
import fr.maxlego08.zauctionhouse.api.event.events.AuctionOpenEvent;
import fr.maxlego08.zauctionhouse.api.event.events.AuctionPreSellEvent;
import me.matthewedevelopment.atheriallib.utilities.AtherialTasks;
import me.matthewedevelopment.atheriallib.utilities.number.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.spawner.Spawner;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
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
    public void onPlayerJoin(PlayerJoinEvent event) {
        fixSpawners(event.getPlayer());
    }

    private void fixSpawners(Player player) {

        Map<Integer,ItemStack> fixedMap= new HashMap<>();
        for (int i = 0; i < player.getInventory().getContents().length; i++) {
            ItemStack stack =  player.getInventory().getContents()[i];
            if (stack==null)continue;
            if (stack.getType()!=XMaterial.SPAWNER.parseMaterial()) continue;

            NBTItem nbtItem =new NBTItem(stack);
            if (!nbtItem.hasTag("ms_mob")){
                EntityType entityTypeFromItemStack = MineableSpawners.getApi().getEntityTypeFromItemStack(stack);
                if (entityTypeFromItemStack==null){

                    player.sendMessage(ChatColor.RED+"You have a broken spawners please message an admin and tell them to report this to ");

                } else {
                    if (player.isOp()) {
                        player.sendMessage(ChatColor.GREEN +"(1) Fixed spawner " + i +" this message is only seen by OPs");
                    }
                    ItemStack itemStack = MineableSpawners.getApi()
                            .getSpawnerFromEntityType(entityTypeFromItemStack, player.getUniqueId());
                    itemStack.setAmount(stack.getAmount());
                    fixedMap.put(i,
                            itemStack);
                }

            } else {
                if (nbtItem.hasTag("ms_owner")){
                    String msOwner = nbtItem.getString("ms_owner");
                    if (!msOwner.equals(player.getUniqueId().toString())){
                        continue;
                    }
                }
                if (player.isOp()) {
                    player.sendMessage(ChatColor.GREEN +"(2) Fixed spawner " + i +" this message is only seen by OPs");
                }
                ItemStack itemStack = MineableSpawners.getApi()
                        .getSpawnerFromEntityType(EntityType.valueOf(nbtItem.getString("ms_mob")), player.getUniqueId());
                itemStack.setAmount(stack.getAmount());
                fixedMap.put(i,
                        itemStack);
            }
        }
        fixedMap.forEach((integer, itemStack) -> player.getInventory().setItem(integer, itemStack));
        player.updateInventory();
    }

//    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Item item = event.getItem();
        if (item.getItemStack().getType()!= XMaterial.SPAWNER.parseMaterial())return;
        ItemStack itemStack = item.getItemStack();
        NBTItem nbtItem = new NBTItem(itemStack);

        Player p = event
                .getPlayer();
        if (!nbtItem.hasTag("ms_owner")) {

            if (!nbtItem.hasTag("ms_mob")){
                return;
            }
            String msMobType=nbtItem.getString("ms_mob");
            int amt = itemStack.getAmount();
            boolean msMob = NewConfig.get().EXCLUDED.contains(msMobType);
            if (!msMob) {
                return;
            }

            ItemStack msMob1 = MineableSpawners.getApi().getSpawnerFromEntityType(EntityType.valueOf(msMobType), p.getUniqueId());

            msMob1.setAmount(amt);
            event.getItem().setItemStack(msMob1);
            return;
        }
        if (p.hasPermission("mineablespawners.bypass"))return;

        UUID uuid = UUID.fromString(nbtItem.getString("ms_owner"));
        if (p.getUniqueId().equals(uuid)){

            return;
        }

        event.setCancelled(true);

    }


//    @EventHandler
//    public void onPreSpawnerSpawn(PreSpawnerSpawnEvent event) {
//        Spawner  spawner = (Spawner) event.getSpawnerLocation().getBlock().getState();
//        Bukkit.getServer().broadcastMessage(ChatColor.RED+""+spawner.getDelay());
//    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPostSpawn(SpawnerSpawnEvent event) {
        NewConfig c = NewConfig.get();
        if (event.getSpawner() == null) return;

        event.getSpawner().resetTimer(); // Reset the timer after the spawn event
        AtherialTasks.runIn(() -> {

            if (Bukkit.getServer().getOnlinePlayers().size() < c.onlineAmt) {
                return;
            }

            int delay = event.getSpawner().getDelay();
            if (delay == 0) {

                return;
            }


            // Get the percentage from config
            double number = NumberUtils.getNumber(new String(c.reduceRate).replace("%", "").trim());

            // Correct the multiplier to increase the delay
            double multi = 1 + (number / 100.0D); // Increase delay by the percentage


            // Apply the new delay by increasing the current delay
            double increasedDelay = (double) delay * multi;


            // Set the new delay (casting back to int if needed)
            event.getSpawner().setDelay((int) increasedDelay);

        }, 2L);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpawnerSpawn(SpawnerSpawnEvent event) {
        Location location = event.getSpawner().getLocation();

        NewConfig c = NewConfig.get();
        MSpawnerRegistry spawnerRegistry = MSpawnerRegistry.get();
        if (event.getSpawner().getSpawnedEntity()!=null) {

            boolean soulbound = c.EXCLUDED.contains(event.getSpawner().getSpawnedEntity().getEntityType().name());
            if (soulbound) {
                if (!spawnerRegistry.isSpawner(location)) {
                    event.setCancelled(true);
                    return;
                }

            }
        }

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
//            Bukkit.getServer(). broadcastMessage("owner not found CANCEL");
            return;
        }

        if (!ownerPlayer.getWorld().getName().equals(location.getWorld().getName())){

            return;
        }
        if (ownerPlayer.getLocation().distance(location) > c.SPAWN_DISTANCE) {
            event.setCancelled(true);
            return;
        }

    }

    @EventHandler
    public void onAuctionPreSell(AuctionPreSellEvent event) {
        ItemStack itemStack = event.getItemStack();

        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasTag("ms_owner")){
            event.setCancelled(true);
            NewConfig.get().CANT_LIST_SOUL_BOUND.send(event.getPlayer());

        }
    }
}
