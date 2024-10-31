package dev.insilicon.artifactFramework;

import dev.insilicon.artifactFramework.BaseInternal.CustomClasses.PDTKeys;
import dev.insilicon.artifactFramework.CustomAbilties.AbilityManager;
import dev.insilicon.artifactFramework.CustomItems.ItemManager;
import dev.insilicon.artifactFramework.Interface.InterfaceManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public final class ArtifactFramework extends JavaPlugin implements Listener {

    private AbilityManager abilityManager;
    private ItemManager itemManager;
    private InterfaceManager interfaceManager;

    //CONFIG
    public static boolean customTextures = false;
    public static String textureURL = "https://your_zip_to_texture_pack.zip";
    public static boolean textures_required = false;





    @Override
    public void onEnable() {
        // Plugin startup logic


        abilityManager = new AbilityManager(this);
        itemManager = new ItemManager(this);
        interfaceManager = new InterfaceManager(this);

        getServer().getPluginManager().registerEvents(this, this);



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        abilityManager.close();

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
