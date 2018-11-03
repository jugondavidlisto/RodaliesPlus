package me.jugondavidlisto.rodaliesplus.utils;

import java.util.ArrayList;

import org.bukkit.Material;

public class Item {
    private String name;
    private String displayName;
    private int slot;
    private ArrayList<String> lores = new ArrayList();
    private String command;
    private String openpage;
    private Material mat;
    private int subtype;

    public void setName(String name) {
        this.name = name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setLores(ArrayList<String> lores) {
        this.lores = lores;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setOpenpage(String openpage) {
        this.openpage = openpage;
    }

    public void setMat(Material mat) {
        this.mat = mat;
    }

    public Material getMat() {
        return this.mat;
    }

    public void subtype(int subtype) {
        this.subtype = subtype;
    }

    public int getsubtype() {
        return this.subtype;
    }

    public Item(String name, String displayName, int slot, ArrayList<String> lores, String command, String openpage, Material mat, int subtype) {
        this.name = name;
        this.displayName = displayName;
        this.slot = slot;
        this.lores = lores;
        this.command = command;
        this.openpage = openpage;
        this.mat = mat;
        this.subtype = subtype;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getSlot() {
        return this.slot;
    }

    public ArrayList<String> getLores() {
        return this.lores;
    }

    public String getCommand() {
        return this.command;
    }

    public String getOpenpage() {
        return this.openpage;
    }
}
