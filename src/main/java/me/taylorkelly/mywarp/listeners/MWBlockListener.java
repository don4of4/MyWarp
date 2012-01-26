package me.taylorkelly.mywarp.listeners;

import me.taylorkelly.mywarp.data.SignWarp;
import me.taylorkelly.mywarp.data.WarpList;
import me.taylorkelly.mywarp.permissions.WarpPermissions;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

public class MWBlockListener extends BlockListener
{
  public MWBlockListener(WarpList list)
  {
  }

  public void onSignChange(SignChangeEvent event)
  {
    Player player = event.getPlayer();

    if (SignWarp.isSignWarp(event))
      if (WarpPermissions.createSignWarp(player)) {
        player.sendMessage(ChatColor.AQUA + "Successfully created a SignWarp");
      }
      else {
        player.sendMessage(ChatColor.RED + "You do not have permission to create a SignWarp");
        event.setCancelled(true);
      }
  }
}