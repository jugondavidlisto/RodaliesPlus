package me.jugondavidlisto.rodaliesplus.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import me.jugondavidlisto.rodaliesplus.metro.Console;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Gate;
import org.bukkit.material.MaterialData;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import me.jugondavidlisto.rodaliesplus.utils.GUI;
import me.jugondavidlisto.rodaliesplus.utils.GUIManager;
import me.jugondavidlisto.rodaliesplus.utils.Message;
import me.jugondavidlisto.rodaliesplus.utils.Ticket;
import me.jugondavidlisto.rodaliesplus.utils.TicketManager;
import me.jugondavidlisto.rodaliesplus.utils.TranslateMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class SignClickListener implements Listener {

    Console instance = Console.getInstance();
    Message msg = new Message();

    public SignClickListener() {
        this.instance.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this.instance);
    }

    @EventHandler
    public void onSignClickEvent(PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) && e.getClickedBlock().getType().toString().contains("GATE") && Console.getInstance().getDataConfig().getConfigurationSection("faregate") != null) {
            for (Map.Entry entry : Console.getInstance().getDataConfig().getConfigurationSection("faregate").getValues(false).entrySet()) {
                FileConfiguration config = Console.getInstance().getDataConfig();
                if (config.getInt("faregate." + (String) entry.getKey() + ".gatex") != e.getClickedBlock().getX() || config.getInt("faregate." + (String) entry.getKey() + ".gatey") != e.getClickedBlock().getY() || config.getInt("faregate." + (String) entry.getKey() + ".gatez") != e.getClickedBlock().getZ() || !config.getString("faregate." + (String) entry.getKey() + ".world").equals(e.getClickedBlock().getWorld().getName()) || e.getPlayer().hasPermission("metroplus.createFaregateSign")) {
                    continue;
                }
                e.setCancelled(true);
                this.msg.sendProtectedGate(e.getPlayer());
                return;
            }
        }
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) && e.getClickedBlock().getState() instanceof Sign) {
            Player p = e.getPlayer();
            Sign s = (Sign) e.getClickedBlock().getState();
            if (s.getLine(0).equals(TranslateMethods.translateColor((String) Console.getInstance().getConfig().getString("setting.ticket.line0")))) {
                if (p.hasPermission("metroplus.useTicketSign")) {
                    for (GUI g : GUIManager.getGUIs()) {
                        if (!g.isMain()) {
                            continue;
                        }
                        p.openInventory(g.getInv());
                    }
                } else {
                    this.msg.sendPermissionDenyMsg(e.getPlayer());
                }
            }
            if (s.getLine(0).equals(TranslateMethods.translateColor((String) Console.getInstance().getConfig().getString("setting.faregate.line0")))) {
                if (e.getPlayer().hasPermission("metroplus.useFaregateSign")) {
                    String type = "";
                    String in = "";
                    int gatex = 0;
                    int gatey = 0;
                    int gatez = 0;
                    int cost = 0;
                    Ticket ticket = null;
                    String displayName = "";
                    if (Console.getInstance().getDataConfig().getConfigurationSection("faregate") != null) {
                        for (Map.Entry entry : Console.getInstance().getDataConfig().getConfigurationSection("faregate").getValues(false).entrySet()) {
                            FileConfiguration config = Console.getInstance().getDataConfig();
                            if (config.getInt("faregate." + (String) entry.getKey() + ".x") != e.getClickedBlock().getX() || config.getInt("faregate." + (String) entry.getKey() + ".y") != e.getClickedBlock().getY() || config.getInt("faregate." + (String) entry.getKey() + ".z") != e.getClickedBlock().getZ() || !config.getString("faregate." + (String) entry.getKey() + ".world").equals(e.getClickedBlock().getWorld().getName())) {
                                continue;
                            }
                            in = config.getString("faregate." + (String) entry.getKey() + ".inout");
                            cost = config.getInt("faregate." + (String) entry.getKey() + ".cost");
                            if (!in.equalsIgnoreCase("in") && !in.equalsIgnoreCase("out")) {
                                this.msg.sendInOutError(e.getPlayer());
                                return;
                            }
                            if (in.equalsIgnoreCase("in") && (Console.listenNonTopup.contains((Object) e.getPlayer()) || Console.listenTopup.contains((Object) e.getPlayer()))) {
                                this.msg.sendDoubleTapped(e.getPlayer());
                                return;
                            }
                            if (in.equalsIgnoreCase("out") && !Console.listenNonTopup.contains((Object) e.getPlayer()) && !Console.listenTopup.contains((Object) e.getPlayer())) {
                                this.msg.sendDoubleTapped(e.getPlayer());
                                return;
                            }
                            type = config.getString("faregate." + (String) entry.getKey() + ".type");
                            boolean foundt = false;
                            boolean ticketOverride = false;
                            for (Ticket t : TicketManager.tickets) {
                                if (Console.getInstance().getConfig().getString("tickettype." + t.getName() + ".overrideSigns") != null) {
                                    ticketOverride = Console.getInstance().getConfig().getString("tickettype." + t.getName() + ".overrideSigns").equalsIgnoreCase("true") ? true : (Console.getInstance().getConfig().getString("tickettype." + t.getName() + ".overrideSigns").equalsIgnoreCase("false") ? false : false);
                                }
                                if (ticketOverride && e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(t.getDisplayName())) {
                                    foundt = true;
                                    ticket = t;
                                    displayName = t.getDisplayName();
                                    break;
                                }
                                if (!t.getName().equals(type)) {
                                    continue;
                                }
                                foundt = true;
                                ticket = t;
                                displayName = t.getDisplayName();
                                break;
                            }
                            if (!foundt) {
                                this.msg.sendTicketTypeNotFound(e.getPlayer());
                                return;
                            }
                            if (ticket == null) {
                                this.msg.sendTicketTypeNotFound(e.getPlayer());
                                return;
                            }
                            gatex = config.getInt("faregate." + (String) entry.getKey() + ".gatex");
                            gatey = config.getInt("faregate." + (String) entry.getKey() + ".gatey");
                            gatez = config.getInt("faregate." + (String) entry.getKey() + ".gatez");
                        }
                        ItemStack inhand = e.getPlayer().getItemInHand();
                        if (inhand != null && inhand.getType() != Material.AIR && inhand.getItemMeta().getDisplayName() != null && inhand.getItemMeta() != null) {
                            if (inhand.getItemMeta().getDisplayName().equals(displayName)) {
                                if (inhand.getType().equals((Object) ticket.getMat())) {
                                    String ticketid = (String) inhand.getItemMeta().getLore().get(0);
                                    ticketid = ticketid.replace(ChatColor.DARK_GRAY + "Ticket ID: A-", "");
                                    double ticketBalance = 0;
                                    for (Map.Entry entry : Console.getInstance().getDataConfig().getConfigurationSection("data." + e.getPlayer().getUniqueId()).getValues(false).entrySet()) {
                                        if (!((String) entry.getKey()).equals(ticketid)) {
                                            continue;
                                        }
                                        ticketBalance = Console.getInstance().getDataConfig().getInt("data." + e.getPlayer().getUniqueId() + "." + ticketid + ".balance");
                                    }
                                    Location gateLocation = new Location(e.getClickedBlock().getWorld(), (double) gatex, (double) gatey, (double) gatez);
                                    Block b = gateLocation.getBlock();
                                    BlockState state = b.getState();
                                    MaterialData md = state.getData();
                                    Block sb = s.getLocation().getBlock();
                                    org.bukkit.material.Sign sign = (org.bukkit.material.Sign) sb.getState().getData();
                                    Block block = b.getRelative(sign.getAttachedFace());
                                    Gate g = (Gate) md;
                                    if (in.equalsIgnoreCase("in")) {
                                        if (ticket.isTopUp()) {
                                            if (ticketBalance > 0) {
                                                if ((ticketBalance -= cost) >= 0) {
                                                    Console.getInstance().getDataConfig().set("data." + e.getPlayer().getUniqueId() + "." + ticketid + ".balance", (Object) ticketBalance);
                                                    Console.getInstance().saveDataConfig();
                                                    ticketBalance = Console.getInstance().getDataConfig().getInt("data." + e.getPlayer().getUniqueId() + "." + ticketid + ".balance");
                                                    String msg = TranslateMethods.translateColor((String) this.instance.getConfig().getString("messages.pluginname") + (String) this.instance.getConfig().getString("messages.ticketbalance"));
                                                    msg = msg.replaceAll("<balance>", String.valueOf(ticketBalance));
                                                    p.sendMessage(msg);
                                                    this.msg.sendEnterGate(e.getPlayer());

                                                    // INICIO PUERTAS VIDIRECIONALES

                                                    org.bukkit.material.Sign sign3 = (org.bukkit.material.Sign) s.getData();
                                                    BlockFace directionFacing = sign3.getFacing();

                                                    switch (directionFacing) {
                                                        case EAST:
                                                            //p.sendMessage("EAST");
                                                            directionFacing = BlockFace.SOUTH;
                                                            break;
                                                        case NORTH:
                                                            //p.sendMessage("NORTH");
                                                            directionFacing = BlockFace.EAST;
                                                            break;
                                                        case SOUTH:
                                                            //p.sendMessage("SOUTH");
                                                            directionFacing = BlockFace.WEST;
                                                            break;
                                                        case WEST:
                                                            //p.sendMessage("WEST");
                                                            directionFacing = BlockFace.NORTH;
                                                            break;
                                                        default:
                                                            break;
                                                    }

                                                    g.setFacingDirection(directionFacing);

                                                    // FIN PUERTAS VIDIRECIONALES

                                                    g.setOpen(true);
                                                    p.playSound(gateLocation, Sound.BLOCK_FENCE_GATE_OPEN, 1.0F, 1.0F);
                                                    state.update();
                                                    Console.listenWalking.put(e.getPlayer(), block);
                                                    Console.listenGate.put(e.getPlayer(), gateLocation);
                                                    Console.listenTopup.add(p);
                                                    Console.listenTicket.put(e.getPlayer(), ticket);
                                                } else {
                                                    this.msg.sendCannotAfford(p);
                                                }
                                            } else {
                                                this.msg.sendCannotAfford(e.getPlayer());
                                            }
                                        } else {
                                            g.setOpen(true);
                                            p.playSound(gateLocation, Sound.BLOCK_FENCE_GATE_OPEN, 1.0F, 1.0F);
                                            state.update();
                                            this.msg.sendEnterGate(p);
                                            Console.listenNonTopup.add(p);
                                            Console.listenWalking.put(e.getPlayer(), block);
                                            Console.listenGate.put(e.getPlayer(), gateLocation);
                                            Console.listenTicket.put(e.getPlayer(), ticket);
                                        }
                                    }
                                    if (in.equalsIgnoreCase("out")) {
                                        if (((Ticket) Console.listenTicket.get((Object) e.getPlayer())).getName().equals(ticket.getName())) {
                                            if (!ticket.isTopUp()) {
                                                if (inhand.getAmount() > 1) {
                                                    inhand.setAmount(inhand.getAmount() - 1);
                                                }
                                                if (inhand.getAmount() == 1) {
                                                    e.getPlayer().getInventory().removeItem(new ItemStack[]{inhand});
                                                }
                                                Console.getInstance().getDataConfig().set("data." + p.getUniqueId().toString() + "." + ticketid, (Object) null);
                                                Console.getInstance().saveDataConfig();
                                            }
                                            if (ticket.isTopUp() && ticketBalance <= 0) {
                                                if (inhand.getAmount() > 1) {
                                                    inhand.setAmount(inhand.getAmount() - 1);
                                                }
                                                if (inhand.getAmount() == 1) {
                                                    e.getPlayer().getInventory().removeItem(new ItemStack[]{inhand});
                                                }
                                                Console.getInstance().getDataConfig().set("data." + p.getUniqueId().toString() + "." + ticketid, (Object) null);
                                                Console.getInstance().saveDataConfig();
                                            }
                                            Console.listenNonTopup.remove((Object) e.getPlayer());
                                            Console.listenTopup.remove((Object) e.getPlayer());
                                            Console.listenTicket.remove((Object) e.getPlayer());

                                            // INICIO PUERTAS VIDIRECIONALES

                                            org.bukkit.material.Sign sign3 = (org.bukkit.material.Sign) s.getData();
                                            BlockFace directionFacing = sign3.getFacing();

                                            switch (directionFacing) {
                                                case EAST:
                                                    //p.sendMessage("EAST");
                                                    directionFacing = BlockFace.SOUTH;
                                                    break;
                                                case NORTH:
                                                    //p.sendMessage("NORTH");
                                                    directionFacing = BlockFace.EAST;
                                                    break;
                                                case SOUTH:
                                                    //p.sendMessage("SOUTH");
                                                    directionFacing = BlockFace.WEST;
                                                    break;
                                                case WEST:
                                                    //p.sendMessage("WEST");
                                                    directionFacing = BlockFace.NORTH;
                                                    break;
                                                default:
                                                    break;
                                            }

                                            g.setFacingDirection(directionFacing);

                                            // FIN PUERTAS VIDIRECIONALES

                                            g.setOpen(true);
                                            p.playSound(gateLocation, Sound.BLOCK_FENCE_GATE_OPEN, 1.0F, 1.0F);
                                            state.update();
                                            this.msg.sendExitGate(p);
                                            Console.listenWalking.put(e.getPlayer(), block);
                                            Console.listenGate.put(e.getPlayer(), gateLocation);
                                        } else {
                                            this.msg.sendTicketNotHolding(e.getPlayer());
                                        }
                                    }
                                }
                            } else {
                                this.msg.sendTicketNotHolding(e.getPlayer());
                            }
                        } else {
                            this.msg.sendTicketNotHolding(e.getPlayer());
                        }
                    } else {
                        this.msg.sendTicketNotHolding(e.getPlayer());
                    }
                } else {
                    this.msg.sendPermissionDenyMsg(e.getPlayer());
                }
            }
        }
    }
}
