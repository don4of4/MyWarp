package me.taylorkelly.mywarp.permissions;

import me.taylorkelly.mywarp.WarpSettings;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WarpPermissions {
	private transient static PermissionsHandler permissionsHandler;

	public static void initialize(Plugin plugin) {
		permissionsHandler = new PermissionsHandler(plugin);
	}
	
	public static int integer(Player player, String node, int defaultInt) {
		return permissionsHandler.getInteger(player, node, defaultInt);
	}

    public static boolean isAdmin(Player player) {
        return player.hasPermission(player, "mywarp.admin", player.isOp());
    }

    public static boolean warp(Player player) {
            return player.hasPermission("mywarp.warp.basic.warp");
    }

    public static boolean delete(Player player) {
            return player.hasPermission("mywarp.warp.basic.delete");
    }

    public static boolean list(Player player) {
            return player.hasPermission("mywarp.warp.basic.list");
    }

    public static boolean welcome(Player player) {
            return player.hasPermission("mywarp.warp.basic.welcome");
    }

    public static boolean search(Player player) {
            return player.hasPermission("mywarp.warp.basic.search");
    }

    public static boolean give(Player player) {
            return player.hasPermission("mywarp.warp.soc.give");
    }

    public static boolean invite(Player player) {
            return player.hasPermission("mywarp.warp.soc.invite");
    }

    public static boolean uninvite(Player player) {
            return player.hasPermission("mywarp.warp.soc.uninvite");
    }

    public static boolean canPublic(Player player) {
            return player.hasPermission("mywarp.warp.soc.public");
    }

    public static boolean canPrivate(Player player) {
            return player.hasPermission("mywarp.warp.soc.private");
    }

    public static boolean signWarp(Player player) {
            return player.hasPermission("mywarp.warp.sign.warp");
    }

    public static boolean privateCreate(Player player) {
            return player.hasPermission("mywarp.warp.basic.createprivate";
    }
    
    public static boolean publicCreate(Player player) {
            return player.hasPermission("mywarp.warp.basic.createpublic");
    }
    
    public static boolean compass(Player player) {
            return player.hasPermission("mywarp.warp.basic.compass");
    }

    public static int maxPrivateWarps(Player player) {
        return WarpSettings.maxPrivate;
    }
    
    public static int maxPublicWarps(Player player) {
        return WarpSettings.maxPublic;
    }

    public static boolean createSignWarp(Player player) {
            return player.hasPermission("mywarp.warp.sign.create");
    }
}