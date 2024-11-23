package dev.insilicon.artifactFramework;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import dev.insilicon.artifactFramework.CustomAbilties.AbilityManager;
import dev.insilicon.artifactFramework.CustomBlocks.BlockListener;
import dev.insilicon.artifactFramework.CustomBlocks.CustomBlockManager;
import dev.insilicon.artifactFramework.CustomItems.ItemManager;
import dev.insilicon.artifactFramework.Interface.AssignCMD;
import dev.insilicon.artifactFramework.Interface.InterfaceManager;

public final class ArtifactFramework extends JavaPlugin implements Listener {
    private static ArtifactFramework plugin;

    private AbilityManager abilityManager;
    private ItemManager itemManager;
    private InterfaceManager interfaceManager;
    private CustomBlockManager customBlockManager;

    // CONFIG
    public static boolean customTextures = false;
    public static String textureURL = "https://your_zip_to_texture_pack.zip";
    public static boolean textures_required = false;



    // Hey!
    // The active directories that are most useful to you as a regular user are each Custom directory inside each directory such as CustomAbilites or items.
    // You can review the base class code in BaseInternal --> Custom Classes to see how the custom classes are structured.
    // There are examples of custom classes in each of the Custom directories for you to take a look at.



    @Override
    public void onEnable() {

        plugin = this;

        // make plugin folder
        getDataFolder().mkdirs();

        // Initialize managers in correct order
        try {
            this.abilityManager = new AbilityManager(this);
            this.itemManager = new ItemManager(this);
            this.customBlockManager = new CustomBlockManager(this );


            getServer().getPluginManager().registerEvents(this, this);
            getServer().getPluginManager().registerEvents(customBlockManager, this);
            getServer().getPluginManager().registerEvents(new BlockListener(customBlockManager), this);

            getServer().getScheduler().runTaskTimer(this, customBlockManager::onTick, 1L, 1L);
            getServer().getScheduler().runTaskTimer(this, customBlockManager::onSecond, 20L, 20L);
            getServer().getScheduler().runTaskTimer(this, customBlockManager::onMinute, 1200L, 1200L);
            getServer().getScheduler().runTaskTimer(this, customBlockManager::saveAllBlocks, 36000L, 36000L);

            this.interfaceManager = new InterfaceManager(this);

            getLogger().info("ArtifactFramework has been enabled successfully!");
        } catch (Exception e) {
            getLogger().severe("Error enabling ArtifactFramework: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }


        AssignCMD assignCMD = new AssignCMD(this);
        getCommand("artifact").setExecutor(assignCMD);
        getCommand("artifact").setTabCompleter(assignCMD);

    }

    @Override
    public void onDisable() {
        if (abilityManager != null) {
            abilityManager.close();
        }
        if (customBlockManager != null) {
            customBlockManager.saveAllBlocks();
        }
        getLogger().info("ArtifactFramework has been disabled!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (customTextures) {
            Player player = event.getPlayer();
            player.setResourcePack(textureURL, "Custom_Textures_1", textures_required);
        }
    }

    public AbilityManager getAbilityManager() {
        return abilityManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public InterfaceManager getInterfaceManager() {
        return interfaceManager;
    }

    public static ArtifactFramework getInstance() {
        return plugin;
    }
}