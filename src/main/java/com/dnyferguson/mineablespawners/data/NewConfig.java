package com.dnyferguson.mineablespawners.data;

import com.dnyferguson.mineablespawners.MineableSpawners;
import me.matthewedevelopment.atheriallib.config.SerializedName;
import me.matthewedevelopment.atheriallib.config.yaml.YamlConfig;
import me.matthewedevelopment.atheriallib.message.message.ChatMessage;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Matthew E on 6/30/2024 at 2:27 PM for the project MineableSpawners
 */
public class NewConfig  extends YamlConfig<MineableSpawners> {

    public ChatMessage NOT_OWNER_PLACE_ITEM = new ChatMessage("&cYou cannot place another players spawners!");
   @SerializedName("spawnDistance") public Integer SPAWN_DISTANCE = 32;
    public ChatMessage NOT_OWNER_CANT_BREAK = new ChatMessage("&cYou cannot break another players spawner!");
    @SerializedName("notSoulBound")
    public  List<String> EXCLUDED = Arrays.asList("ZOMBIE");
    public NewConfig( MineableSpawners plugin) {
        super("newC.yml", plugin);
    }

    public static NewConfig get() {

        return MineableSpawners.getNewC();
    }
}
