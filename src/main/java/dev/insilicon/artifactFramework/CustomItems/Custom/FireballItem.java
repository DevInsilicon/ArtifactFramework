package dev.insilicon.artifactFramework.CustomItems.Custom;

import dev.insilicon.artifactFramework.ArtifactFramework;
import dev.insilicon.artifactFramework.BaseInternal.CustomClasses.CustomItem;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class FireballItem extends CustomItem {

    private MiniMessage miniMessage = MiniMessage.miniMessage();
    private HashMap<Player, Long> cooldown = new HashMap<>();

    public FireballItem(ArtifactFramework plugin) {
        super(plugin, "FireballSword", "Shoots fireballs, and is a example custom item");

        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(miniMessage.deserialize("<gradient:#FB7408:#FFDC16>Fire Ball</gradient> <gradient:#CACACA:#CACACA>Sword</gradient>"));
        meta.lore(List.of(miniMessage.deserialize("<color:#8109E7>Shoots fireballs when you click!</color>")));

        item.setItemMeta(meta);
        setItembase(item);
    }

    @Override
    public void interaction(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() == null) return;

        // Cooldown period in milliseconds (e.g., 10 seconds)
        long cooldownPeriod = 10000;

        // Check if the player is in the cooldown map
        if (cooldown.containsKey(player)) {
            long lastUsed = cooldown.get(player);
            long currentTime = System.currentTimeMillis();

            // Check if the cooldown period has passed
            if (currentTime - lastUsed < cooldownPeriod) {
                player.sendMessage(miniMessage.deserialize("<red>Item is on cooldown!</red>"));
                return;
            }
        }

        // Update the cooldown map with the current time
        cooldown.put(player, System.currentTimeMillis());

        // Your interaction logic here
        if (event.getClickedBlock() != null) return;

        //spawn fireball at player location 1 block in front of player facing direction then set its velcoity away from player
        player.launchProjectile(org.bukkit.entity.Fireball.class, player.getLocation().getDirection().multiply(1.5));
    }




}
