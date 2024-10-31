package dev.insilicon.artifactFramework.BaseInternal.CustomClasses;

import dev.insilicon.artifactFramework.ArtifactFramework;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CustomItem {

    private ArtifactFramework plugin;
    private String name;
    private String description;
    private List<Player> activeHolders = new ArrayList<>();
    private List<Player> activeHand = new ArrayList<>();

    public CustomItem(ArtifactFramework plugin, String name, String description) {
        this.plugin = plugin;
        this.name = name;
        this.description = description;
    }

    public void interaction(Player player, List<interactionType> types) {

    }

    public void used(Player player) {

    }

    public void tick() {

    }


    public void Second() {

    }

    public void minute() {

    }
}
