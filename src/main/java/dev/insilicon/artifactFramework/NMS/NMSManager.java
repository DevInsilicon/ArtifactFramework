package dev.insilicon.artifactFramework.NMS;

import net.minecraft.network.protocol.Packet;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import dev.insilicon.artifactFramework.ArtifactFramework;
import net.minecraft.server.level.ServerPlayer;
public class NMSManager {

    // Note:
    // No current use for non static methods, but base here for future use.
    private ArtifactFramework plugin;


    public NMSManager(ArtifactFramework plugin) {
        this.plugin = plugin;
        
    }







    // static
    public static void sendPacket(Player player, Packet packet) {

        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        serverPlayer.connection.send(packet);

    }



}
