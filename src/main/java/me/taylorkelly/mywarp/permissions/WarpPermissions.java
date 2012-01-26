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
        return permissionsHandler.hasPermission(player, "mywarp.admin", player.isOp());
    }

    public static boolean warp(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.basic.warp", true);
    }

    public static boolean delete(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.basic.delete", true);
    }

    public static boolean list(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.basic.list", true);
    }

    public static boolean welcome(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.basic.welcome", true);
    }

    public static boolean search(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.basic.search", true);
    }

    public static boolean give(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.soc.give", true);
    }

    public static boolean invite(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.soc.invite", true);
    }

    public static boolean uninvite(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.soc.uninvite", true);
    }

    public static boolean canPublic(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.soc.public", true);
    }

    public static boolean canPrivate(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.soc.private", true);
    }

    public static boolean signWarp(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.sign.warp", true);
    }

    public static boolean privateCreate(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.basic.createprivate", true);
    }
    
    public static boolean publicCreate(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.basic.createpublic", true);
    }
    
    public static boolean compass(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.basic.compass", true);
    }

    public static int maxPrivateWarps(Player player) {
        return WarpSettings.maxPrivate;
    }
    
    public static int maxPublicWarps(Player player) {
        return WarpSettings.maxPublic;
    }

    public static boolean createSignWarp(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.sign.create", true);
    }
}