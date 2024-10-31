package dev.insilicon.artifactFramework.BaseInternal.CustomClasses;

import dev.insilicon.artifactFramework.ArtifactFramework;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CustomAbility {

    private ArtifactFramework plugin;
    private String name;
    private String description;
    private List<Player> activePlayers = new ArrayList<>();


    public CustomAbility(ArtifactFramework plugin, String name, String description) {
        this.plugin = plugin;
        this.name = name;
        this.description = description;
    }


    public void interaction(Player player, List<interactionType> type) {



    }

    public void movement(Player player) {

    }


    public void tick() {

    }

    public void Second() {

    }

    public void minute() {

    }


    public void join(Player player) {

    }

    public void leave(Player player) {

    }

    public void handleClose() {

    }

    public void handleSQLdata(Player player, String data) {

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Player> getActivePlayers() {
        return activePlayers;
    }
}
