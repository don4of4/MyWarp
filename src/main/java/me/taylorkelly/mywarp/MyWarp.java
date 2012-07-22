package me.taylorkelly.mywarp;

import me.taylorkelly.mywarp.commands.WarpCommand;
import me.taylorkelly.mywarp.data.WarpList;
import me.taylorkelly.mywarp.griefcraft.Updater;
import me.taylorkelly.mywarp.listeners.MWBlockListener;
import me.taylorkelly.mywarp.listeners.MWPlayerListener;
import me.taylorkelly.mywarp.permissions.WarpPermissions;
import me.taylorkelly.mywarp.sql.ConnectionManager;
import me.taylorkelly.mywarp.utils.WarpHelp;
import me.taylorkelly.mywarp.utils.WarpLogger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyWarp extends JavaPlugin {

    public WarpList warpList;
    public String name;
    public String version;
    public static final Logger log = Logger.getLogger("Minecraft");

    @Override
    public void onDisable() {
        ConnectionManager.closeConnection();
    }

    @Override
    public void onEnable() {
        name = this.getDescription().getName();
        version = this.getDescription().getVersion();
        PluginManager pm = getServer().getPluginManager();
        
        WarpSettings.initialize(getDataFolder());
        
        libCheck();
        if(!sqlCheck()) { return; }

        Connection conn = ConnectionManager.initialize();

        if (conn == null) {
            log.log(Level.SEVERE, "[MYWARP] Could not establish SQL connection. Disabling MyWarp");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        warpList = new WarpList(getServer());
        MWBlockListener blockListener = new MWBlockListener(warpList);
        MWPlayerListener playerListener = new MWPlayerListener(warpList);

        WarpPermissions.initialize(this);
        WarpHelp.initialize(this);
        
        pm.registerEvents(blockListener, this);
        pm.registerEvents(playerListener, this);

        getCommand("mywarp").setExecutor(new WarpCommand(this));
        
        WarpLogger.info(name + " " + version + " enabled");
    }


    private void libCheck(){
        Updater updater = new Updater();
        try {
            updater.check();
            updater.update();
        } catch (Exception ignored) {}
    }
    
    private boolean sqlCheck() {
        Connection conn = ConnectionManager.initialize();
        if (conn == null) {
            WarpLogger.severe("Could not establish SQL connection. Disabling MyWarp");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        } 
        return true;
    }
    
    private void updateFiles(File oldDatabase, File newDatabase) {
        if (!getDataFolder().exists())
            getDataFolder().mkdirs();

        if (newDatabase.exists())
            newDatabase.delete();

        try {
            newDatabase.createNewFile();
        } catch (IOException ex) {
        	WarpLogger.severe("Could not create new database file", ex);
        }

        copyFile(oldDatabase, newDatabase);
    }

    /**
     * File copier from xZise
     * @param fromFile
     * @param toFile
     */
    private static void copyFile(File fromFile, File toFile) {
        FileInputStream from = null;
        FileOutputStream to = null;
        try {
            from = new FileInputStream(fromFile);
            to = new FileOutputStream(toFile);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = from.read(buffer)) != -1) {
                to.write(buffer, 0, bytesRead);
            }
        } catch (IOException ex) {
            Logger.getLogger(MyWarp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (from != null) {
                try {
                    from.close();
                } catch (IOException ignored) {}
            }
            if (to != null) {
                try {
                    to.close();
                } catch (IOException ignored) {}
            }
        }
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void severe(String string, Exception ex) {
        log.log(Level.SEVERE, "[MYHOME]" + string, ex);

    }

    public static void severe(String string) {
        log.log(Level.SEVERE, "[MYHOME]" + string);
    }
}
