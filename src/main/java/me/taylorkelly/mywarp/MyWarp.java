package me.taylorkelly.mywarp;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.taylorkelly.mywarp.griefcraft.Updater;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

public class MyWarp extends JavaPlugin {
    private WarpList warpList;
    private MWPlayerListener playerListener;
    private MWBlockListener blockListener;

    public final String name = this.getDescription().getName();
    public final String version = this.getDescription().getVersion();

    private Updater updater;

    public MyWarp(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
        updater = new Updater();

    }

    public void onDisable() {
        ConnectionManager.freeConnection();
    }

    public void onEnable() {
        Logger log = Logger.getLogger("Minecraft");

        try {
            updater.check();
            updater.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Connection conn = ConnectionManager.initializeConnection(getServer());
        if (conn == null) {
            log.log(Level.SEVERE, "[MYWARP] Could not establish SQL connection. Disabling MyWarp");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (new File("MyWarp").exists() && new File("MyWarp", "warps.db").exists()) {
            updateFiles();
        }

        warpList = new WarpList(getServer());
        blockListener = new MWBlockListener(warpList);
        playerListener = new MWPlayerListener(warpList);

        WarpPermissions.initialize(getServer());
        WarpSettings.initialize(getDataFolder());

        getServer().getPluginManager().registerEvent(Type.PLAYER_CHAT, playerListener, Priority.Low, this);
        getServer().getPluginManager().registerEvent(Type.BLOCK_RIGHTCLICKED, blockListener, Priority.High, this);
        log.info(name + " " + version + " enabled");
    }

    private void updateFiles() {
        File file = new File("MyWarp", "warps.db");
        File folder = new File("MyWarp");
        file.renameTo(new File("homes-warps.db"));
        folder.delete();
    }

    private boolean warning;

    public boolean onCommand(Player player, Command command, String commandLabel, String[] args) {
        String[] split = args;
        String commandName = command.getName().toLowerCase();

        // TODO permissions
        if (commandName.equals("warp")) {
            /**
             * /warp convert
             */
            if (split.length == 1 && split[0].equalsIgnoreCase("convert") && WarpPermissions.isAdmin(player)) {
                if (!warning) {
                    player.sendMessage(ChatColor.RED + "Warning: " + ChatColor.WHITE + "Only use a copy of warps.txt.");
                    player.sendMessage("This will delete the warps.txt it uses");
                    player.sendMessage("Use " + ChatColor.RED + "'/warp convert'" + ChatColor.WHITE + " again to confirm.");
                    warning = true;
                } else {
                    Converter.convert(player, getServer(), warpList);
                    warning = false;
                }
                /**
                 * /warp list or /warp list #
                 */
            } else if ((split.length == 1 || (split.length == 2 && isInteger(split[1]))) && split[0].equalsIgnoreCase("list") && WarpPermissions.list(player)) {
                Lister lister = new Lister(warpList);
                lister.addPlayer(player);

                if (split.length == 2) {
                    int page = Integer.parseInt(split[1]);
                    if (page < 1) {
                        player.sendMessage(ChatColor.RED + "Page number can't be below 1.");
                        return true;
                    } else if (page > lister.getMaxPages(player)) {
                        player.sendMessage(ChatColor.RED + "There are only " + lister.getMaxPages(player) + " pages of warps");
                        return true;
                    }
                    lister.setPage(page);
                } else {
                    lister.setPage(1);
                }
                lister.list();

                /**
                 * /warp slist
                 */
            } else if (split.length == 1 && split[0].equalsIgnoreCase("slist") && WarpPermissions.list(player)) {
                warpList.list(player);
                /**
                 * /warp search <name>
                 */
            } else if (split.length > 1 && split[0].equalsIgnoreCase("search") && WarpPermissions.search(player)) {
                String name = "";
                for (int i = 1; i < split.length; i++) {
                    name += split[i];
                    if (i + 1 < split.length)
                        name += " ";
                }

                Searcher searcher = new Searcher(warpList);
                searcher.addPlayer(player);
                searcher.setQuery(name);
                searcher.search();
                /**
                 * /warp create <name>
                 */
            } else if (split.length > 1 && split[0].equalsIgnoreCase("create") && WarpPermissions.create(player)) {
                String name = "";
                for (int i = 1; i < split.length; i++) {
                    name += split[i];
                    if (i + 1 < split.length)
                        name += " ";
                }

                warpList.addWarp(name, player);
                /**
                 * /warp pcreate <name>
                 */
            } else if (split.length > 1 && split[0].equalsIgnoreCase("pcreate") && WarpPermissions.create(player) && WarpPermissions.canPrivate(player)) {
                String name = "";
                for (int i = 1; i < split.length; i++) {
                    name += split[i];
                    if (i + 1 < split.length)
                        name += " ";
                }

                warpList.addWarpPrivate(name, player);
                /**
                 * /warp delete <name>
                 */
            } else if (split.length > 1 && split[0].equalsIgnoreCase("delete") && WarpPermissions.delete(player)) {
                String name = "";
                for (int i = 1; i < split.length; i++) {
                    name += split[i];
                    if (i + 1 < split.length)
                        name += " ";
                }

                warpList.deleteWarp(name, player);
                /**
                 * /warp welcome <name>
                 */
            } else if (split.length > 1 && split[0].equalsIgnoreCase("welcome") && WarpPermissions.welcome(player)) {
                String name = "";
                for (int i = 1; i < split.length; i++) {
                    name += split[i];
                    if (i + 1 < split.length)
                        name += " ";
                }

                warpList.welcomeMessage(name, player);
                /**
                 * /warp private <name>
                 */
            } else if (split.length > 1 && split[0].equalsIgnoreCase("private") && WarpPermissions.canPrivate(player)) {
                String name = "";
                for (int i = 1; i < split.length; i++) {
                    name += split[i];
                    if (i + 1 < split.length)
                        name += " ";
                }

                warpList.privatize(name, player);
                /**
                 * /warp public <name>
                 */
            } else if (split.length > 1 && split[0].equalsIgnoreCase("public") && WarpPermissions.canPublic(player)) {
                String name = "";
                for (int i = 1; i < split.length; i++) {
                    name += split[i];
                    if (i + 1 < split.length)
                        name += " ";
                }

                warpList.publicize(name, player);

                /**
                 * /warp give <player> <name>
                 */
            } else if (split.length > 2 && split[0].equalsIgnoreCase("give") && WarpPermissions.give(player)) {
                Player givee = getServer().getPlayer(split[1]);
                // TODO Change to matchPlayer
                String giveeName = (givee == null) ? split[1] : givee.getName();

                String name = "";
                for (int i = 2; i < split.length; i++) {
                    name += split[i];
                    if (i + 1 < split.length)
                        name += " ";
                }

                warpList.give(name, player, giveeName);

                /**
                 * /warp invite <player> <name>
                 */
            } else if (split.length > 2 && split[0].equalsIgnoreCase("invite") && WarpPermissions.invite(player)) {
                Player invitee = getServer().getPlayer(split[1]);
                // TODO Change to matchPlayer
                String inviteeName = (invitee == null) ? split[1] : invitee.getName();

                String name = "";
                for (int i = 2; i < split.length; i++) {
                    name += split[i];
                    if (i + 1 < split.length)
                        name += " ";
                }

                warpList.invite(name, player, inviteeName);
                /**
                 * /warp uninvite <player> <name>
                 */
            } else if (split.length > 2 && split[0].equalsIgnoreCase("uninvite") && WarpPermissions.uninvite(player)) {
                Player invitee = getServer().getPlayer(split[1]);
                // TODO Change to matchPlayer
                String inviteeName = (invitee == null) ? split[1] : invitee.getName();

                String name = "";
                for (int i = 2; i < split.length; i++) {
                    name += split[i];
                    if (i + 1 < split.length)
                        name += " ";
                }

                warpList.uninvite(name, player, inviteeName);

                /**
                 * /warp help
                 */
            } else if (split.length == 1 && split[0].equalsIgnoreCase("help")) {
                ArrayList<String> messages = new ArrayList<String>();
                messages.add(ChatColor.RED + "-------------------- " + ChatColor.WHITE + "/WARP HELP" + ChatColor.RED + " --------------------");
                if (WarpPermissions.warp(player)) {
                    messages.add(ChatColor.RED + "/warp <name>" + ChatColor.WHITE + "  -  Warp to " + ChatColor.GRAY + "<name>");
                }
                if (WarpPermissions.create(player)) {
                    messages.add(ChatColor.RED + "/warp create <name>" + ChatColor.WHITE + "  -  Create warp " + ChatColor.GRAY + "<name>");
                }

                if (WarpPermissions.delete(player)) {
                    messages.add(ChatColor.RED + "/warp delete <name>" + ChatColor.WHITE + "  -  Delete warp " + ChatColor.GRAY + "<name>");
                }

                if (WarpPermissions.welcome(player)) {
                    messages.add(ChatColor.RED + "/warp welcome <name>" + ChatColor.WHITE + "  -  Change the welcome message of " + ChatColor.GRAY + "<name>");
                }

                if (WarpPermissions.list(player)) {
                    messages.add(ChatColor.RED + "/warp list <#>" + ChatColor.WHITE + "  -  Views warp page " + ChatColor.GRAY + "<#>");
                }

                if (WarpPermissions.search(player)) {
                    messages.add(ChatColor.RED + "/warp search <query>" + ChatColor.WHITE + "  -  Search for " + ChatColor.GRAY + "<query>");
                }
                if (WarpPermissions.give(player)) {
                    messages.add(ChatColor.RED + "/warp give <player> <name>" + ChatColor.WHITE + "  -  Give " + ChatColor.GRAY + "<player>" + ChatColor.WHITE
                            + " your " + ChatColor.GRAY + "<name>");
                }
                if (WarpPermissions.invite(player)) {
                    messages.add(ChatColor.RED + "/warp invite <player> <name>" + ChatColor.WHITE + "  -  Invite " + ChatColor.GRAY + "<player>"
                            + ChatColor.WHITE + " to " + ChatColor.GRAY + "<name>");
                }
                if (WarpPermissions.uninvite(player)) {
                    messages.add(ChatColor.RED + "/warp uninvite <player> <name>" + ChatColor.WHITE + "  -  Uninvite " + ChatColor.GRAY + "<player>"
                            + ChatColor.WHITE + " to " + ChatColor.GRAY + "<name>");
                }
                if (WarpPermissions.canPublic(player)) {
                    messages.add(ChatColor.RED + "/warp public <name>" + ChatColor.WHITE + "  -  Makes warp " + ChatColor.GRAY + "<name>" + ChatColor.WHITE
                            + " public");
                }
                if (WarpPermissions.canPrivate(player)) {
                    messages.add(ChatColor.RED + "/warp private <name>" + ChatColor.WHITE + "  -  Makes warp " + ChatColor.GRAY + "<name>" + ChatColor.WHITE
                            + " private");
                }
                for (String message : messages) {
                    player.sendMessage(message);
                }

                /**
                 * /warp <name>
                 */
            } else if (split.length > 0 && WarpPermissions.warp(player)) {
                // TODO ChunkLoading
                String name = "";
                for (int i = 0; i < split.length; i++) {
                    name += split[i];
                    if (i + 1 < split.length)
                        name += " ";
                }
                warpList.warpTo(name, player);
            } else {
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Connection getConnection() {
        return ConnectionManager.getConnection();
    }
}