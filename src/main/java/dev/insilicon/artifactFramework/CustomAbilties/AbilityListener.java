package dev.insilicon.artifactFramework.CustomAbilties;

import dev.insilicon.artifactFramework.ArtifactFramework;
import dev.insilicon.artifactFramework.BaseInternal.CustomClasses.CustomAbility;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class AbilityListener implements Listener {

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
            abilityManager.handleRegisterLogic(player);
        } else {
            abilityManager.handleAssignmentLogic(player, abilityData);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String abilityName = abilitySQL.getAbility(player);

        if (abilityName != null) {
            abilityManager.removeOnlinePlayer(player, abilityName);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!hasAbility(event.getPlayer())) return;

        List<interactionType> type = new ArrayList<>();

        switch (event.getAction()) {
            case LEFT_CLICK_AIR:
                type.add(interactionType.AIR_CLICK);
                break;
            case RIGHT_CLICK_AIR:
                type.add(interactionType.AIR_CLICK);
                break;
            case LEFT_CLICK_BLOCK:
                type.add(interactionType.BREAK_CLICK);
                break;
            case RIGHT_CLICK_BLOCK:
                type.add(interactionType.USE_CLICK);
                break;
            case PHYSICAL:
                type.add(interactionType.ATTACK_CLICK);
                break;
            default:
                type.add(interactionType.UNKNOWN);
                break;
        }

        abilityManager.distributeInteraction(event.getPlayer(), type);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!hasAbility(player)) return;

        List<interactionType> type = new ArrayList<>();
        type.add(interactionType.HURT_CLICK);
        abilityManager.distributeInteraction(player, type);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        if (!hasAbility(player)) return;

        List<interactionType> type = new ArrayList<>();
        type.add(interactionType.OPEN_CLICK);
        abilityManager.distributeInteraction(player, type);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!hasAbility(event.getPlayer())) return;
        abilityManager.distributeMovement(event.getPlayer());
    }

    public boolean hasAbility(Player player) {
        return abilitySQL.getAbility(player) != null;
    }
}