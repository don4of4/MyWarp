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
        return permissionsHandler.hasPermission(player, "mywarp.admin", false);
    }

    public static boolean warp(Player player) {
        return permissionsHandler.hasPermission(player, "mywarp.warp.basic.warp", false);
    }

    public static boolean delete(Player player) {
        return permissionsHandler.hasPermission(player, "mywarp.warp.basic.delete", false);
    }

    public static boolean list(Player player) {
        return permissionsHandler.hasPermission(player, "mywarp.warp.basic.list", false);
    }

    public static boolean welcome(Player player) {
        return permissionsHandler.hasPermission(player, "mywarp.warp.basic.welcome", false);
    }

    public static boolean search(Player player) {
        return permissionsHandler.hasPermission(player, "mywarp.warp.basic.search", false);
    }

    public static boolean give(Player player) {
        return permissionsHandler.hasPermission(player, "mywarp.warp.soc.give", false);
    }

    public static boolean invite(Player player) {
        return permissionsHandler.hasPermission(player, "mywarp.warp.soc.invite", false);
    }

    public static boolean uninvite(Player player) {
        return permissionsHandler.hasPermission(player, "mywarp.warp.soc.uninvite", false);
    }

    public static boolean canPublic(Player player) {
        return permissionsHandler.hasPermission(player, "mywarp.warp.soc.public", false);
    }

    public static boolean canPrivate(Player player) {
        return permissionsHandler.hasPermission(player, "mywarp.warp.soc.private", false);
    }

    public static boolean signWarp(Player player) {
        return permissionsHandler.hasPermission(player, "mywarp.warp.sign.warp", false);
    }

    public static boolean privateCreate(Player player) {

        return permissionsHandler.hasPermission(player, "mywarp.warp.basic.createprivate", false);
    }

    public static boolean publicCreate(Player player) {
        return permissionsHandler.hasPermission(player, "mywarp.warp.basic.createpublic", false);
    }

    public static boolean compass(Player player) {
        return permissionsHandler.hasPermission(player, "mywarp.warp.basic.compass", false);
    }

    public static int maxPrivateWarps(Player player) {
        if (permissionsHandler.hasPermission(player, "mywarp.private.limit.unlimited", false)) {
            return 9999999;
        } else if (permissionsHandler.hasPermission(player, "mywarp.private.limit.100", false)) {
            return 100;
        } else if (permissionsHandler.hasPermission(player, "mywarp.private.limit.50", false)) {
            return 50;
        } else if (permissionsHandler.hasPermission(player, "mywarp.private.limit.25", false)) {
            return 25;
        } else if (permissionsHandler.hasPermission(player, "mywarp.private.limit.15", false)) {
            return 15;
        } else if (permissionsHandler.hasPermission(player, "mywarp.private.limit.10", false)) {
            return 10;
        }

        return WarpSettings.maxPrivate;
    }

    public static int maxPublicWarps(Player player) {
        if (permissionsHandler.hasPermission(player, "mywarp.public.limit.unlimited", false)) {
            return 9999999;
        } else if (permissionsHandler.hasPermission(player, "mywarp.public.limit.100", false)) {
            return 100;
        } else if (permissionsHandler.hasPermission(player, "mywarp.public.limit.50", false)) {
            return 50;
        } else if (permissionsHandler.hasPermission(player, "mywarp.public.limit.25", false)) {
            return 25;
        } else if (permissionsHandler.hasPermission(player, "mywarp.public.limit.15", false)) {
            return 15;
        } else if (permissionsHandler.hasPermission(player, "mywarp.public.limit.10", false)) {
            return 10;
        }

        return WarpSettings.maxPublic;
    }

    public static boolean createSignWarp(Player player) {
        return permissionsHandler.hasPermission(player, "mywarp.warp.sign.create", false);
    }
}
