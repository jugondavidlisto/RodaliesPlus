package me.jugondavidlisto.rodaliesplus.utils;

import org.bukkit.Material;

public class Ticket {
    private String name;
    private boolean topUp;
    private double startupCost;
    private String displayName;
    private Material mat;

    public String getName() {
        return this.name;
    }

    public Material getMat() {
        return this.mat;
    }

    public void setTopUp(boolean topUp) {
        this.topUp = topUp;
    }

    public void setStartupCost(int startupCost) {
        this.startupCost = startupCost;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setMat(Material mat) {
        this.mat = mat;
    }

    public Ticket(String name, boolean topUp, double startupCost2, String displayName, Material mat) {
        this.name = name;
        this.topUp = topUp;
        this.startupCost = startupCost2;
        this.displayName = displayName;
        this.mat = mat;
    }

    public boolean isTopUp() {
        return this.topUp;
    }

    public double getStartupCost() {
        return this.startupCost;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}