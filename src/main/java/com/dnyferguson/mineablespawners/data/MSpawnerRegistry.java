package com.dnyferguson.mineablespawners.data;

import me.matthewedevelopment.atheriallib.database.registry.DataObjectRegistry;

/**
 * Created by Matthew E on 6/27/2024 at 3:30 PM for the project MineableSpawners
 */
public class MSpawnerRegistry  extends DataObjectRegistry<MSpawner> {
    public MSpawnerRegistry() {
        super(MSpawner.class);
    }

    @Override
    public void onRegister() {


    }
}
