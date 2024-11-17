package dev.insilicon.artifactFramework.Interface;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.insilicon.artifactFramework.ArtifactFramework;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class AssignCMD implements CommandExecutor, TabCompleter {

    private ArtifactFramework plugin;
    private MiniMessage miniMessage = MiniMessage.miniMessage();

    public AssignCMD(ArtifactFramework plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        // Permission check
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(miniMessage.deserialize("<dark_red><bold>Error:</bold> This command must be executed by a player!"));
            return true;
        } else {
            if (!PermissionHandler.hasPermission((Player) commandSender, "artifactframework.admin")) {
                commandSender.sendMessage(miniMessage.deserialize("<red>⚠ <bold>Access Denied:</bold> You lack the required permissions!"));
                return true;
            }
        }

        // Basic argument check
        if (strings.length == 0) {
            showHelp(commandSender);
            return true;
        }

        switch (strings[0].toLowerCase()) {
            case "help":
                showHelp(commandSender);
                break;

            case "list":
                if (strings.length < 2) {
                    commandSender.sendMessage(miniMessage.deserialize("<yellow>Usage: <gray>/assign list <green>[abilities|items]"));
                    return true;
                }
                handleList(commandSender, strings[1]);
                break;

            case "give":
                if (strings.length < 3) {
                    commandSender.sendMessage(miniMessage.deserialize("<yellow>Usage: <gray>/assign give <green><player> <item>"));
                    return true;
                }
                handleGive(commandSender, strings[1], strings[2]);
                break;

            case "assign":
                if (strings.length < 3) {
                    commandSender.sendMessage(miniMessage.deserialize("<yellow>Usage: <gray>/assign assign <green><player> <ability>"));
                    return true;
                }
                handleAssign(commandSender, strings[1], strings[2]);
                break;

            case "unassign":
                if (strings.length < 3) {
                    commandSender.sendMessage(miniMessage.deserialize("<yellow>Usage: <gray>/assign unassign <green><player> <ability>"));
                    return true;
                }
                handleUnassign(commandSender, strings[1], strings[2]);
                break;

            default:
                commandSender.sendMessage(miniMessage.deserialize("<red>Unknown subcommand! Use <yellow>/assign help</yellow> for available commands."));
                break;
        }

        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(miniMessage.deserialize("""
            <gradient:gold:yellow>═══════ ArtifactFramework Help ═══════</gradient>
            <yellow>Available Commands:
            <gray>» <green>/assign help <gray>- Show this help menu
            <gray>» <green>/assign list <abilities|items> <gray>- List abilities or items
            <gray>» <green>/assign give <player> <item> <gray>- Give custom item
            <gray>» <green>/assign assign <player> <ability> <gray>- Assign ability
            <gray>» <green>/assign unassign <player> <ability> <gray>- Remove ability
            <gradient:gold:yellow>═══════════════════════════════</gradient>
            """));
    }

    private void handleList(CommandSender sender, String type) {
        switch (type.toLowerCase()) {
            case "abilities":
                List<String> abilities = plugin.getAbilityManager().getAbilities();
                if (abilities.isEmpty()) {
                    sender.sendMessage(miniMessage.deserialize("<red>No abilities are currently registered!"));
                    return;
                }
                sender.sendMessage(miniMessage.deserialize("<gradient:gold:yellow>══════ Registered Abilities ══════</gradient>"));
                for (String ability : abilities) {
                    sender.sendMessage(miniMessage.deserialize("<gray>» <green>" + ability));
                }
                break;

            case "items":
                List<String> items = plugin.getItemManager().getCustomItems();
                if (items.isEmpty()) {
                    sender.sendMessage(miniMessage.deserialize("<red>No custom items are currently registered!"));
                    return;
                }
                sender.sendMessage(miniMessage.deserialize("<gradient:gold:yellow>══════ Custom Items ══════</gradient>"));
                for (String item : items) {
                    sender.sendMessage(miniMessage.deserialize("<gray>» <green>" + item));
                }
                break;

            default:
                sender.sendMessage(miniMessage.deserialize("<red>Invalid list type! Use <yellow>abilities</yellow> or <yellow>items</yellow>"));
                break;
        }
    }

    private void handleGive(CommandSender sender, String playerName, String itemName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(miniMessage.deserialize("<red>⚠ Player <yellow>" + playerName + "</yellow> not found!"));
            return;
        }

        List<String> items = plugin.getItemManager().getCustomItems();
        if (!items.contains(itemName)) {
            sender.sendMessage(miniMessage.deserialize("<red>⚠ Item <yellow>" + itemName + "</yellow> does not exist!"));
            return;
        }

        plugin.getItemManager().giveCustomItem(target, itemName);
        sender.sendMessage(miniMessage.deserialize("<green>✓ Given <yellow>" + itemName + "</yellow> to <yellow>" + target.getName() + "</yellow>!"));
        target.sendMessage(miniMessage.deserialize("<green>✓ Received custom item: <yellow>" + itemName + "</yellow>!"));
    }

    private void handleAssign(CommandSender sender, String playerName, String abilityName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(miniMessage.deserialize("<red>⚠ Player <yellow>" + playerName + "</yellow> not found!"));
            return;
        }

        List<String> abilities = plugin.getAbilityManager().getAbilities();
        if (!abilities.contains(abilityName)) {
            sender.sendMessage(miniMessage.deserialize("<red>⚠ Ability <yellow>" + abilityName + "</yellow> does not exist!"));
            return;
        }

        plugin.getAbilityManager().getAbilityPDT().setAbility(target, abilityName);
        sender.sendMessage(miniMessage.deserialize("<green>✓ Assigned <yellow>" + abilityName + "</yellow> to <yellow>" + target.getName() + "</yellow>!"));
        target.sendMessage(miniMessage.deserialize("<green>✓ You have been granted the ability: <yellow>" + abilityName + "</yellow>!"));
    }

    private void handleUnassign(CommandSender sender, String playerName, String abilityName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(miniMessage.deserialize("<red>⚠ Player <yellow>" + playerName + "</yellow> not found!"));
            return;
        }

        List<String> abilities = plugin.getAbilityManager().getAbilities();
        if (!abilities.contains(abilityName)) {
            sender.sendMessage(miniMessage.deserialize("<red>⚠ Ability <yellow>" + abilityName + "</yellow> does not exist!"));
            return;
        }

        plugin.getAbilityManager().removeOnlinePlayer(target, abilityName);
        sender.sendMessage(miniMessage.deserialize("<green>✓ Removed ability <yellow>" + abilityName + "</yellow> from <yellow>" + target.getName() + "</yellow>!"));
        target.sendMessage(miniMessage.deserialize("<red>Your ability <yellow>" + abilityName + "</yellow> has been removed!"));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        String[] current_args = strings;

        switch (current_args[0]) {
            case "list":
                if (current_args.length == 2) {
                    return List.of("abilities", "items");
                }
                break;

            case "give":
                if (current_args.length == 2) {
                    return plugin.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
                }
                if (current_args.length == 3) {
                    return plugin.getItemManager().getCustomItems();
                }
                break;

            case "assign":
                if (current_args.length == 2) {
                    return plugin.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
                }
                if (current_args.length == 3) {
                    return plugin.getAbilityManager().getAbilities();
                }
                break;

            case "unassign":
                if (current_args.length == 2) {
                    return plugin.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
                }
                if (current_args.length == 3) {
                    return plugin.getAbilityManager().getAbilities();
                }
                break;

            default:
                if (current_args.length == 1) {
                    return List.of("help", "give", "assign", "unassign", "list");
                }
        }
        return List.of();
    }
}