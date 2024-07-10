package com.dnyferguson.mineablespawners.data;

import me.matthewedevelopment.atheriallib.database.registry.DataColumn;
import me.matthewedevelopment.atheriallib.database.registry.DataColumnType;
import me.matthewedevelopment.atheriallib.database.registry.DataObject;
import me.matthewedevelopment.atheriallib.utilities.location.AtherialLocation;
import me.matthewedevelopment.atheriallib.utilities.location.AtherialXYZLocation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Matthew E on 6/27/2024 at 3:28 PM for the project MineableSpawners
 */
public class MSpawner  extends DataObject<MSpawner> {

    private AtherialXYZLocation location;
    private String type;
    private UUID owner;

    public MSpawner(UUID uuid, AtherialXYZLocation location, String type, UUID owner) {
        super(uuid);
        this.location = location;
        this.type = type;
        this.owner = owner;

    }

    public AtherialXYZLocation getLocation() {
        return location;
    }

    public UUID getOwner() {
        return owner;
    }

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
        columns.add(new DataColumn("type", DataColumnType.VARCHAR, type));
        columns.add(new DataColumn("owner", DataColumnType.VARCHAR, owner));

        return columns;
    }

    public String getType() {
        return type;
    }

    @Override
    public MSpawner loadResultFromSet(ResultSet rs) {
        try {
            String locString = rs.getString("location");
            if (isTextClear(locString)) {
                location = AtherialXYZLocation.fromString(locString);
            }

            type=rs.getString("type");

            String ownerString = rs.getString("owner");
            owner=isTextClear(ownerString)?UUID.fromString(ownerString) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }
}