package com.dnyferguson.mineablespawners.data;

import com.dnyferguson.mineablespawners.MineableSpawners;
import me.matthewedevelopment.atheriallib.database.registry.DataObjectRegistry;
import me.matthewedevelopment.atheriallib.utilities.location.AtherialLocation;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Matthew E on 6/27/2024 at 3:30 PM for the project MineableSpawners
 */
public class MSpawnerRegistry  extends DataObjectRegistry<MSpawner> {
    private Map<AtherialLocation, UUID> spawnerLocationMap;

    public void insertNewSpawner(Location loc, Player owner, EntityType type) {
        spawnerLocationMap.put(XYZ.)
    }
    public MSpawnerRegistry() {
        super(MSpawner.class);
        this. spawnerLocationMap=new HashMap<>();
    }

    public static MSpawnerRegistry get() {
        return MineableSpawners.getPlugin().getSpawnerRegistry();
    }
    @Override
    public void onRegister() {


    }
}
