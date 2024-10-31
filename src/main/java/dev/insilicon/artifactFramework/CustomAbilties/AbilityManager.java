package dev.insilicon.artifactFramework.CustomAbilties;

import dev.insilicon.artifactFramework.ArtifactFramework;
import dev.insilicon.artifactFramework.BaseInternal.CustomClasses.CustomAbility;
import dev.insilicon.artifactFramework.BaseInternal.CustomClasses.interactionType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AbilityManager {

    private ArtifactFramework plugin;
    private AbilitySQL abilitySQL;
    private List<Player> onlinePlayers = new ArrayList<>();
    private List<CustomAbility> abilities = new ArrayList<>();
    private AbilityListener abilityListener;

    public AbilityManager(ArtifactFramework plugin) {
        this.plugin = plugin;


        abilitySQL = new AbilitySQL(plugin);
        abilityListener = new AbilityListener(plugin, this);
        plugin.getServer().getPluginManager().registerEvents(abilityListener, plugin);


        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                second();
            }
        }, 0L, 20L);

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                minute();
            }
        }, 0L, 1200L);
    }

    public AbilitySQL getAbilitySQL() {
        return abilitySQL;
    }

    public void addOnlinePlayer(Player player) {
        onlinePlayers.add(player);
    }

    public void removeOnlinePlayer(Player player, String abilityName) {
        onlinePlayers.remove(player);

        for (CustomAbility ability : abilities) {
            if (ability.getName().equals(abilityName)) {
                ability.leave(player);
            }
        }
    }

    public void handleRegisterLogic(Player player) {

        // Make sure you add this logic (This is called when a player join and doesnt have a registered ability

        //example logic give player a random ability
        if (abilities.isEmpty()) {
            return; // No abilities available
        }

        // Select a random ability from the list
        CustomAbility randomAbility = abilities.get(new Random().nextInt(abilities.size()));
        String abilityName = randomAbility.getName();

        // Assign the ability to the player
        abilitySQL.setAbility(player, abilityName, "");
        randomAbility.join(player);

    }

    public List<String> getAbilities() {
        List<String> names = new ArrayList<>();
        for (CustomAbility ability : abilities) {
            names.add(ability.getName());
        }
        return names;
    }

    public void handleAssignmentLogic(Player player, String abilityData) {

        for (CustomAbility ability : abilities) {
            if (ability.getName().equals(abilityData)) {
                ability.join(player);
                ability.handleSQLdata(player, abilityData);
            }
        }

    }

    public void distributeInteraction(Player player, List<interactionType> types) {
        for (CustomAbility ability : abilities) {
            ability.interaction(player, types);
        }
    }

    public void close() {
        for (CustomAbility ability : abilities) {
            ability.handleClose();
        }
    }

    public void distributeMovement(Player player) {
        for (CustomAbility ability : abilities) {
            ability.movement(player);
        }
    }

    public void tick() {
        for (CustomAbility ability : abilities) {
            ability.tick();
        }
    }

    public void second() {
        for (CustomAbility ability : abilities) {
            ability.Second();
        }
    }

    public void minute() {
        for (CustomAbility ability : abilities) {
            ability.minute();
        }
    }


}
