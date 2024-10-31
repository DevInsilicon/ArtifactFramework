package dev.insilicon.artifactFramework.Interface;

import dev.insilicon.artifactFramework.ArtifactFramework;

public class InterfaceManager {
    private ArtifactFramework plugin;

    public InterfaceManager(ArtifactFramework plugin) {
        this.plugin = plugin;

        // Register commands
        plugin.getCommand("assign").setExecutor(new AssignCMD(plugin));
        plugin.getCommand("assign").setTabCompleter(new AssignCMD(plugin));


    }
}
