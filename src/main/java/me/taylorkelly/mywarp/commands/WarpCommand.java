package me.taylorkelly.mywarp.commands;

import me.taylorkelly.mywarp.MyWarp;
import me.taylorkelly.mywarp.WarpSettings;
import me.taylorkelly.mywarp.data.Searcher;
import me.taylorkelly.mywarp.data.Warp;
import me.taylorkelly.mywarp.permissions.WarpPermissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand implements CommandExecutor {
    MyWarp plugin;

    public WarpCommand(MyWarp instance) {
        plugin = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String commandName = command.getName().toLowerCase();

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length > 0 && args[0].equalsIgnoreCase("reload") && WarpPermissions.isAdmin(player)) {
                WarpSettings.initialize(plugin.getDataFolder());
                player.sendMessage("[MyWarp] Reloading config");
            } else if (args.length > 0 && args[0].equalsIgnoreCase("list") && WarpPermissions.list(player)) {
                plugin.getServer().dispatchCommand(player, "mywarp stats");
            } else if (args.length > 0 && args[0].equalsIgnoreCase("slist") && WarpPermissions.list(player)) {
                plugin.warpList.list(player);
            } else if (args.length > 0 && args[0].equalsIgnoreCase("stats") && WarpPermissions.list(player)) {
                Integer maxPubWarps = WarpPermissions.maxPublicWarps(player);
                Integer maxPrivWarps = WarpPermissions.maxPrivateWarps(player);

                String puWarps = "";
                String prWarps = "";

                Integer pubWarps = 0;
                Integer privWarps = 0;

                for (Warp warp : plugin.warpList.getAllWarps()) {
                    if (!warp.creator.equalsIgnoreCase(player.getName()))
                        continue;

                    if (warp.publicAll) {
                        puWarps += warp.name + ", ";
                        pubWarps++;
                    } else {
                        prWarps += warp.name + ", ";
                        privWarps++;
                    }
                }

                puWarps = puWarps.trim();
                prWarps = prWarps.trim();

                player.sendMessage(ChatColor.RED + "-------------------- " + ChatColor.WHITE + "YOUR WARPS" + ChatColor.RED + " --------------------");
                player.sendMessage(ChatColor.RED + "Private Warps: " + ChatColor.WHITE + privWarps + ChatColor.GRAY + "/" + ChatColor.WHITE + maxPrivWarps);
                player.sendMessage(ChatColor.RED + "Private Warps List: " + ChatColor.WHITE + prWarps);
                player.sendMessage(ChatColor.RED + "Public Warps: " + ChatColor.WHITE + pubWarps + ChatColor.GRAY + "/" + ChatColor.WHITE + maxPubWarps);
                player.sendMessage(ChatColor.RED + "Public Warps List: " + ChatColor.WHITE + puWarps);
                player.sendMessage(ChatColor.RED + "--------------------       --------------------");
            } else if (args.length > 1 && args[0].equalsIgnoreCase("search") && WarpPermissions.search(player)) {
                String name = "";

                for (int i = 1; i < args.length; i++) {
                    name += args[i];
                    if (i + 1 < args.length) {
                        name += " ";
                    }
                }

                Searcher searcher = new Searcher(plugin.warpList);
                searcher.addPlayer(player);
                searcher.setQuery(name);
                searcher.search();
            } else if (args.length > 1 && (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("set"))
                    && (WarpPermissions.publicCreate(player) || WarpPermissions.privateCreate(player))) {
                String name = "";
                for (int i = 1; i < args.length; i++) {
                    name += args[i];
                    if (i + 1 < args.length) {
                        name += " ";
                    }
                }
                if (WarpPermissions.publicCreate(player)) {
                    plugin.warpList.addWarp(name, player);
                } else {
                    plugin.warpList.addWarpPrivate(name, player);
                }
            } else if (args.length > 1 && args[0].equalsIgnoreCase("point") && WarpPermissions.compass(player)) {
                String name = "";
                for (int i = 1; i < args.length; i++) {
                    name += args[i];
                    if (i + 1 < args.length) {
                        name += " ";
                    }
                }
                plugin.warpList.point(name, player);
            } else if (args.length > 1 && args[0].equalsIgnoreCase("pcreate") && WarpPermissions.privateCreate(player)) {
                String name = "";
                for (int i = 1; i < args.length; i++) {
                    name += args[i];
                    if (i + 1 < args.length) {
                        name += " ";
                    }
                }

                plugin.warpList.addWarpPrivate(name, player);
            } else if (args.length > 1 && args[0].equalsIgnoreCase("delete") && WarpPermissions.delete(player)) {
                String name = "";
                for (int i = 1; i < args.length; i++) {
                    name += args[i];
                    if (i + 1 < args.length) {
                        name += " ";
                    }
                }

                plugin.warpList.deleteWarp(name, player);
            } else if (args.length > 1 && args[0].equalsIgnoreCase("welcome") && WarpPermissions.welcome(player)) {
                String name = "";
                for (int i = 1; i < args.length; i++) {
                    name += args[i];
                    if (i + 1 < args.length) {
                        name += " ";
                    }
                }

                plugin.warpList.welcomeMessage(name, player);
            } else if (args.length > 1 && args[0].equalsIgnoreCase("private") && WarpPermissions.canPrivate(player)) {
                String name = "";
                for (int i = 1; i < args.length; i++) {
                    name += args[i];
                    if (i + 1 < args.length) {
                        name += " ";
                    }
                }

                plugin.warpList.privatize(name, player);
            } else if (args.length > 1 && args[0].equalsIgnoreCase("public") && WarpPermissions.canPublic(player)) {
                String name = "";
                for (int i = 1; i < args.length; i++) {
                    name += args[i];
                    if (i + 1 < args.length) {
                        name += " ";
                    }
                }

                plugin.warpList.publicize(name, player);
            } else if (args.length > 2 && args[0].equalsIgnoreCase("give") && WarpPermissions.give(player)) {
                Player givee = plugin.getServer().getPlayer(args[1]);
                // TODO Change to matchPlayer
                String giveeName = (givee == null) ? args[1] : givee.getName();

                String name = "";
                for (int i = 2; i < args.length; i++) {
                    name += args[i];
                    if (i + 1 < args.length) {
                        name += " ";
                    }
                }

                plugin.warpList.give(name, player, giveeName);
            } else if (args.length > 2 && args[0].equalsIgnoreCase("invite") && WarpPermissions.invite(player)) {
                Player invitee = plugin.getServer().getPlayer(args[1]);
                // TODO Change to matchPlayer
                String inviteeName = (invitee == null) ? args[1] : invitee.getName();

                String name = "";
                for (int i = 2; i < args.length; i++) {
                    name += args[i];
                    if (i + 1 < args.length) {
                        name += " ";
                    }
                }

                plugin.warpList.invite(name, player, inviteeName);
            } else if (args.length > 2 && args[0].equalsIgnoreCase("uninvite") && WarpPermissions.uninvite(player)) {
                Player invitee = plugin.getServer().getPlayer(args[1]);
                String inviteeName = (invitee == null) ? args[1] : invitee.getName();

                String name = "";
                for (int i = 2; i < args.length; i++) {
                    name += args[i];
                    if (i + 1 < args.length) {
                        name += " ";
                    }
                }

                plugin.warpList.uninvite(name, player, inviteeName);
            } else if (args.length > 2 && args[0].equalsIgnoreCase("player") && WarpPermissions.isAdmin(player)) {
                Player invitee = plugin.getServer().getPlayer(args[1]);
                //String inviteeName = (invitee == null) ? split[1] : invitee.getName();

                // TODO ChunkLoading
                String name = "";
                for (int i = 2; i < args.length; i++) {
                    name += args[i];
                    if (i + 1 < args.length) {
                        name += " ";
                    }
                }
                plugin.warpList.adminWarpTo(name, invitee, player);
            } else if (args.length > 0 && args[0].equalsIgnoreCase("help") || args.length == 0) {
                ArrayList<String> messages = new ArrayList<String>();
                messages.add(ChatColor.RED + "-------------------- " + ChatColor.WHITE + "/WARP HELP" + ChatColor.RED + " --------------------");
                if (WarpPermissions.warp(player)) {
                    messages.add(ChatColor.RED + "/warp [name]" + ChatColor.WHITE + "  -  Warp to " + ChatColor.GRAY + "[name]");
                }

                if (WarpPermissions.publicCreate(player) || WarpPermissions.privateCreate(player)) {
                    messages.add(ChatColor.RED + "/warp create [name]" + ChatColor.WHITE + "  -  Create warp " + ChatColor.GRAY + "[name]");
                }

                if (WarpPermissions.privateCreate(player)) {
                    messages.add(ChatColor.RED + "/warp pcreate [name]" + ChatColor.WHITE + "  -  Create warp " + ChatColor.GRAY + "[name]");
                }

                if (WarpPermissions.delete(player)) {
                    messages.add(ChatColor.RED + "/warp delete [name]" + ChatColor.WHITE + "  -  Delete warp " + ChatColor.GRAY + "[name]");
                }

                if (WarpPermissions.welcome(player)) {
                    messages.add(ChatColor.RED + "/warp welcome [name]" + ChatColor.WHITE + "  -  Change the welcome message of " + ChatColor.GRAY
                            + "[name]");
                }

                if (WarpPermissions.list(player)) {
                    messages.add(ChatColor.RED + "/warp list (#)" + ChatColor.WHITE + "  -  Views warp page " + ChatColor.GRAY + "(#)");
                }

                if (WarpPermissions.search(player)) {
                    messages.add(ChatColor.RED + "/warp search [query]" + ChatColor.WHITE + "  -  Search for " + ChatColor.GRAY + "[query]");
                }

                if (WarpPermissions.give(player)) {
                    messages.add(ChatColor.RED + "/warp give [player] [name[" + ChatColor.WHITE + "  -  Give " + ChatColor.GRAY + "[player]"
                            + ChatColor.WHITE + " your " + ChatColor.GRAY + "[name]");
                }

                if (WarpPermissions.invite(player)) {
                    messages.add(ChatColor.RED + "/warp invite [player] [name]" + ChatColor.WHITE + "  -  Invite " + ChatColor.GRAY + "[player]"
                            + ChatColor.WHITE + " to " + ChatColor.GRAY + "[name]");
                }

                if (WarpPermissions.uninvite(player)) {
                    messages.add(ChatColor.RED + "/warp uninvite [player] [name[" + ChatColor.WHITE + "  -  Uninvite " + ChatColor.GRAY + "[player]"
                            + ChatColor.WHITE + " to " + ChatColor.GRAY + "[name]");
                }

                if (WarpPermissions.canPublic(player)) {
                    messages.add(ChatColor.RED + "/warp public [name]" + ChatColor.WHITE + "  -  Makes warp " + ChatColor.GRAY + "[name]" + ChatColor.WHITE
                            + " public");
                }

                if (WarpPermissions.canPrivate(player)) {
                    messages.add(ChatColor.RED + "/warp private [name]" + ChatColor.WHITE + "  -  Makes warp " + ChatColor.GRAY + "[name]"
                            + ChatColor.WHITE + " private");
                }

                for (String message : messages) {
                    player.sendMessage(message);
                }
            } else if (args.length > 0 && WarpPermissions.warp(player)) {
                // TODO ChunkLoading
                String name = "";
                for (int i = 0; i < args.length; i++) {
                    name += args[i];
                    if (i + 1 < args.length) {
                        name += " ";
                    }
                }
                plugin.warpList.warpTo(name, player);
            } else {
                return false;
            }

            return true;
        } else {
            if (args.length > 0 && args[0].equalsIgnoreCase("validate")) {
                List<Warp> warps = plugin.warpList.getAllWarps();

                Integer prWarp = 0;
                Integer puWarp = 0;

                for (Player players : Bukkit.getServer().getOnlinePlayers()) {
                    Integer maxPuWarps = WarpPermissions.maxPublicWarps(players);
                    Integer maxPrWarps = WarpPermissions.maxPrivateWarps(players);

                    Integer puWarpCount = 0;
                    Integer prWarpCount = 0;

                    for (Warp warp : warps) {
                        if (!warp.creator.equalsIgnoreCase(players.getName()))
                            continue;

                        if (warp.publicAll) {
                            if (puWarpCount > maxPuWarps) {
                                plugin.warpList.deleteWarp(warp.name);
                                puWarp++;
                                continue;
                            }

                            puWarpCount++;
                        } else {
                            if (prWarpCount > maxPrWarps) {
                                plugin.warpList.deleteWarp(warp.name);
                                prWarp++;
                                continue;
                            }

                            prWarpCount++;
                        }
                    }
                }

                sender.sendMessage(ChatColor.RED + "Warps Validated. " + puWarp + " Public Warps Deleted. " + prWarp + " Private Warps Deleted.");

                return true;
            }
        }

        return false;
    }
}
