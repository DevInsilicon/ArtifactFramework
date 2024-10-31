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
                        return List.of("abilities");

                    case "blocks":
                        return List.of("blocks");

                    case "items":
                        return List.of("items");

                    default:
                        return List.of("abilities", "blocks", "items");
                }

            default:
                return List.of("help", "give", "assign", "unassign", "list");
        }
    }
}
