package me.jugondavidlisto.rodaliesplus.listeners;

import java.util.Map;
import java.util.Set;

import me.jugondavidlisto.rodaliesplus.metro.Console;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import me.jugondavidlisto.rodaliesplus.utils.Message;
import me.jugondavidlisto.rodaliesplus.utils.TranslateMethods;

public class BlockBreakListener implements Listener {

    Console console = Console.getInstance();

    public BlockBreakListener() {
        this.console.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this.console);
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e) {
        Message msg = new Message();
        if (e.getBlock().getType().toString().contains("GATE")) {
            for (Map.Entry entry : Console.getInstance().getDataConfig().getConfigurationSection("faregate").getValues(false).entrySet()) {
                FileConfiguration config = Console.getInstance().getDataConfig();
                if (config.getInt("faregate." + (String) entry.getKey() + ".gatex") != e.getBlock().getX() || config.getInt("faregate." + (String) entry.getKey() + ".gatey") != e.getBlock().getY() || config.getInt("faregate." + (String) entry.getKey() + ".gatez") != e.getBlock().getZ() || !config.getString("faregate." + (String) entry.getKey() + ".world").equals(e.getBlock().getWorld().getName()) || e.getPlayer().hasPermission("metroplus.createFaregateSign"))
                    continue;
                e.setCancelled(true);
                msg.sendProtectedGate(e.getPlayer());
            }
        }
        if (e.getBlock().getState() instanceof Sign) {
            Sign s = (Sign) e.getBlock().getState();
            if (s.getLine(0).equals(TranslateMethods.translateColor((String) Console.getInstance().getConfig().getString("setting.faregate.line0"))) && !e.getPlayer().hasPermission("metroplus.createFaregateSign")) {
                e.setCancelled(true);
                msg.sendProtectedSign(e.getPlayer());
            }
            if (s.getLine(0).equals(TranslateMethods.translateColor((String) Console.getInstance().getConfig().getString("setting.ticket.line0"))) && !e.getPlayer().hasPermission("metroplus.createTicketSign")) {
                e.setCancelled(true);
                msg.sendProtectedSign(e.getPlayer());
            }
        }
    }
}