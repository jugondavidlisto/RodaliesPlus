package me.jugondavidlisto.rodaliesplus.utils;

public class TranslateMethods {
    public static String translateColor(String msg) {
        msg = msg.replaceAll("&", "\u00a7");
        return msg;
    }
}