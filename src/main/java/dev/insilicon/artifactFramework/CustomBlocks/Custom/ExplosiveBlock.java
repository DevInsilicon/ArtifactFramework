package dev.insilicon.artifactFramework.CustomBlocks.Custom;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import dev.insilicon.artifactFramework.ArtifactFramework;
import dev.insilicon.artifactFramework.BaseInternal.CustomClasses.BlockDataType;
import dev.insilicon.artifactFramework.BaseInternal.CustomClasses.CustomBlock;

public class ExplosiveBlock extends CustomBlock {

    public ExplosiveBlock(ArtifactFramework plugin, NamespacedKey key, Object dataClass, Location location) { // The inputs must ALWAYS remain in the same order and none extra
        super(plugin, "ExplosiveBlock", "Explodes when you click it three times or you break it.", key, dataClass, new ExplosiveBlock_Data(4,3), location);
    }

    @Override
    public ItemStack blockStack() {
        ItemStack item = new ItemStack(Material.TNT, 1);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(miniMessage.deserialize("<red>Explosive Block"));
        meta.lore(List.of(
           miniMessage.deserialize("<gray>You need to click this block 3 times for it to explode!")
        ));

        item.setItemMeta(meta);
        return applyPDC(item);
    }

    @Override
public boolean place(Location location) {
    this.block_location = location;
    
    Block block = location.getBlock();
    block.setType(Material.TNT);
    
    // THIS IS REQUIRED THAT YOU SET THE PDC CONTAINER FOR THE BLOCK OR ELSE THE CBM WON'T BE ABLE TO FIND THE BLOCK
    if (block.getState() instanceof TileState state) {
        PersistentDataContainer container = state.getPersistentDataContainer();
        container.set(block_key, PersistentDataType.STRING, block_id.toLowerCase());
        state.update();
    }
    
    return true;
}

    @Override
    public void interaction(PlayerInteractEvent event) {

        // subtract clicks
        ExplosiveBlock_Data data = (ExplosiveBlock_Data) getBlock_data();
        data.clicked();
        updateBlockData(data);

        // if clicks are 0, explode the block
        if (data.getClicksLeft() == 0) {
            block_location.getWorld().createExplosion(block_location, data.getExplosivePower());
        }

    }



    @Override
    public boolean breakBlock(BlockBreakEvent event) {
        // explode the block automatically at location if location is not null
        if (block_location != null) {
            ExplosiveBlock_Data data = (ExplosiveBlock_Data) getBlock_data();
            block_location.getWorld().createExplosion(block_location, data.getExplosivePower());
        }
        return true;

    }
}

class ExplosiveBlock_Data extends BlockDataType {
    // This is a local data class. It is only accessible within this file. If you need your data class to be accessible outside of just this class refer to the BlockData.ExplosiveBlockData class for an example.
    private int explosivePower;
    private int clicksLeft;

    public ExplosiveBlock_Data(int explosivePower, int clicksLeft) {
        this.explosivePower = explosivePower;
        this.clicksLeft = clicksLeft;
    }

    public int getExplosivePower() {
        return explosivePower;
    }

    public int getClicksLeft() {
        return clicksLeft;
    }

    public void clicked() {
        clicksLeft--;
    }
}
