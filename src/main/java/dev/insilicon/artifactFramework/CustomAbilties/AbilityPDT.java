package dev.insilicon.artifactFramework.CustomAbilties;

import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.persistence.PersistentDataType;

import dev.insilicon.artifactFramework.ArtifactFramework;

public class AbilityPDT {
    // Note:
    // This used to be a class called AbilitySQL, but it was removed for a cleaner implementation
    // using the PDC system. You can look at the original implementation in the git history.
    // Anything with "AbilitySQLData" was removed, because of the new PDC system, and how easy it is to just add another key.

    public static NamespacedKey abilityKey = new NamespacedKey(ArtifactFramework.getPlugin(ArtifactFramework.class), "abilitykey");

    private ArtifactFramework plugin;
    

    public AbilityPDT(ArtifactFramework plugin) {
        this.plugin = plugin;

    }

    public void setAbility(OfflinePlayer player, String abilityName) {
        player.getPlayer().getPersistentDataContainer().set(abilityKey, PersistentDataType.STRING, abilityName);
    }

    public String getAbility(OfflinePlayer player) {
        return player.getPlayer().getPersistentDataContainer().get(abilityKey, PersistentDataType.STRING);
    }
    
}
