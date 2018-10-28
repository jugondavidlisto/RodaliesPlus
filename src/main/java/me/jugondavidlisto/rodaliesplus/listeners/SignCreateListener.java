package me.jugondavidlisto.rodaliesplus.listeners;

import java.util.ArrayList;
import java.util.Set;
import me.jugondavidlisto.rodaliesplus.metro.Console;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Sign;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import me.jugondavidlisto.rodaliesplus.utils.Message;
import me.jugondavidlisto.rodaliesplus.utils.Ticket;
import me.jugondavidlisto.rodaliesplus.utils.TicketManager;
import me.jugondavidlisto.rodaliesplus.utils.TranslateMethods;
import org.bukkit.ChatColor;

public class SignCreateListener
        implements Listener {

    Console main = Console.getInstance();
    Message msg = new Message();

    public SignCreateListener() {
        this.main.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this.main);
    }

    public ArrayList<Block> getBlockSurrounding(Block b) {
        BlockFace[] faces;
        ArrayList<Block> blocks = new ArrayList<Block>();
        BlockFace[] arrblockFace = faces = new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH};
        int n = arrblockFace.length;
        int n2 = 0;
        while (n2 < n) {
            BlockFace s = arrblockFace[n2];
            Block assblock = b.getRelative(s);
            blocks.add(assblock);
            ++n2;
        }
        return blocks;
    }

    @EventHandler
    public void onSignCreate(SignChangeEvent e) {
        if (e.getLine(0).equals("[Tickets]")) {
            if (e.getPlayer().hasPermission("metroplus.createTicketSign")) {
                e.setLine(0, TranslateMethods.translateColor((String) Console.getInstance().getConfig().getString("setting.ticket.line0")));
                e.setLine(1, TranslateMethods.translateColor((String) Console.getInstance().getConfig().getString("setting.ticket.line1")));
                e.setLine(2, TranslateMethods.translateColor((String) Console.getInstance().getConfig().getString("setting.ticket.line2")));
                e.setLine(3, TranslateMethods.translateColor((String) Console.getInstance().getConfig().getString("setting.ticket.line3")));
                e.getPlayer().sendMessage(TranslateMethods.translateColor((String) this.main.getConfig().getString("messages.pluginname") + (String) this.main.getConfig().getString("messages.createdsign")));
            } else {
                e.getPlayer().sendMessage(TranslateMethods.translateColor((String) this.main.getConfig().getString("messages.pluginname") + (String) this.main.getConfig().getString("messages.permissiondenied")));
            }
        }
        if (e.getLine(0).equalsIgnoreCase("[NP]")) { //Capitalization matters for this.
            if (e.getPlayer().hasPermission("metroplus.createTicketSign")) { //If the player has that permission, it'll set the line, else, it won't, that will be good so people can't exploit a glitch, although,
                //a colored signs plugin ex: Essentials can conflict if you use it, and give the permission to reg players, I suggest not to.
                e.setLine(0, ChatColor.WHITE + ""); //Capitalization matters for this too.
                e.setLine(1, ChatColor.DARK_RED + "✖");
                e.setLine(2, ChatColor.BOLD + "No Pasar");
                e.setLine(3, ChatColor.BLACK + "");
                e.getPlayer().sendMessage(TranslateMethods.translateColor((String) this.main.getConfig().getString("messages.pluginname") + (String) this.main.getConfig().getString("messages.createdsign")));
            } else {
                e.getPlayer().sendMessage(TranslateMethods.translateColor((String) this.main.getConfig().getString("messages.pluginname") + (String) this.main.getConfig().getString("messages.permissiondenied")));
            }
        }
        if (e.getLine(0).equals("[Faregate]")) {
            if (e.getPlayer().hasPermission("metroplus.createFaregateSign")) {
                e.setLine(0, TranslateMethods.translateColor((String) Console.getInstance().getConfig().getString("setting.faregate.line0")));
                String type = e.getLine(1);
                String inout = e.getLine(2);
                String scost = e.getLine(3);
                Double cost = 0.0;
                try {
                    cost = Double.parseDouble(scost);
                } catch (NumberFormatException ex) {
                    this.msg.sendInvalidNumber(e.getPlayer());
                    return;
                }
                e.setLine(1, ChatColor.GREEN + "\u2B05");
                e.setLine(2, TranslateMethods.translateColor((String) Console.getInstance().getConfig().getString("setting.faregate.line2")));
                e.setLine(3, TranslateMethods.translateColor((String) Console.getInstance().getConfig().getString("setting.faregate.line3")));
                int cs = 0;
                if (Console.getInstance().getDataConfig().getConfigurationSection("faregate") != null && Console.getInstance().getDataConfig().getConfigurationSection("faregate").getKeys(false).size() > 0) {
                    cs = Console.getInstance().getDataConfig().getConfigurationSection("faregate").getKeys(false).size() + 1;
                }
                FileConfiguration datac = Console.getInstance().getDataConfig();
                boolean foundt = false;
                for (Ticket t : TicketManager.tickets) {
                    if (!type.equals(t.getName())) {
                        continue;
                    }
                    foundt = true;
                }
                if (!foundt) {
                    this.msg.sendTicketTypeNotFound(e.getPlayer());
                    return;
                }
                if (inout.equalsIgnoreCase("in")) {
                    datac.set("faregate." + cs + ".inout", (Object) inout);
                } else if (inout.equalsIgnoreCase("out")) {
                    datac.set("faregate." + cs + ".inout", (Object) inout);
                } else {
                    this.msg.sendInOutError(e.getPlayer());
                    return;
                }
                datac.set("faregate." + cs + ".world", (Object) e.getBlock().getWorld().getName());
                datac.set("faregate." + cs + ".x", (Object) e.getBlock().getX());
                datac.set("faregate." + cs + ".y", (Object) e.getBlock().getY());
                datac.set("faregate." + cs + ".z", (Object) e.getBlock().getZ());
                int gatex = 0;
                int gatey = 0;
                int gatez = 0;
                boolean foundgate = false;
                Block b = e.getBlock();
                Sign sign = (Sign) b.getState().getData();
                Block attachedBlock = b.getRelative(sign.getAttachedFace()).getRelative(0, -1, 0);
                Block block = null;
                for (Block s : this.getBlockSurrounding(attachedBlock)) {
                    if (!s.getType().toString().contains("GATE")) {
                        continue;
                    }
                    foundgate = true;
                    block = s;
                    gatex = s.getX();
                    gatey = s.getY();
                    gatez = s.getZ();
                    e.getPlayer().sendMessage("Bloc no null: " + gatex + " " + gatey + " " + gatez + " " + block + " " + b.getRelative(sign.getAttachedFace()) + " " + this.getBlockSurrounding(attachedBlock));
                    break;
                }
                if (block == null || !foundgate) {
                    this.msg.sendGateNotFound(e.getPlayer());
                    e.getPlayer().sendMessage("Bloc null: " + gatex + " " + gatey + " " + gatez + " " + block + " " + b.getRelative(sign.getAttachedFace()) + " " + this.getBlockSurrounding(attachedBlock));
                    return;
                }
                datac.set("faregate." + cs + ".gatex", (Object) gatex);
                datac.set("faregate." + cs + ".gatey", (Object) gatey);
                datac.set("faregate." + cs + ".gatez", (Object) gatez);
                datac.set("faregate." + cs + ".type", (Object) type);
                datac.set("faregate." + cs + ".cost", (Object) cost);
                Console.getInstance().saveDataConfig();
                e.setLine(1, TranslateMethods.translateColor((String) e.getLine(1)));
                e.setLine(2, TranslateMethods.translateColor((String) e.getLine(2)));
                e.setLine(3, TranslateMethods.translateColor((String) e.getLine(3)));
                e.getPlayer().sendMessage(TranslateMethods.translateColor((String) this.main.getConfig().getString("messages.pluginname") + (String) this.main.getConfig().getString("messages.createdsign")));
            } else {
                this.msg.sendPermissionDenyMsg(e.getPlayer());
            }
        }
    }
}
