package me.jugondavidlisto.rodaliesplus.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.jugondavidlisto.rodaliesplus.metro.Console;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import me.jugondavidlisto.rodaliesplus.utils.GUI;
import me.jugondavidlisto.rodaliesplus.utils.GUIManager;
import me.jugondavidlisto.rodaliesplus.utils.Item;
import me.jugondavidlisto.rodaliesplus.utils.Ticket;
import me.jugondavidlisto.rodaliesplus.utils.TicketManager;

public class InventoryListener implements Listener {
    Console console = Console.getInstance();

    public InventoryListener() {
        this.console.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this.console);
    }

    @EventHandler
    public void onPlayerInteract(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
            return;
        }
        String openpage = "";
        for (GUI g : GUIManager.getGUIs()) {
            if (!e.getInventory().getName().equals(g.getDisplayName())) continue;
            e.setCancelled(true);
            for (Ticket tickettype : TicketManager.tickets) {
                if (g.getListento().equals(tickettype.getName())) {
                    Console.listeningto.put(p, tickettype);
                }
                ItemMeta ClickedMeta = e.getCurrentItem().getItemMeta();
                if (Console.listeningto.get((Object) p) == null || !ClickedMeta.getDisplayName().equals(((Ticket) Console.listeningto.get((Object) p)).getDisplayName()) || !e.getCurrentItem().getType().equals((Object) ((Ticket) Console.listeningto.get((Object) p)).getMat()))
                    continue;
                for (GUI gu : GUIManager.getGUIs()) {
                    if (!gu.getName().equals(g.getOpenpage())) continue;
                    p.closeInventory();
                    p.openInventory(gu.getInv());
                    Console.LastListeningTo.put(p, e.getCurrentItem());
                    return;
                }
            }
            for (Item i : g.getItems()) {
                if (e.getInventory().getItem(i.getSlot() - 1) == null || !e.getInventory().getItem(i.getSlot() - 1).equals((Object) e.getCurrentItem()))
                    continue;
                p.closeInventory();
                if (i.getCommand() != null) {
                    String command = i.getCommand();
                    command = command.replaceAll("<player>", e.getWhoClicked().getName());
                    if (Console.LastListeningTo.get((Object) p) != null) {
                        ItemStack ticket = (ItemStack) Console.LastListeningTo.get((Object) p);
                        String haha = (String) ticket.getItemMeta().getLore().get(0);
                        command = command.replaceAll("<ticketid>", haha);
                    }
                    Console.getInstance().getServer().getConsoleSender().sendMessage(command);
                    Bukkit.dispatchCommand((CommandSender) Console.getInstance().getServer().getConsoleSender(), (String) command);
                }
                openpage = i.getOpenpage();
            }
        }
        for (GUI gui : GUIManager.getGUIs()) {
            if (!gui.getName().equals(openpage)) continue;
            p.openInventory(gui.getInv());
        }
    }
}