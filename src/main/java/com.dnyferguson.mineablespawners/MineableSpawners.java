package com.dnyferguson.mineablespawners;

import com.dnyferguson.mineablespawners.api.API;
import com.dnyferguson.mineablespawners.commands.MineableSpawnersCommand;

import com.dnyferguson.mineablespawners.data.MSpawnerRegistry;
import com.dnyferguson.mineablespawners.data.NewConfig;
import com.dnyferguson.mineablespawners.data.SpawnerOwnerListener;
import com.dnyferguson.mineablespawners.listeners.AnvilRenameListener;
import com.dnyferguson.mineablespawners.listeners.SpawnerExplodeListener;
import com.dnyferguson.mineablespawners.listeners.WitherBreakSpawnerListener;
import com.dnyferguson.mineablespawners.listeners.EggChangeListener;
import com.dnyferguson.mineablespawners.listeners.SpawnerMineListener;
import com.dnyferguson.mineablespawners.listeners.SpawnerPlaceListener;
import com.dnyferguson.mineablespawners.shopguiplus.MineableSpawnersProvider;
import com.dnyferguson.mineablespawners.utils.ConfigurationHandler;
import me.matthewe.atheriallibplugin.AtherialAddon;
import me.matthewe.atheriallibplugin.AtherialLibPlugin;
import me.matthewedevelopment.atheriallib.dependency.vault.VaultDependency;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.exception.api.ExternalSpawnerProviderNameConflictException;
import net.brcdev.shopgui.spawner.external.provider.ExternalSpawnerProvider;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineableSpawners extends AtherialAddon {
    private static MineableSpawners plugin;

    private MSpawnerRegistry spawnerRegistry;
    private ConfigurationHandler configurationHandler;
//    private Economy econ;
    private static API api;

    private static NewConfig newC;

    public static NewConfig getNewC() {
        return newC;
    }

    private MineableSpawnersProvider spawnerProvider;

    private void hookIntoShopGui() {
        try {
            ShopGuiPlusApi.registerSpawnerProvider(this.spawnerProvider);
        } catch (Exception e) {
            getLogger().warning("Failed to hook into ShopGUI+: " + e.getMessage());
        }
    }

    public MineableSpawners() {



        AtherialLibPlugin.registerAddon(this);
    }

    @Override
    public void onStart() {
//AtherialLibPlugin.getInstance().setDebug(true);
    }

    @Override
    public void onStop() {
//        spawnerRegistry.saveAll();
    }

    @Override
    public void onEnable() {
        plugin = this;
        api = new API(this);
        newC=new NewConfig(this);
        newC.loadConfig();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        configurationHandler = new ConfigurationHandler(this);

        this.spawnerProvider = new MineableSpawnersProvider();
        hookIntoShopGui();

        getCommand("mineablespawners").setExecutor(new MineableSpawnersCommand(this));

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new SpawnerMineListener(this), this);
        pm.registerEvents(new SpawnerPlaceListener(this), this);
        pm.registerEvents(new EggChangeListener(this), this);
        pm.registerEvents(new AnvilRenameListener(this), this);
        pm.registerEvents(new SpawnerExplodeListener(this), this);
        pm.registerEvents(new WitherBreakSpawnerListener(this), this);
        pm.registerEvents(new SpawnerOwnerListener(this), this);

        if (getConfigurationHandler().getBoolean("global", "show-available")) {
            StringBuilder str = new StringBuilder("Available mob types: \n");
            for (EntityType type : EntityType.values()) {
                str.append("- ");
                str.append(type.name());
                str.append("\n");
            }
            getLogger().info(str.toString());
        }



        spawnerRegistry = new MSpawnerRegistry();
        spawnerRegistry.register();
    }

    public MSpawnerRegistry getSpawnerRegistry() {
        return spawnerRegistry;
    }

    public ConfigurationHandler getConfigurationHandler() {
        return configurationHandler;
    }

    public Economy getEcon() {
        return AtherialLibPlugin.getInstance().getDependencyManager().getDependency(VaultDependency.class).getEconomy();
    }

    public static API getApi() {
        return api;
    }

    public static MineableSpawners getPlugin() {
        return plugin;
    }
}
