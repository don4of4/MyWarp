package me.taylorkelly.mywarp;

import java.io.File;

public class WarpSettings {
    
    private static final String settingsFile = "MyWarp.settings";
    public static File dataDir;

    public static int maxPublic;
    public static int maxPrivate;
    public static boolean adminsObeyLimits;
    public static boolean adminPrivateWarps;
    public static boolean loadChunks;
    
    public static boolean usemySQL;
    public static String mySQLuname;
    public static String mySQLpass;
    public static String mySQLconn;

    public static void initialize(File dataFolder) {
        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File configFile  = new File(dataFolder, settingsFile);
        PropertiesFile file = new PropertiesFile(configFile);
        dataDir = dataFolder;
        
        maxPublic = file.getInt("maxPublic", 5, "Maximum number of public warps any player can make");
        maxPrivate = file.getInt("maxPrivate", 10, "Maximum number of private warps any player can make");
        adminsObeyLimits = file.getBoolean("adminsObeyLimits", false, "Whether or not admins can disobey warp limits");
        adminPrivateWarps = file.getBoolean("adminPrivateWarps", true, "Whether or not admins can see private warps in their list");
        loadChunks = file.getBoolean("loadChunks", false, "Force sending of the chunk which people teleport to - default: false");
        
        usemySQL = file.getBoolean("usemySQL", false, "MySQL usage --  true = use MySQL database / false = use SQLite");
		mySQLconn = file.getString("mySQLconn", "jdbc:mysql://localhost:3306/minecraft", "MySQL Connection (only if using MySQL)");
		mySQLuname = file.getString("mySQLuname", "root", "MySQL Username (only if using MySQL)");
		mySQLpass = file.getString("mySQLpass", "password", "MySQL Password (only if using MySQL)");
		
        file.save();
    }
}
