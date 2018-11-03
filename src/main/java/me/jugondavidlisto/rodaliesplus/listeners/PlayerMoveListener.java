package me.jugondavidlisto.rodaliesplus.listeners;

import java.util.HashMap;

import me.jugondavidlisto.rodaliesplus.metro.Console;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.material.Gate;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class PlayerMoveListener implements Listener {

    Console instance = Console.getInstance();

    public PlayerMoveListener() {
        this.instance.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this.instance);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (Console.listenWalking.get((Object) e.getPlayer()) != null) {
            Block b = (Block) Console.listenWalking.get((Object) e.getPlayer());
            Location gateLocation = (Location) Console.listenGate.get((Object) e.getPlayer());
            Block ba = gateLocation.getBlock();
            BlockState state = ba.getState();
            MaterialData md = state.getData();
            Gate g = (Gate) md;
            Location nextloc = b.getLocation();
            int nextx = nextloc.getBlockX();
            int nexty = nextloc.getBlockY();
            int nextz = nextloc.getBlockZ();
            if (e.getPlayer().getLocation().getBlockX() == nextx && e.getPlayer().getLocation().getBlockY() == nexty && e.getPlayer().getLocation().getBlockZ() == nextz && nextloc.getWorld() == e.getPlayer().getWorld()) {
                Console.listenWalking.remove((Object) e.getPlayer());
                Console.listenGate.remove((Object) e.getPlayer());
                g.setOpen(false);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.playSound(gateLocation, Sound.BLOCK_FENCE_GATE_CLOSE, 1.0F, 1.0F);
                }
                state.update();
            }
        }
    }
}
