package me.jugondavidlisto.rodaliesplus.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import me.jugondavidlisto.rodaliesplus.metro.Console;
import org.bukkit.configuration.file.FileConfiguration;

public class TicketManager {

    Console console = Console.getInstance();
    public static ArrayList<Ticket> tickets = new ArrayList();

    public TicketManager() {
        Iterator localIterator = console.getConfig().getConfigurationSection("tickettype").getValues(false).entrySet().iterator();
        while (localIterator.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry) localIterator.next();
            String name = (String) entry.getKey();
            boolean Topup = false;

            String TopupS;
            if (console.getConfig().getString("tickettype." + (String) entry.getKey() + ".topup") != null) {
                TopupS = console.getConfig().getString("tickettype." + (String) entry.getKey() + ".topup");
            } else {
                TopupS = "false";
            }
            org.bukkit.Material mat = org.bukkit.Material.valueOf(console.getConfig().getString("tickettype." + (String) entry.getKey() + ".material").toUpperCase());
            if (TopupS.equals("true")) {
                Topup = true;
            } else if (TopupS.equals("false")) {
                Topup = false;
            } else {
                console.getLogger().severe("[Metro+]tickettype." + (String) entry.getKey() + ".topup in config.yml can only be true/false!");
                Topup = false;
                console.getServer().getPluginManager().disablePlugin(console);
            }
            String displayName = TranslateMethods.translateColor(console.getConfig().getString("tickettype." + (String) entry.getKey() + ".displayname"));
            double startupCost = console.getConfig().getDouble("tickettype." + (String) entry.getKey() + ".startupcost");
            Ticket ticket = new Ticket(name, Topup, startupCost, displayName, mat);
            tickets.add(ticket);
        }
    }
}
