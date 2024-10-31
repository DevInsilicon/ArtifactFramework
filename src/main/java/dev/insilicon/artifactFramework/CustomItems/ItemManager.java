package dev.insilicon.artifactFramework.CustomItems;

import dev.insilicon.artifactFramework.ArtifactFramework;
import dev.insilicon.artifactFramework.BaseInternal.CustomClasses.CustomItem;
import dev.insilicon.artifactFramework.BaseInternal.CustomClasses.PDTKeys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager implements Listener {
    private ArtifactFramework plugin;

    private List<CustomItem> customItems = new ArrayList<>();


    public ItemManager(ArtifactFramework plugin) {
        this.plugin = plugin;


        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // ADD CUSTOM ITEMS HERE



        // END

        // register the second and minute bukkit schedulers
        for (CustomItem customItem : customItems) {
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    customItem.Second();
                }
            }, 0L, 20L);
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    customItem.minute();
                }
            }, 0L, 1200L);
        }

        // tick scheduler
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                tick();
            }
        }, 0L, 1L);

    }


    public void giveCustomItem(Player player, String name) {
        for (CustomItem customItem : customItems) {
            if (customItem.getName().equals(name)) {
                ItemStack item = customItem.getItembase();
                player.getInventory().addItem(item);
            }
        }
    }

    public List<String> getCustomItems() {
        List<String> names = new ArrayList<>();
        for (CustomItem customItem : customItems) {
            names.add(customItem.getName());
        }
        return names;
    }

    // selected item change on hotbar event
    @EventHandler
    public void onItemHeldChange(PlayerItemHeldEvent event) {
        // Handle item selection change
        int newSlot = event.getNewSlot();
        int previousSlot = event.getPreviousSlot();

        ItemStack previousItem = event.getPlayer().getInventory().getItem(previousSlot);
        ItemStack newItem = event.getPlayer().getInventory().getItem(newSlot);

        if (newItem != null) {

            if (newItem.getItemMeta().getPersistentDataContainer().has(PDTKeys.CUSTOM_ITEM, PersistentDataType.STRING)) {

                for (CustomItem customItem : customItems) {
                    if (customItem.getName().equals(newItem.getItemMeta().getPersistentDataContainer().get(PDTKeys.CUSTOM_ITEM, PersistentDataType.STRING))) {
                        customItem.addHand(event.getPlayer());
                        customItem.equipped(event.getPlayer());

                    }
                }

            }

        } else {
            if (previousItem != null) {
                if (previousItem.getItemMeta().getPersistentDataContainer().has(PDTKeys.CUSTOM_ITEM, PersistentDataType.STRING)) {
                    for (CustomItem customItem : customItems) {
                        if (customItem.getName().equals(previousItem.getItemMeta().getPersistentDataContainer().get(PDTKeys.CUSTOM_ITEM, PersistentDataType.STRING))) {
                            customItem.removeHand(event.getPlayer());
                            customItem.unequipped(event.getPlayer());
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        for (CustomItem customItem : customItems) {
           if (customItem.isHolder(event.getPlayer())) {
               customItem.movement(event.getPlayer());
               break;
           }
        }
    }


    // pickup
    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if (event.getItem().getItemStack().getItemMeta().getPersistentDataContainer().has(PDTKeys.CUSTOM_ITEM, PersistentDataType.STRING)) {
            for (CustomItem customItem : customItems) {
                if (customItem.getName().equals(event.getItem().getItemStack().getItemMeta().getPersistentDataContainer().get(PDTKeys.CUSTOM_ITEM, PersistentDataType.STRING))) {
                    customItem.addHolder(event.getPlayer());
                    customItem.newHolder(event.getPlayer());
                }
            }
        }
    }

    // player interaction
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        for (CustomItem customItem : customItems) {
            if (customItem.isHand(event.getPlayer())) {
                customItem.interaction(event);
            }
        }
    }

    //attack
    @EventHandler
    public void onPlayerAttack(PlayerInteractEvent event) {
        for (CustomItem customItem : customItems) {
            if (customItem.isHand(event.getPlayer())) {
                customItem.attack(event.getPlayer());
            }
        }
    }


    public void tick() {
        for (CustomItem customItem : customItems) {
            customItem.tick();
        }
    }









}
