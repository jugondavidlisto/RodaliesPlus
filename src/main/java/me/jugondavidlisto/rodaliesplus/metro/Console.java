package me.jugondavidlisto.rodaliesplus.metro;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import me.jugondavidlisto.rodaliesplus.listeners.BlockBreakListener;
import me.jugondavidlisto.rodaliesplus.listeners.InventoryCloseListener;
import me.jugondavidlisto.rodaliesplus.listeners.InventoryListener;
import me.jugondavidlisto.rodaliesplus.listeners.MetroCMD;
import me.jugondavidlisto.rodaliesplus.listeners.PlayerMoveListener;
import me.jugondavidlisto.rodaliesplus.listeners.SignClickListener;
import me.jugondavidlisto.rodaliesplus.listeners.SignCreateListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import me.jugondavidlisto.rodaliesplus.utils.GUIManager;
import me.jugondavidlisto.rodaliesplus.utils.Ticket;
import me.jugondavidlisto.rodaliesplus.utils.TicketManager;

public class Console
extends JavaPlugin {
    public static Console instance;
    Logger logger = Logger.getLogger("Minecraft");
    public static HashMap<Player, Ticket> listeningto;
    public static HashMap<Player, ItemStack> LastListeningTo;
    public static HashMap<Player, Block> listenWalking;
    public static HashMap<Player, Location> listenGate;
    public static HashMap<Player, Ticket> listenTicket;
    public static ArrayList<Player> listenNonTopup;
    public static ArrayList<Player> listenTopup;
    /*public static Permission viewcmd;
    public static Permission createTicketSign;
    public static Permission useTicketSign;
    public static Permission createFaregateSign;
    public static Permission useFaregateSign;*/
    private File datafile = null;
    private FileConfiguration datayml = null;
    private File configfile;
    public static Economy economy;

    static {
        listeningto = new HashMap();
        LastListeningTo = new HashMap();
        listenWalking = new HashMap();
        listenGate = new HashMap();
        listenTicket = new HashMap();
        listenNonTopup = new ArrayList();
        listenTopup = new ArrayList();
        /*viewcmd = new Permission("metroplus.view");
        createTicketSign = new Permission("metroplus.createticketsign");
        useTicketSign = new Permission("metroplus.useticketsign");
        createFaregateSign = new Permission("metroplus.createfaregatesign");
        useFaregateSign = new Permission("metroplus.usefaregatesign");*/
        economy = null;
    }
    
    private static final String LOG_TAG = "[Metro+] ";

    public Console() {
        this.configfile = new File(this.getDataFolder(), "config.yml");
    }

    public void onEnable() {
        instance = this;
        this.initialize();
        this.logger.info(LOG_TAG + "has been enabledd.");
        if (!this.setupEconomy()) {
            this.logger.info(LOG_TAG + "This plugin is disabling due to Vault is not installed!");
            this.getServer().getPluginManager().disablePlugin((Plugin)this);
        }
    }

    public void onDisable() {
        this.logger.info(LOG_TAG + "has been disabled.");
    }

    public static Console getInstance() {
        return instance;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider economyProvider = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = (Economy)economyProvider.getProvider();
        }
        if (economy != null) {
            return true;
        }
        return false;
    }

    public void initialize() {
        this.logger.info(LOG_TAG + "initialized");
        if (this.getServer().getPluginManager().getPlugin("Vault") != null && this.getServer().getPluginManager().getPlugin("Vault").isEnabled()) {
            this.reloadDataConfig();
            this.saveDataConfig();
            this.saveDefaultDataConfig();
            if (!this.configfile.exists()) {
                this.getConfig().options().copyDefaults(true);
                this.saveConfig();
                this.saveDefaultConfig();
            }
            /*this.getServer().getPluginManager().addPermission(viewcmd);
            this.getServer().getPluginManager().addPermission(createTicketSign);
            this.getServer().getPluginManager().addPermission(useTicketSign);
            this.getServer().getPluginManager().addPermission(useFaregateSign);
            this.getServer().getPluginManager().addPermission(createFaregateSign);*/
            new me.jugondavidlisto.rodaliesplus.utils.GUIManager();
            new me.jugondavidlisto.rodaliesplus.utils.TicketManager();
            new me.jugondavidlisto.rodaliesplus.listeners.SignCreateListener();
            new me.jugondavidlisto.rodaliesplus.listeners.SignClickListener();
            new me.jugondavidlisto.rodaliesplus.listeners.BlockBreakListener();
            new me.jugondavidlisto.rodaliesplus.listeners.InventoryListener();
            new me.jugondavidlisto.rodaliesplus.listeners.InventoryCloseListener();
            new me.jugondavidlisto.rodaliesplus.listeners.PlayerMoveListener();
            this.getCommand("metroplus").setExecutor((CommandExecutor)new MetroCMD());
        } else {
            this.logger.severe(LOG_TAG + "stopped working due to Vault not installed.");
        }
    }

    public void reloadDataConfig() {
        if (this.datafile == null) {
            this.datafile = new File(this.getDataFolder(), "data.yml");
        }
        this.datayml = YamlConfiguration.loadConfiguration((File)this.datafile);
    }

    public FileConfiguration getDataConfig() {
        if (this.datayml == null) {
            this.reloadDataConfig();
        }
        return this.datayml;
    }

    public void saveDataConfig() {
        if (this.datafile == null || this.datayml == null) {
            return;
        }
        try {
            this.getDataConfig().save(this.datafile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDefaultDataConfig() {
        if (this.datafile == null) {
            this.datafile = new File(this.getDataFolder(), "data.yml");
        }
        if (!this.datafile.exists()) {
            instance.saveResource("data.yml", false);
        }
    }
}