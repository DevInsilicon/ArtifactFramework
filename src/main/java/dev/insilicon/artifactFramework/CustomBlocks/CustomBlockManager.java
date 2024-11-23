package dev.insilicon.artifactFramework.CustomBlocks;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import dev.insilicon.artifactFramework.ArtifactFramework;
import dev.insilicon.artifactFramework.BaseInternal.CustomClasses.CustomBlock;
import dev.insilicon.artifactFramework.CustomBlocks.BlockSQL.Block;
import dev.insilicon.artifactFramework.CustomBlocks.Custom.ExplosiveBlock;

public class CustomBlockManager implements Listener {
    public static NamespacedKey BlockKey = new NamespacedKey(  "ArtifactFramework", "BlockKey");

    private final List<Class<? extends CustomBlock>> customBlocks = new ArrayList<>();
    private final List<CustomBlock> initializedBlocks = new ArrayList<>();
    private ArtifactFramework plugin;
    private BlockSQL blockSQL = new BlockSQL();

    public CustomBlockManager(ArtifactFramework plugin) {
        this.plugin = plugin;

        blockSQL.initialize(plugin);

        // ADD CUSTOM BLOCKS HERE
        registerBlockType(ExplosiveBlock.class);

        // DO NOT TOUCH ANYTHING PAST THIS
        backendlogic();
    }

    private void backendlogic() {
        // load block classes from SQL by nameid
        List<Block> sqlBlocks = blockSQL.getAllBlocks();
        for (Block sqlBlock : sqlBlocks) {
            registerBlock(sqlBlock.getId(), new Location(null, sqlBlock.getX(), sqlBlock.getY(), sqlBlock.getZ()));
        }
    }

    public void registerBlockType(Class<? extends CustomBlock> blockClass) {
        customBlocks.add(blockClass);
    }

    public CustomBlock registerBlock(String blockId, Location location) {
        try {
            // Find matching block type
            Class<? extends CustomBlock> blockClass = customBlocks.stream()
                    .filter(c -> c.getSimpleName().equalsIgnoreCase(blockId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown block type: " + blockId));

            // Create instance using reflection
            Constructor<? extends CustomBlock> constructor = blockClass.getConstructor(
                    ArtifactFramework.class,
                    NamespacedKey.class,
                    Object.class,
                    Location.class
            );

            CustomBlock block = constructor.newInstance(
                    ArtifactFramework.getInstance(),
                    BlockKey,
                    null,
                    location
            );

            initializedBlocks.add(block);
            return block;

        } catch (Exception e) {
            ArtifactFramework.getInstance().getLogger().severe("Failed to create block: " + e.getMessage());
            return null;
        }
    }

    public void onTick() {
        for (CustomBlock block : initializedBlocks) {
            block.tick();
        }
    }

    public void onSecond() {
        for (CustomBlock block : initializedBlocks) {
            block.second();
        }
    }

    public void onMinute() {
        for (CustomBlock block : initializedBlocks) {
            block.minute();
        }
    }

    public void handleInteraction(PlayerInteractEvent event) {
        Location location = event.getClickedBlock().getLocation();
        CustomBlock block = getBlockAtLocation(location);
        if (block != null) {
            block.interaction(event);
        }
    }

    public void handleBreak(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        CustomBlock block = getBlockAtLocation(location);
        if (block != null) {
            block.breakBlock(event);
        }
    }

    private CustomBlock getBlockAtLocation(Location location) {
        for (CustomBlock block : initializedBlocks) {
            if (block.getLocation().equals(location)) {
                return block;
            }
        }
        Block sqlBlock = blockSQL.getBlockFromLocation(location.getX(), location.getY(), location.getZ());
        if (sqlBlock != null) {
            return registerBlock(sqlBlock.getId(), location);
        }
        return null;
    }

    public void saveAllBlocks() {
        for (CustomBlock block : initializedBlocks) {
            blockSQL.addBlock(
                block.block_id,
                block.getLocation().getX(),
                block.getLocation().getY(),
                block.getLocation().getZ(),
                block.getBlock_data()
            );
        }
    }
}
