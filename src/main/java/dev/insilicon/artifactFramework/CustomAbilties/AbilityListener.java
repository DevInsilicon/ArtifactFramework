package dev.insilicon.artifactFramework.CustomAbilties;

import dev.insilicon.artifactFramework.ArtifactFramework;
import dev.insilicon.artifactFramework.BaseInternal.CustomClasses.interactionType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class AbilityListener implements Listener
{

    private ArtifactFramework plugin;
    private AbilityManager abilityManager;
    private AbilitySQL abilitySQL;

    public AbilityListener(ArtifactFramework plugin, AbilityManager abilityManager) {
        this.plugin = plugin;
        this.abilityManager = abilityManager;
        this.abilitySQL = this.abilityManager.getAbilitySQL();


    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String abilityName = abilitySQL.getAbility(player);
        String abilityData = abilitySQL.getAbilityData(player);

        abilityManager.addOnlinePlayer(player);

        if (abilityName == null) {
            abilityManager.handleAssignmentLogic(player, abilityData);
        } else {
            abilityManager.handleRegisterLogic(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String abilityName = abilitySQL.getAbility(player);
        String abilityData = abilitySQL.getAbilityData(player);

        if (abilityName != null) {
            abilityManager.removeOnlinePlayer(player, abilityName);
        }
    }


    // on interact event
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!hasAbility((Player) event.getPlayer())) return;

        List<interactionType> type = new ArrayList<>();

        switch (event.getAction()) {
            case LEFT_CLICK_AIR:
                type.add(interactionType.AIR_CLICK);
            case RIGHT_CLICK_AIR:
                type.add(interactionType.AIR_CLICK);
            case LEFT_CLICK_BLOCK:
                type.add(interactionType.BREAK_CLICK);
            case RIGHT_CLICK_BLOCK:
                type.add(interactionType.USE_CLICK);
            case PHYSICAL:
                type.add(interactionType.ATTACK_CLICK);

            default:
                type.add(interactionType.UNKNOWN);
                break;
        }

        abilityManager.distributeInteraction(event.getPlayer(), type);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!hasAbility((Player) event.getDamager())) return;

        if (event.getDamager() instanceof Player) {
            List<interactionType> type = new ArrayList<>();
            type.add(interactionType.HURT_CLICK);

            Player attacker = (Player) event.getDamager();

            abilityManager.distributeInteraction(attacker, type);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!hasAbility((Player) event.getPlayer())) return;

        if (event.getPlayer() instanceof Player) {
            List<interactionType> type = new ArrayList<>();
            type.add(interactionType.OPEN_CLICK);

            abilityManager.distributeInteraction((Player) event.getPlayer(), type);

        }
    }

    // player move
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!hasAbility((Player) event.getPlayer())) return;

        abilityManager.distributeMovement(event.getPlayer());

    }





    public boolean hasAbility(Player player) {
        return abilitySQL.getAbility(player) != null;
    }

    //tick
    @EventHandler
    public void onTick() {
        abilityManager.tick();
    }

}
