package dev.insilicon.artifactFramework.BaseInternal.CustomClasses;

import dev.insilicon.artifactFramework.ArtifactFramework;
import org.bukkit.entity.Player;

import java.util.List;

public class CustomBlock {

    private ArtifactFramework plugin;
    private String name;
    private String description;

    public CustomBlock(ArtifactFramework plugin, String name, String description) {
        this.plugin = plugin;
        this.name = name;
        this.description = description;
    }

    public void interaction(Player player, List<interactionType> types) {

    }

    public void breaking() {

    }

    public void broken() {

    }

    public void tick() {

    }

    public void Second() {

    }

    public void minute() {

    }



}
