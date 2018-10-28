/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.jugondavidlisto.rodaliesplus.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import me.jugondavidlisto.rodaliesplus.metro.Console;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import me.jugondavidlisto.rodaliesplus.utils.GUI;
import me.jugondavidlisto.rodaliesplus.utils.GUIManager;
import me.jugondavidlisto.rodaliesplus.utils.Message;
import me.jugondavidlisto.rodaliesplus.utils.Ticket;
import me.jugondavidlisto.rodaliesplus.utils.TicketManager;
import me.jugondavidlisto.rodaliesplus.utils.TranslateMethods;
import org.bukkit.ChatColor;

public class MetroCMD implements CommandExecutor {
    Message msg = new Message();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (p.hasPermission("metroplus.viewcmd")) {
                if (args.length == 0) {
                    this.msg.sendHelpMsg(p);
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reloadconfig")) {
                        Console.getInstance().reloadConfig();
                        Console.getInstance().reloadDataConfig();
                        Console.getInstance().saveConfig();
                        Console.getInstance().saveDataConfig();
                        this.msg.sendReloadedConfig(sender);
                    } else {
                        this.msg.sendArgumentsErrorc(sender);
                    }
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("refund")) {
                        boolean foundconfig = false;
                        String ticketid = args[1];
                        double Cost = 0.0;
                        Player targetP = null;
                        Ticket ticket = null;
                        if (Console.getInstance().getDataConfig().getConfigurationSection("data") != null) {
                            for (Map.Entry entry : Console.getInstance().getDataConfig().getConfigurationSection("data").getValues(false).entrySet()) {
                                if (Console.getInstance().getDataConfig().getConfigurationSection("data." + (String)entry.getKey() + "." + ticketid) == null) continue;
                                foundconfig = true;
                                String type = Console.getInstance().getDataConfig().getString("data." + (String)entry.getKey() + "." + ticketid + ".type");
                                for (Player pl : Console.getInstance().getServer().getOnlinePlayers()) {
                                    if (!pl.getUniqueId().toString().equals(pl.getUniqueId().toString())) continue;
                                    targetP = pl;
                                }
                                for (Ticket tickettype : TicketManager.tickets) {
                                    if (!tickettype.getName().equals(type)) continue;
                                    ticket = tickettype;
                                    Cost = tickettype.isTopUp() ? Console.getInstance().getDataConfig().getDouble("data." + (String)entry.getKey() + "." + ticketid + ".balance") : tickettype.getStartupCost();
                                }
                                if (!foundconfig) {
                                    this.msg.sendConfigNotFoundc(sender);
                                    return true;
                                }
                                if (ticket == null) {
                                    this.msg.sendTicketTypeNotFoundc(sender);
                                    return true;
                                }
                                if (targetP == null) {
                                    this.msg.sendPlayernotFoundc(sender);
                                    return true;
                                }
                                if (targetP.getInventory().getContents() != null) {
                                    boolean found = false;
                                    ItemStack[] arritemStack = targetP.getInventory().getContents();
                                    int n = arritemStack.length;
                                    int n2 = 0;
                                    while (n2 < n) {
                                        ItemStack is = arritemStack[n2];
                                        if (is != null && is.getType() != null && is.getType() != Material.AIR) {
                                            if (targetP.getInventory().getSize() > 0) {
                                                if (is.getItemMeta().getDisplayName() != null && is.getItemMeta().getLore() != null && is.getItemMeta().getDisplayName().equals(ticket.getDisplayName())) {
                                                    if (((String)is.getItemMeta().getLore().get(0)).equals(ticketid)) {
                                                        found = true;
                                                        EconomyResponse er = Console.economy.depositPlayer((OfflinePlayer)targetP, Cost);
                                                        if (er.transactionSuccess()) {
                                                            if (is.getAmount() > 1) {
                                                                Console.getInstance().getDataConfig().set("data." + (String)entry.getKey() + "." + ticketid, (Object)null);
                                                                Console.getInstance().saveDataConfig();
                                                                Console.getInstance();
                                                                is.setAmount(is.getAmount() - 1);
                                                                this.msg.sendTransactionSucess(targetP);
                                                            } else {
                                                                targetP.getInventory().removeItem(new ItemStack[]{is});
                                                                this.msg.sendTransactionSucess(targetP);
                                                            }
                                                        }
                                                    } else {
                                                        this.msg.sendInventoryNullc(sender);
                                                        this.msg.sendTransactionFail(targetP);
                                                    }
                                                }
                                            } else {
                                                this.msg.sendInventoryNullc(sender);
                                            }
                                        }
                                        ++n2;
                                    }
                                    if (found) continue;
                                    this.msg.sendInventoryNullc(sender);
                                    continue;
                                }
                                this.msg.sendInventoryNullc(sender);
                            }
                        } else {
                            this.msg.sendConfigNotFoundc(sender);
                        }
                    } else if (args[0].equalsIgnoreCase("balance")) {
                        String ticketid = args[1];
                        Player targetP = null;
                        boolean foundConfig = false;
                        boolean foundPlayer = false;
                        if (Console.getInstance().getDataConfig().getConfigurationSection("data") != null) {
                            for (Map.Entry entry : Console.getInstance().getDataConfig().getConfigurationSection("data").getValues(false).entrySet()) {
                                if (Console.getInstance().getDataConfig().getString("data." + (String)entry.getKey() + "." + ticketid) == null) continue;
                                foundConfig = true;
                                for (Player pl : Console.getInstance().getServer().getOnlinePlayers()) {
                                    if (!pl.getUniqueId().toString().equals(entry.getKey())) continue;
                                    foundPlayer = true;
                                    targetP = pl;
                                }
                            }
                            if (!foundConfig) {
                                this.msg.sendConfigNotFoundc(sender);
                                return true;
                            }
                            if (targetP == null) {
                                this.msg.sendPlayernotFoundc(sender);
                                return true;
                            }
                        } else {
                            this.msg.sendConfigNotFoundc(sender);
                        }
                        double balance = Console.getInstance().getDataConfig().getDouble("data." + targetP.getUniqueId().toString() + "." + ticketid + ".balance");
                        String msg = TranslateMethods.translateColor((String)Console.getInstance().getConfig().getString("messages.ticketbalance"));
                        msg = msg.replaceAll("<balance>", String.valueOf(balance));
                        targetP.sendMessage(msg);
                    } else if (args[0].equalsIgnoreCase("opengui")) {
                        Player targetP = Console.getInstance().getServer().getPlayer(args[1]);
                        for (GUI g : GUIManager.getGUIs()) {
                            if (!g.isMain()) continue;
                            targetP.openInventory(g.getInv());
                        }
                    } else {
                        this.msg.sendArgumentsErrorc(sender);
                    }
                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("topup")) {
                        String ticketid = args[1];
                        Player targetP = null;
                        double value = 0.0;
                        boolean foundConfig = false;
                        if (Console.getInstance().getDataConfig().getConfigurationSection("data") != null) {
                            for (Map.Entry entry : Console.getInstance().getDataConfig().getConfigurationSection("data").getValues(false).entrySet()) {
                                if (Console.getInstance().getDataConfig().getString("data." + (String)entry.getKey() + "." + ticketid) == null) continue;
                                foundConfig = true;
                                for (Player pl : Console.getInstance().getServer().getOnlinePlayers()) {
                                    if (!pl.getUniqueId().toString().equals(entry.getKey())) continue;
                                    targetP = pl;
                                }
                            }
                            if (targetP == null) {
                                this.msg.sendPlayernotFoundc(sender);
                                return true;
                            }
                            if (!foundConfig) {
                                this.msg.sendConfigNotFoundc(sender);
                                return true;
                            }
                            try {
                                value = Double.parseDouble(args[2]);
                            }
                            catch (NumberFormatException e) {
                                this.msg.sendInvalidNumberc(sender);
                            }
                            EconomyResponse er = Console.economy.withdrawPlayer((OfflinePlayer)targetP, value);
                            if (er.transactionSuccess()) {
                                Console.getInstance().getDataConfig().set("data." + targetP.getUniqueId().toString() + "." + ticketid + ".balance", (Object)(Console.getInstance().getDataConfig().getDouble("data." + targetP.getUniqueId().toString() + "." + ticketid + ".balance") + value));
                                this.msg.sendTransactionSucess(targetP);
                            } else {
                                this.msg.sendTransactionFail(targetP);
                            }
                        } else {
                            this.msg.sendConfigNotFoundc(sender);
                        }
                    } else {
                        this.msg.sendArgumentsError(p);
                    }
                } else if (args.length == 4) {
                    if (args[0].equalsIgnoreCase("exchange")) {
                        Player targetP = Console.getInstance().getServer().getPlayer(args[1]);
                        Ticket tickettype = null;
                        double startUpCost = 0.0;
                        int Amount = 1;
                        String id = UUID.randomUUID().toString().substring(0, 7);
                        if (targetP == null) {
                            this.msg.sendPlayernotFound(p);
                            return false;
                        }
                        try {
                            Amount = Integer.parseInt(args[3]);
                        }
                        catch (NumberFormatException e) {
                            this.msg.sendInvalidNumber(p);
                            return false;
                        }
                        for (Ticket t : TicketManager.tickets) {
                            if (!t.getName().equals(args[2])) continue;
                            tickettype = t;
                            startUpCost = t.getStartupCost();
                        }
                        if (tickettype == null) {
                            this.msg.sendTicketTypeNotFound(p);
                            return false;
                        }
                        ItemStack ticket = new ItemStack(tickettype.getMat());
                        ItemMeta ticketmeta = ticket.getItemMeta();
                        ticketmeta.setDisplayName(tickettype.getDisplayName());
                        ArrayList<String> lore = new ArrayList<String>();
                        lore.add(id);
                        ticketmeta.setLore(lore);
                        ticket.setItemMeta(ticketmeta);
                        double cost = startUpCost * (double)Amount;
                        EconomyResponse r = Console.economy.withdrawPlayer(targetP.getName(), cost);
                        if (r.transactionSuccess()) {
                            this.msg.sendTransactionSucess(targetP);
                            targetP.getInventory().addItem(new ItemStack[]{ticket});
                            if (tickettype.isTopUp()) {
                                Console.getInstance().getDataConfig().set("data." + targetP.getUniqueId() + "." + id + ".type", (Object)tickettype.getName());
                                Console.getInstance().saveDataConfig();
                                Console.getInstance().getDataConfig().set("data." + targetP.getUniqueId() + "." + id + ".balance", (Object)cost);
                                Console.getInstance().saveDataConfig();
                            } else {
                                Console.getInstance().getDataConfig().set("data." + targetP.getUniqueId() + "." + id + ".type", (Object)tickettype.getName());
                                Console.getInstance().saveDataConfig();
                            }
                        } else {
                            this.msg.sendTransactionFail(targetP);
                        }
                    }
                } else if (args.length == 5) {
                    this.msg.sendArgumentsErrorc(sender);
                } else {
                    this.msg.sendArgumentsError(p);
                }
            } else {
                this.msg.sendPermissionDenyMsg(p);
            }
        } else if (args.length == 0) {
            this.msg.sendHelpMsgC(sender);
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reloadconfig")) {
                Console.getInstance().reloadConfig();
                Console.getInstance().reloadDataConfig();
                Console.getInstance().saveConfig();
                Console.getInstance().saveDataConfig();
                this.msg.sendReloadedConfig(sender);
            } else {
                this.msg.sendArgumentsErrorc(sender);
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("refund")) {
                boolean foundconfig = false;
                String ticketid = args[1];
                double Cost = 0.0;
                Player targetP = null;
                Ticket ticket = null;
                if (Console.getInstance().getDataConfig().getConfigurationSection("data") != null) {
                    for (Map.Entry entry : Console.getInstance().getDataConfig().getConfigurationSection("data").getValues(false).entrySet()) {
                        if (Console.getInstance().getDataConfig().getConfigurationSection("data." + (String)entry.getKey() + "." + ticketid) == null) continue;
                        foundconfig = true;
                        String type = Console.getInstance().getDataConfig().getString("data." + (String)entry.getKey() + "." + ticketid + ".type");
                        for (Player pl : Console.getInstance().getServer().getOnlinePlayers()) {
                            if (!pl.getUniqueId().toString().equals(pl.getUniqueId().toString())) continue;
                            targetP = pl;
                        }
                        for (Ticket tickettype : TicketManager.tickets) {
                            if (!tickettype.getName().equals(type)) continue;
                            ticket = tickettype;
                            Cost = tickettype.isTopUp() ? Console.getInstance().getDataConfig().getDouble("data." + (String)entry.getKey() + "." + ticketid + ".balance") : tickettype.getStartupCost();
                        }
                        if (!foundconfig) {
                            this.msg.sendConfigNotFoundc(sender);
                            return true;
                        }
                        if (ticket == null) {
                            this.msg.sendTicketTypeNotFoundc(sender);
                            return true;
                        }
                        if (targetP == null) {
                            this.msg.sendPlayernotFoundc(sender);
                            return true;
                        }
                        if (targetP.getInventory().getContents() != null) {
                            boolean found = false;
                            ItemStack[] arritemStack = targetP.getInventory().getContents();
                            int r = arritemStack.length;
                            int is = 0;
                            while (is < r) {
                                ItemStack is2 = arritemStack[is];
                                if (is2 != null && is2.getType() != null && is2.getType() != Material.AIR) {
                                    if (targetP.getInventory().getSize() > 0) {
                                        if (is2.getItemMeta().getDisplayName() != null && is2.getItemMeta().getLore() != null && is2.getItemMeta().getDisplayName().equals(ticket.getDisplayName())) {
                                            if (((String)is2.getItemMeta().getLore().get(0)).equals(ticketid)) {
                                                found = true;
                                                EconomyResponse er = Console.economy.depositPlayer((OfflinePlayer)targetP, Cost);
                                                if (er.transactionSuccess()) {
                                                    if (is2.getAmount() > 1) {
                                                        Console.getInstance().getDataConfig().set("data." + (String)entry.getKey() + "." + ticketid, (Object)null);
                                                        Console.getInstance().saveDataConfig();
                                                        Console.getInstance();
                                                        is2.setAmount(is2.getAmount() - 1);
                                                        this.msg.sendTransactionSucess(targetP);
                                                    } else {
                                                        targetP.getInventory().removeItem(new ItemStack[]{is2});
                                                        this.msg.sendTransactionSucess(targetP);
                                                    }
                                                }
                                            } else {
                                                this.msg.sendInventoryNullc(sender);
                                                this.msg.sendTransactionFail(targetP);
                                            }
                                        }
                                    } else {
                                        this.msg.sendInventoryNullc(sender);
                                    }
                                }
                                ++is;
                            }
                            if (found) continue;
                            this.msg.sendInventoryNullc(sender);
                            continue;
                        }
                        this.msg.sendInventoryNullc(sender);
                    }
                } else {
                    this.msg.sendConfigNotFoundc(sender);
                }
            } else if (args[0].equalsIgnoreCase("balance")) {
                String ticketid = args[1];
                Player targetP = null;
                boolean foundConfig = false;
                boolean foundPlayer = false;
                if (Console.getInstance().getDataConfig().getConfigurationSection("data") != null) {
                    for (Map.Entry entry : Console.getInstance().getDataConfig().getConfigurationSection("data").getValues(false).entrySet()) {
                        if (Console.getInstance().getDataConfig().getString("data." + (String)entry.getKey() + "." + ticketid) == null) continue;
                        foundConfig = true;
                        for (Player pl : Console.getInstance().getServer().getOnlinePlayers()) {
                            if (!pl.getUniqueId().toString().equals(entry.getKey())) continue;
                            foundPlayer = true;
                            targetP = pl;
                        }
                    }
                    if (!foundConfig) {
                        this.msg.sendConfigNotFoundc(sender);
                        return true;
                    }
                    if (targetP == null) {
                        this.msg.sendPlayernotFoundc(sender);
                        return true;
                    }
                } else {
                    this.msg.sendConfigNotFoundc(sender);
                }
                double balance = Console.getInstance().getDataConfig().getDouble("data." + targetP.getUniqueId().toString() + "." + ticketid + ".balance");
                String msg = TranslateMethods.translateColor((String)Console.getInstance().getConfig().getString("messages.ticketbalance"));
                msg = msg.replaceAll("<balance>", String.valueOf(balance));
                targetP.sendMessage(msg);
            } else if (args[0].equalsIgnoreCase("opengui")) {
                Player targetP = Console.getInstance().getServer().getPlayer(args[1]);
                for (GUI g : GUIManager.getGUIs()) {
                    if (!g.isMain()) continue;
                    targetP.openInventory(g.getInv());
                }
            } else {
                this.msg.sendArgumentsErrorc(sender);
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("topup")) {
                String ticketid = args[1];
                Player targetP = null;
                double value = 0.0;
                boolean foundConfig = false;
                if (Console.getInstance().getDataConfig().getConfigurationSection("data") != null) {
                    for (Map.Entry entry : Console.getInstance().getDataConfig().getConfigurationSection("data").getValues(false).entrySet()) {
                        if (Console.getInstance().getDataConfig().getString("data." + (String)entry.getKey() + "." + ticketid) == null) continue;
                        foundConfig = true;
                        for (Player pl : Console.getInstance().getServer().getOnlinePlayers()) {
                            if (!pl.getUniqueId().toString().equals(entry.getKey())) continue;
                            targetP = pl;
                        }
                    }
                    if (targetP == null) {
                        this.msg.sendPlayernotFoundc(sender);
                        return true;
                    }
                    if (!foundConfig) {
                        this.msg.sendConfigNotFoundc(sender);
                        return true;
                    }
                    try {
                        value = Double.parseDouble(args[2]);
                    }
                    catch (NumberFormatException e) {
                        this.msg.sendInvalidNumberc(sender);
                    }
                    EconomyResponse er = Console.economy.withdrawPlayer((OfflinePlayer)targetP, value);
                    if (er.transactionSuccess()) {
                        Console.getInstance().getDataConfig().set("data." + targetP.getUniqueId().toString() + "." + ticketid + ".balance", (Object)(Console.getInstance().getDataConfig().getDouble("data." + targetP.getUniqueId().toString() + "." + ticketid + ".balance") + value));
                        this.msg.sendTransactionSucess(targetP);
                    } else {
                        this.msg.sendTransactionFail(targetP);
                    }
                } else {
                    this.msg.sendConfigNotFoundc(sender);
                }
            } else {
                this.msg.sendArgumentsErrorc(sender);
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("exchange")) {
                Player targetP = Console.getInstance().getServer().getPlayer(args[1]);
                Ticket tickettype = null;
                double startUpCost = 0.0;
                int Amount = 1;
                String id = UUID.randomUUID().toString().substring(0, 4);
                if (targetP == null) {
                    this.msg.sendPlayernotFoundc(sender);
                    return false;
                }
                try {
                    Amount = Integer.parseInt(args[3]);
                }
                catch (NumberFormatException e) {
                    this.msg.sendInvalidNumberc(sender);
                    return false;
                }
                for (Ticket t : TicketManager.tickets) {
                    if (!t.getName().equals(args[2])) continue;
                    tickettype = t;
                    startUpCost = t.getStartupCost();
                }
                if (tickettype == null) {
                    this.msg.sendTicketTypeNotFoundc(sender);
                    return false;
                }
                ItemStack ticket = new ItemStack(tickettype.getMat());
                ItemMeta ticketmeta = ticket.getItemMeta();
                ticketmeta.setDisplayName(tickettype.getDisplayName());
                ArrayList<String> lore = new ArrayList<String>();
                lore.add(ChatColor.DARK_GRAY + "Ticket ID: A-" + id);
                if (tickettype.getName().equals("pacoMetro")) {
                    lore.add("");
                    //lore.add(ChatColor.DARK_AQUA + "" + ChatColor.ITALIC + "for WolHaven Metro");
                    lore.add(ChatColor.DARK_AQUA + "" + ChatColor.ITALIC + "for pacoMetro");
                }
                if (tickettype.getName().equals("BERT")) {
                    lore.add("");
                    //lore.add(ChatColor.DARK_AQUA + "" + ChatColor.ITALIC + "for WolHaven Metro");
                    lore.add(ChatColor.DARK_AQUA + "" + ChatColor.ITALIC + "for BERT");
                }
                ticketmeta.setLore(lore);
                ticket.setItemMeta(ticketmeta);
                double cost = startUpCost * (double)Amount;
                EconomyResponse r = Console.economy.withdrawPlayer(targetP.getName(), cost);
                if (r.transactionSuccess()) {
                    this.msg.sendTransactionSucess(targetP);
                    targetP.getInventory().addItem(new ItemStack[]{ticket});
                    if (tickettype.isTopUp()) {
                        Console.getInstance().getDataConfig().set("data." + targetP.getUniqueId() + "." + id + ".type", (Object)tickettype.getName());
                        Console.getInstance().saveDataConfig();
                        Console.getInstance().getDataConfig().set("data." + targetP.getUniqueId() + "." + id + ".balance", (Object)cost);
                        Console.getInstance().saveDataConfig();
                    } else {
                        Console.getInstance().getDataConfig().set("data." + targetP.getUniqueId() + "." + id + ".type", (Object)tickettype.getName());
                        Console.getInstance().saveDataConfig();
                    }
                } else {
                    this.msg.sendTransactionFail(targetP);
                }
            }
        } else if (args.length == 5) {
            this.msg.sendArgumentsErrorc(sender);
        } else {
            this.msg.sendArgumentsErrorc(sender);
        }
        return false;
    }
}