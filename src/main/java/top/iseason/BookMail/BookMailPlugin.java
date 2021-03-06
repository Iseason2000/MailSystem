package top.iseason.BookMail;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import top.iseason.BookMail.Manager.ConfigManager;
import top.iseason.BookMail.Manager.PackageManager;
import top.iseason.BookMail.Manager.SqlManager;
import top.iseason.BookMail.Manager.TimeManager;
import top.iseason.BookMail.command.MainCommand;
import top.iseason.BookMail.listener.PackageListener;
import top.iseason.BookMail.listener.PlayerListener;

import java.sql.SQLException;


import static top.iseason.BookMail.Util.Message.sendLog;

public class BookMailPlugin extends JavaPlugin implements Listener {
    private static BookMailPlugin plugin;
    private static TimeManager timeManager;
    private static ConfigManager configManager;

    public static BookMailPlugin getInstance() {
        return plugin;
    }

    public void onEnable() { //启用插件
        plugin = this;
        sendLog(ChatColor.AQUA + "███╗   ███╗ █████╗ ██╗██╗     ");
        sendLog(ChatColor.AQUA + "████╗ ████║██╔══██╗██║██║     ");
        sendLog(ChatColor.AQUA + "██╔████╔██║███████║██║██║     ");
        sendLog(ChatColor.AQUA + "██║╚██╔╝██║██╔══██║██║██║     ");
        sendLog(ChatColor.AQUA + "██║ ╚═╝ ██║██║  ██║██║███████╗");
        sendLog(ChatColor.AQUA + "╚═╝     ╚═╝╚═╝  ╚═╝╚═╝╚══════╝");
        saveDefaultConfig();
        PluginCommand command = Bukkit.getPluginCommand("BookMail");
        if (command != null) {
            command.setExecutor(new MainCommand());
            command.setTabCompleter(new MainCommand());
        }
        try {
            SqlManager.initSqilte();
            sendLog(ChatColor.GREEN + "数据库连接成功!");
        } catch (ClassNotFoundException | SQLException e) {
            sendLog(ChatColor.RED + "数据库连接失败!");
            e.printStackTrace();
        }
        Bukkit.getPluginManager().registerEvents(new PackageListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        sendLog(ChatColor.GREEN + "插件已启用! " + ChatColor.GOLD + "作者:" + ChatColor.BLUE + "Iceason");
        new PackageManager();
        timeManager = new TimeManager();
        configManager = new ConfigManager(this);
//        new CheckMCVersion(Bukkit.getVersion());
    }

    public void onDisable() {
        try {
            SqlManager.disableSqlite();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        saveDefaultConfig();
        sendLog(ChatColor.RED + "插件已注销!");
    }

    public static TimeManager getTimeManager() {
        return timeManager;
    }

    public static void setTimeManager(TimeManager timeManager) {
        BookMailPlugin.timeManager = timeManager;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

}
