package dev.insilicon.artifactFramework.CustomBlocks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockListener implements Listener {
    private final CustomBlockManager customBlockManager;

    public BlockListener(CustomBlockManager customBlockManager) {
        this.customBlockManager = customBlockManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        customBlockManager.handleInteraction(event);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        customBlockManager.handleBreak(event);
    }
}
