package com.dnyferguson.mineablespawners.data;

import com.dnyferguson.mineablespawners.MineableSpawners;
import me.matthewedevelopment.atheriallib.database.registry.DataObjectRegistry;
import me.matthewedevelopment.atheriallib.utilities.AtherialTasks;
import me.matthewedevelopment.atheriallib.utilities.location.AtherialLocation;
import me.matthewedevelopment.atheriallib.utilities.location.AtherialXYZLocation;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Matthew E on 6/27/2024 at 3:30 PM for the project MineableSpawners
 */
public class MSpawnerRegistry  extends DataObjectRegistry<MSpawner> {
    private Map<AtherialXYZLocation, UUID> spawnerLocationMap;

    public MSpawnerRegistry() {
        super(MSpawner.class);
        this. spawnerLocationMap=new HashMap<>();
    }

    public void insertNewSpawner(Location loc, Player owner, EntityType type) {
        AtherialXYZLocation atherialXYZLocation = AtherialXYZLocation.fromLocation(loc, true);
        MSpawner mSpawner =new MSpawner(UUID.randomUUID(),atherialXYZLocation,type.toString(), owner.getUniqueId());
        spawnerLocationMap.put(atherialXYZLocation,mSpawner.getUuid());
        insertAsync(mSpawner,() -> {});
    }

    public boolean deleteSpawner(Location loc) {
        if (!isSpawner(loc)) {
            return false;
        }
        MSpawner spawner = getSpawner(loc);
        if (spawner==null)return false;
        AtherialXYZLocation atherialXYZLocation = AtherialXYZLocation.fromLocation(loc, true);
        spawnerLocationMap.remove(atherialXYZLocation);
        AtherialTasks.runAsync(() -> {
            deleteSync(spawner.getUuid());
        });
        return true;
    }

    public MSpawner getSpawner(Location location) {
        if (!isSpawner(location))return null;
        return map.getOrDefault(spawnerLocationMap.get(AtherialXYZLocation.fromLocation(location, true)), null);
    }

    public boolean isSpawner(Location location) {
        return spawnerLocationMap.containsKey(AtherialXYZLocation.fromLocation(location, true));
    }

    @Override
    public void onLoadAll(List<MSpawner> list) {
        super.onLoadAll(list);
        for (MSpawner mSpawner : list) {
            spawnerLocationMap.put(mSpawner.getLocation(), mSpawner.getUuid());
        }


    }

    public static MSpawnerRegistry get() {
        return MineableSpawners.getPlugin().getSpawnerRegistry();
    }
    @Override
    public void onRegister() {


    }
}
