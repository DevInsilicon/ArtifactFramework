package dev.insilicon.artifactFramework.BaseInternal.CustomClasses;

import dev.insilicon.artifactFramework.ArtifactFramework;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CustomItem {

    private ArtifactFramework plugin;
    private String name;
    private MiniMessage miniMessage = MiniMessage.miniMessage();
    private String description;
    private List<Player> activeHolders = new ArrayList<>();
    private List<Player> activeHand = new ArrayList<>();
    private ItemStack itembase = new ItemStack(Material.COMPASS);

    public CustomItem(ArtifactFramework plugin, String name, String description) {
        this.plugin = plugin;
        this.name = name;
        this.description = description;
    }

    public void interaction(PlayerInteractEvent event) {

    }

    public void attack(Player player) {

    }

    public void equipped(Player player) {

    }

    public void unequipped(Player player) {

    }

    public void movement(Player player) {

    }

    public void tick() {

    }


    public void Second() {

    }

    public void minute() {

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void addHolder(Player player) {
        activeHolders.add(player);
    }

    public void removeHolder(Player player) {
        activeHolders.remove(player);
    }

    public void addHand(Player player) {
        activeHand.add(player);
    }

    public void removeHand(Player player) {
        activeHand.remove(player);
    }

    public boolean isHolder(Player player) {
        return activeHolders.contains(player);
    }

    public boolean isHand(Player player) {
        return activeHand.contains(player);
    }

    public void newHolder(Player player) {

    }

    public ItemStack getItembase() {
        return itembase;
    }
    public void setItembase(ItemStack itembase) {
        this.itembase = itembase;
    }
}
