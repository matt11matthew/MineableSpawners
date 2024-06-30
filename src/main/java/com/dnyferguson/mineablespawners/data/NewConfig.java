package com.dnyferguson.mineablespawners.data;

import com.dnyferguson.mineablespawners.MineableSpawners;
import me.matthewedevelopment.atheriallib.config.SerializedName;
import me.matthewedevelopment.atheriallib.config.yaml.YamlConfig;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Matthew E on 6/30/2024 at 2:27 PM for the project MineableSpawners
 */
public class NewConfig  extends YamlConfig<MineableSpawners> {

    @SerializedName("notSoulBound")
    public  List<String> EXCLUDED = Arrays.asList("ZOMBIE");
    public NewConfig( MineableSpawners plugin) {
        super("newC.yml", plugin);
    }
}
