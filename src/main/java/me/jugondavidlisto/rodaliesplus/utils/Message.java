package me.jugondavidlisto.rodaliesplus.utils;

import me.jugondavidlisto.rodaliesplus.metro.Console;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.jugondavidlisto.rodaliesplus.utils.TranslateMethods;

public class Message {
    Console instance = Console.getInstance();

    public void sendHelpMsg(Player p) {
        p.sendMessage("\u00a76---------" + TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname")));
        p.sendMessage("\u00a7aCommand Lines:");
        p.sendMessage("\u00a76 metroplus exchange <player> <tickettype> <amount>");
        p.sendMessage("\u00a76 metroplus refund <ticketid>");
        p.sendMessage("\u00a76 metroplus topup <ticketid> <value>");
        p.sendMessage("\u00a76 metroplus opengui <player>");
    }

    public void sendHelpMsgC(CommandSender p) {
        p.sendMessage("\u00a76---------" + TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname")));
        p.sendMessage("\u00a7aCommand Lines:");
        p.sendMessage("\u00a76 metroplus exchange <player> <tickettype> <amount>");
        p.sendMessage("\u00a76 metroplus refund <ticketid>");
        p.sendMessage("\u00a76 metroplus topup <ticketid> <value>");
        p.sendMessage("\u00a76 metroplus opengui <player>");
    }

    public void sendPermissionDenyMsg(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.permissiondenied")));
    }

    public void sendPermissionDenyMsgc(CommandSender p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.permissiondenied")));
    }

    public void sendTicketTypeNotFound(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.ticketnotfound")));
    }

    public void sendTicketTypeNotFoundc(CommandSender p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.ticketnotfound")));
    }

    public void sendArgumentsError(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.argumentserror")));
    }

    public void sendArgumentsErrorc(CommandSender p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.argumentserror")));
    }

    public void sendInvalidNumber(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.invalidnumber")));
    }

    public void sendInvalidNumberc(CommandSender p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.invalidnumber")));
    }

    public void sendTransactionSucess(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.transactionsucess")));
    }

    public void sendTransactionSucessc(CommandSender p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.transactionsucess")));
    }

    public void sendTransactionFail(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.transactionfail")));
    }

    public void sendTransactionFailc(CommandSender p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.transactionfail")));
    }

    public void sendPlayernotFound(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.invalidplayer")));
    }

    public void sendPlayernotFoundc(CommandSender p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.invalidplayer")));
    }

    public void sendConfigNotFound(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.confignotfound")));
    }

    public void sendConfigNotFoundc(CommandSender p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.confignotfound")));
    }

    public void sendInventoryNull(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.ticketnotfoundininventory")));
    }

    public void sendInventoryNullc(CommandSender p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.ticketnotfoundininventory")));
    }

    public void sendInOutError(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.inouterror")));
    }

    public void sendGateNotFound(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.gatenotfound")));
    }

    public void sendTicketNotHolding(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.notholdingticket")));
    }

    public void sendEnterGate(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.entergate")));
    }

    public void sendExitGate(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.exitgate")));
    }

    public void sendProtectedGate(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.protectedgate")));
    }

    public void sendProtectedSign(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.protectedsign")));
    }

    public void sendDoubleTapped(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.doubletapped")));
    }

    public void sendCannotAfford(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.cannotafford")));
    }

    public void sendRefunded(Player p) {
        p.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.refunded")));
    }

    public void sendReloadedConfig(CommandSender sender) {
        sender.sendMessage(TranslateMethods.translateColor((String)this.instance.getConfig().getString("messages.pluginname") + (String)this.instance.getConfig().getString("messages.reloaded")));
    }
}