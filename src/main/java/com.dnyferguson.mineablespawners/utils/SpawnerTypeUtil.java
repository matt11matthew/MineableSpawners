package com.dnyferguson.mineablespawners.utils;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class SpawnerTypeUtil {
    public static ItemStack createSpawnerItem() {
        ItemStack spawnerItem = new ItemStack(Material.SPAWNER);
        NBTItem nbtItem = new NBTItem(spawnerItem);

        nbtItem.addCompound("BlockEntityTag").setString("EntityId", "minecraft:zombie");

        return nbtItem.getItem();
    }
    public static EntityType getSpawnerType(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() !=    XMaterial.SPAWNER.parseMaterial()) {
            return null;
        }


        NBTItem nbtItem =new NBTItem(itemStack);

        if (nbtItem.hasKey("BlockEntityTag")) {
            NBTCompound blockEntityTag = nbtItem.getCompound("BlockEntityTag");

            if (blockEntityTag.hasTag("EntityId")) {
                String entityTypeName = blockEntityTag.getString("EntityId");
                try {
                    return EntityType.valueOf(entityTypeName.toUpperCase());
                } catch (IllegalArgumentException e) {
                    // Handle case where EntityType is not found
                    e.printStackTrace();
                    return null;
                }
            }
        }


        return null;
    }
}