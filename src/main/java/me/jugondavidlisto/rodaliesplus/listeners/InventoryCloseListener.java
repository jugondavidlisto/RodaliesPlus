package me.jugondavidlisto.rodaliesplus.listeners;

import java.util.HashMap;

import me.jugondavidlisto.rodaliesplus.metro.Console;
import org.bukkit.Server;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class InventoryCloseListener implements Listener {
    Console console = Console.getInstance();

    public InventoryCloseListener() {
        this.console.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this.console);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Console.listeningto.remove((Object) e.getPlayer());
    }
}
