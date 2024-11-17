package dev.insilicon.artifactFramework.CustomAbilties.Custom;

import dev.insilicon.artifactFramework.ArtifactFramework;
import dev.insilicon.artifactFramework.BaseInternal.CustomClasses.CustomAbility;
import dev.insilicon.artifactFramework.BaseInternal.CustomClasses.interactionType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class SpeedCrouchAbility extends CustomAbility {
    public SpeedCrouchAbility(ArtifactFramework plugin) {
        super(plugin, "SpeedCrouch", "Get speed when you click & crouch! Lasts for 20 seconds, 60 second cooldown.");
    }

    @Override
    public void interaction(Player player, List<interactionType> type) {
        if (type.contains(interactionType.AIR_CLICK)) {
            Player p = player;
            if (p.isSneaking()) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 20, 2));
            }
        }
    }
}
