package dev.insilicon.artifactFramework;

import dev.insilicon.artifactFramework.CustomAbilties.AbilityManager;
import dev.insilicon.artifactFramework.CustomBlocks.BlockManager;
import dev.insilicon.artifactFramework.CustomItems.ItemManager;
import dev.insilicon.artifactFramework.Interface.InterfaceManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ArtifactFramework extends JavaPlugin {

    private AbilityManager abilityManager;
    private BlockManager blockManager;
    private ItemManager itemManager;
    private InterfaceManager interfaceManager;



    @Override
    public void onEnable() {
        // Plugin startup logic


        abilityManager = new AbilityManager(this);
        blockManager = new BlockManager(this);
        itemManager = new ItemManager(this);
        interfaceManager = new InterfaceManager(this);



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
