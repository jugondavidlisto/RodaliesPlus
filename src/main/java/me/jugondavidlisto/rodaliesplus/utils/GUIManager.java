package me.jugondavidlisto.rodaliesplus.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import me.jugondavidlisto.rodaliesplus.metro.Console;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import me.jugondavidlisto.rodaliesplus.utils.GUI;
import me.jugondavidlisto.rodaliesplus.utils.Item;
import me.jugondavidlisto.rodaliesplus.utils.TranslateMethods;

public class GUIManager {
    Console console = Console.getInstance();
    private static ArrayList<GUI> GUIs = new ArrayList();

    public GUIManager() {
        for (Map.Entry entry : this.console.getConfig().getConfigurationSection("GUI").getValues(false).entrySet()) {
            String openpage;
            String listento;
            String name = (String)entry.getKey();
            String displayName = TranslateMethods.translateColor((String)this.console.getConfig().getString("GUI." + (String)entry.getKey() + ".displayname"));
            int slots = this.console.getConfig().getInt("GUI." + (String)entry.getKey() + ".slots");
            ArrayList<Item> items = new ArrayList<Item>();
            boolean Main = false;
            if (this.console.getConfig().getString("GUI." + (String)entry.getKey() + ".main") != null) {
                if (this.console.getConfig().getString("GUI." + (String)entry.getKey() + ".main").equals("true")) {
                    Main = true;
                } else if (this.console.getConfig().getString("GUI." + (String)entry.getKey() + ".main").equals("false")) {
                    Main = false;
                } else {
                    this.console.getLogger().severe("[Metro+]GUI" + (String)entry.getKey() + ".main in config.yml can only be true/false!");
                    Main = false;
                    this.console.getServer().getPluginManager().disablePlugin((Plugin)this.console);
                }
            } else {
                Main = false;
            }
            if (this.console.getConfig().getString("GUI." + (String)entry.getKey() + ".listen.listento") != null && this.console.getConfig().getString("GUI." + (String)entry.getKey() + ".listen.openpage") != null) {
                listento = this.console.getConfig().getString("GUI." + (String)entry.getKey() + ".listen.listento");
                openpage = this.console.getConfig().getString("GUI." + (String)entry.getKey() + ".listen.openpage");
            } else {
                listento = "null";
                openpage = "null";
            }
            if (this.console.getConfig().getConfigurationSection("GUI." + (String)entry.getKey() + ".items") != null) {
                for (Map.Entry entrys : this.console.getConfig().getConfigurationSection("GUI." + (String)entry.getKey() + ".items").getValues(false).entrySet()) {
                    String commandchecked;
                    String nameItem = (String)entrys.getKey();
                    String displayNameItem = TranslateMethods.translateColor((String)this.console.getConfig().getString("GUI." + (String)entry.getKey() + ".items." + (String)entrys.getKey() + ".displayname"));
                    int slot = this.console.getConfig().getInt("GUI." + (String)entry.getKey() + ".items." + (String)entrys.getKey() + ".slot");
                    ArrayList<String> lore = new ArrayList<String>();
                    Iterator iterator = this.console.getConfig().getStringList("GUI." + (String)entry.getKey() + ".items." + (String)entrys.getKey() + ".lore").iterator();
                    while (iterator.hasNext()) {
                        String haha = (String)iterator.next();
                        haha = TranslateMethods.translateColor((String)haha);
                        lore.add(haha);
                    }
                    String command = this.console.getConfig().getString("GUI." + (String)entry.getKey() + ".items." + (String)entrys.getKey() + ".action.command") != null ? (commandchecked = this.console.getConfig().getString("GUI." + (String)entry.getKey() + ".items." + (String)entrys.getKey() + ".action.command")) : "";
                    String openpage1 = this.console.getConfig().getString("GUI." + (String)entry.getKey() + ".items." + (String)entrys.getKey() + ".action.openpage") != null ? this.console.getConfig().getString("GUI." + (String)entry.getKey() + ".items." + (String)entrys.getKey() + ".action.openpage") : "";
                    Material mat = Material.valueOf((String)this.console.getConfig().getString("GUI." + (String)entry.getKey() + ".items." + (String)entrys.getKey() + ".material").toUpperCase());
                    int subtype = this.console.getConfig().getInt("GUI." + (String)entry.getKey() + ".items." + (String)entrys.getKey() + ".subtype");
                    if (subtype<= 0) {
                        subtype=0;
                    }
                    Item item = new Item(nameItem, displayNameItem, slot, lore, command, openpage1, mat, subtype);
                    items.add(item);
                }
            }
            GUI gui = new GUI(name, displayName, slots, items, listento, openpage, Main);
            GUIs.add(gui);
        }
    }

    public static ArrayList<GUI> getGUIs() {
        return GUIs;
    }
}