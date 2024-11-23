package dev.insilicon.artifactFramework.BaseInternal.CustomClasses;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import dev.insilicon.artifactFramework.ArtifactFramework;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class CustomBlock implements Listener { // <-- YES Each custom block extends a listener so you by default have access to all the events in the game you do NOT NEED TO REGISTER IT.
    public String block_id;
    private String description;
    public Location block_location;
    private BlockDataType block_data;
    private Object provided_base_data;
    public NamespacedKey block_key;
    public ArtifactFramework plugin;
    public static MiniMessage miniMessage = MiniMessage.miniMessage();


    public CustomBlock(ArtifactFramework plugin, String block_id, String description, NamespacedKey key,Object dataClass, BlockDataType block_data, Location location) {
        // This isn't a all in one block manager this class is an instance of each block you want to create.
        this.plugin = plugin;
        this.block_id = block_id;
        this.description = description;
        this.block_key = key;
        this.provided_base_data = dataClass;

        // if dataClass is null then we will use the block_data class that is passed in
        if (dataClass == null) {
            this.block_data = block_data;
        } else {
            this.block_data = (BlockDataType) dataClass;
        }

        // if dataClass can be casted to the type of block_data then we will use the dataClass use try catch to prevent errors
        try {
            this.block_data = (BlockDataType) dataClass;
        } catch (Exception e) {
            plugin.getLogger().severe("Error casting dataClass to BlockDataType for block: " + block_id);
        }

        place(location);

    }

    public ItemStack blockStack() {
        ItemStack item = new ItemStack(Material.STONE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize("<red>Oops! The user that is using this framework forgot to implement the blockStack method for the block: " + block_id));
        meta.getPersistentDataContainer().set(block_key, PersistentDataType.STRING, block_id);
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack applyPDC(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(block_key, PersistentDataType.STRING, block_id);
        item.setItemMeta(meta);
        return item;
    }


    public boolean place(Location location) {
        boolean do_place = false;
        if (do_place) {
            block_location = location;
            plugin.getLogger().severe("Oops! The user that is using this framework forgot to implement the place method for the block: " + block_id);
        }
        // custom place logic where you physically place the block in the world using the paper api
        // Return true and that means the item is successfully placed and the framework will remove it from the player's inventory.
        // Return false and the item will not be placed and the framework will not remove it from the player's inventory.

        return do_place;
    }

    public void interaction(PlayerInteractEvent event) {
        // if you don't want to use the event which idk why you wouldn't but this is called when the player interacts with the block
    }

    public boolean breakBlock(BlockBreakEvent event)  {
        // when the block is broken by a player or entity this will be called, to have a block registered by explosion you must hook onto the listener
        return true;
    }

    // 3 below are self explanitory
    public void tick() {

    }

    public void second() {

    }

    public void minute() {

    }


    @SuppressWarnings("unchecked")
    public <T extends BlockDataType> T getBlock_data() {
        try {
            return (T) block_data;
        } catch (ClassCastException e) {
            plugin.getLogger().severe("Error casting provided_base_data to BlockDataType for block: " + block_id);
            return null;
        }
    }


    public <T extends BlockDataType> boolean updateBlockData(T newData) {
        try {
            if (provided_base_data != null &&
                    !provided_base_data.getClass().equals(newData.getClass())) {
                plugin.getLogger().warning("Attempted to update block data with incompatible type for block: " + block_id);
                return false;
            }

            this.block_data = newData;
            // Additional update logic here if needed
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Error updating block data for block: " + block_id + ": " + e.getMessage());
            return false;
        }
    }

    public Location getLocation() {
        return block_location;
    }
}
