package com.dnyferguson.mineablespawners.data;

import me.matthewedevelopment.atheriallib.database.registry.DataColumn;
import me.matthewedevelopment.atheriallib.database.registry.DataColumnType;
import me.matthewedevelopment.atheriallib.database.registry.DataObject;
import me.matthewedevelopment.atheriallib.utilities.location.AtherialLocation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Matthew E on 6/27/2024 at 3:28 PM for the project MineableSpawners
 */
public class MSpawner  extends DataObject<MSpawner> {

    private AtherialLocation location;


    public MSpawner(UUID uuid) {
        super(uuid);
    }

    public MSpawner() {
    }

    @Override
    public String getTableName() {
        return "mspawners";
    }

    @Override
    public List<DataColumn> getDefaultColumns() {
        List<DataColumn> columns = new ArrayList<>();
        columns.add(new DataColumn("location", DataColumnType.VARCHAR, location));

        return columns;
    }

    @Override
    public MSpawner loadResultFromSet(ResultSet rs) {
        try {
            String locString = rs.getString("location");
            if (isTextClear(locString)) {
                location = AtherialLocation.fromString(locString);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }
}