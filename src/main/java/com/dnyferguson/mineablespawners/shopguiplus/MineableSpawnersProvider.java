package com.dnyferguson.mineablespawners.shopguiplus;

import com.dnyferguson.mineablespawners.MineableSpawners;
import net.brcdev.shopgui.spawner.external.provider.ExternalSpawnerProvider;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class MineableSpawnersProvider implements ExternalSpawnerProvider {
  public String getName() {
    return "MineableSpawners";
  }
  
  public ItemStack getSpawnerItem(EntityType entityType) {
    return MineableSpawners.getApi().getSpawnerFromEntityType(entityType, null);
  }
  
  public EntityType getSpawnerEntityType(ItemStack itemStack) {
    return MineableSpawners.getApi().getEntityTypeFromItemStack(itemStack);
  }
}
