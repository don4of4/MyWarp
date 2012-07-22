package me.taylorkelly.mywarp.permissions;

import me.taylorkelly.mywarp.utils.WarpLogger;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class PermissionsHandler implements IPermissionsHandler {
    private enum PermHandler {
        VAULT, SUPERPERMS, NONE
    }

    private static PermHandler pEnum = PermHandler.NONE;
    private transient IPermissionsHandler handler = new NullHandler();
    private final transient Plugin plugin;

    public PermissionsHandler(final Plugin instance) {
        plugin = instance;
        checkPermissions();
    }

    @Override
    public boolean hasPermission(Player player, String node, boolean defaultPerm) {
        return handler.hasPermission(player, node, defaultPerm);
    }

    @Override
    public int getInteger(Player player, String node, int defaultInt) {
        return handler.getInteger(player, node, defaultInt);
    }

    public void checkPermissions() {
        final PluginManager pluginManager = plugin.getServer().getPluginManager();

        final Plugin vaultP = pluginManager.getPlugin("Vault");
        if (vaultP != null && vaultP.isEnabled()) {
            if (!(handler instanceof VaultHandler)) {
                pEnum = PermHandler.VAULT;
                String version = vaultP.getDescription().getVersion();
                WarpLogger.info("Access Control: Using Vault v"+ version);
                handler = new VaultHandler(plugin);
            }
            return;
        }

        if (pEnum == PermHandler.NONE) {
            if (!(handler instanceof SuperpermsHandler)) {
                WarpLogger.info("Access Control: Using SuperPerms");
                handler = new SuperpermsHandler(this.plugin);
            }
        }
    }
}