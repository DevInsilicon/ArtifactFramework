package dev.insilicon.artifactFramework.Interface;

import dev.insilicon.artifactFramework.ArtifactFramework;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AssignCMD implements CommandExecutor, TabCompleter {

    private ArtifactFramework plugin;
    private MiniMessage miniMessage = MiniMessage.miniMessage();


    public AssignCMD(ArtifactFramework plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (strings.length == 0) {
            commandSender.sendMessage(miniMessage.deserialize("<red>Invalid arguments!"));
            return true;
        }






        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        String[] current_args = strings;

        switch (current_args[0]) {

            case "list":
                switch (current_args[1]) {

                    case "abilities":
                        return plugin.getAbilityManager().getAbilities();


                    case "items":
                        return plugin.getItemManager().getCustomItems();

                    default:
                        return List.of("abilities", "items");
                }

            case "give":
                if (current_args[1] == null) {
                    return plugin.getServer().getOnlinePlayers().stream().map(player -> player.getName()).toList();
                }
                if (current_args[2] == null) {
                    return plugin.getItemManager().getCustomItems();
                }
            case "assgin":
                if (current_args[1] == null) {
                    return plugin.getServer().getOnlinePlayers().stream().map(player -> player.getName()).toList();
                }
                if (current_args[2] == null) {
                    return plugin.getAbilityManager().getAbilities();
                }

            case "unassign":
                if (current_args[1] == null) {
                    return plugin.getServer().getOnlinePlayers().stream().map(player -> player.getName()).toList();
                }
                if (current_args[2] == null) {
                    return plugin.getAbilityManager().getAbilities();
                }

            default:
                return List.of("help", "give", "assign", "unassign", "list");
        }
    }
}
