package dev.insilicon.artifactFramework;

import dev.insilicon.artifactFramework.CustomAbilties.AbilityManager;
import dev.insilicon.artifactFramework.CustomItems.ItemManager;
import dev.insilicon.artifactFramework.Interface.AssignCMD;
import dev.insilicon.artifactFramework.Interface.InterfaceManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class ArtifactFramework extends JavaPlugin implements Listener {

    private AbilityManager abilityManager;
    private ItemManager itemManager;
    private InterfaceManager interfaceManager;

    // CONFIG
    public static boolean customTextures = false;
    public static String textureURL = "https://your_zip_to_texture_pack.zip";
    public static boolean textures_required = false;

    @Override
    public void onEnable() {

        // make plugin folder
        getDataFolder().mkdirs();

        // Initialize managers in correct order
        try {
            this.abilityManager = new AbilityManager(this);
            this.itemManager = new ItemManager(this);

            // Register events before interface manager
            getServer().getPluginManager().registerEvents(this, this);

            // Initialize interface manager last
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
}