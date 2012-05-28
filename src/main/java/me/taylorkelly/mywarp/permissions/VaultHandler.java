package me.taylorkelly.mywarp.permissions;

import me.taylorkelly.mywarp.WarpSettings;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHandler implements IPermissionsHandler {
	private transient Permission handler;

	public VaultHandler(final Plugin permissionsPlugin) {
        RegisteredServiceProvider<Permission> vPerm = permissionsPlugin.getServer().getServicesManager().getRegistration(Permission.class);

        handler = null;

        if (vPerm != null)
		    handler = vPerm.getProvider();
	}

	@Override
	public boolean hasPermission(final Player player, final String node, boolean defaultPerm) {
		if (handler == null)
            return defaultPerm;

        return handler.has(player, node);
	}

	@Override
	public int getInteger(final Player player, final String node, final int defaultInt) {
        if(player.isOp() && WarpSettings.opPermissions)
            return 0;

        return defaultInt;
	}
}

