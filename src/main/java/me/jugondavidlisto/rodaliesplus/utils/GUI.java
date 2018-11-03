package me.jugondavidlisto.rodaliesplus.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.jugondavidlisto.rodaliesplus.utils.Item;

public class GUI {
    private String name;
    private String displayName;
    private int slots;
    private ArrayList<Item> items = new ArrayList();
    private String listento;
    private Inventory inv;
    private boolean Main;
    private String openpage;

    public boolean isMain() {
        return this.Main;
    }

    public ArrayList<Item> getItems() {
        return this.items;
    }

    public String getListento() {
        return this.listento;
    }

    public String getOpenpage() {
        return this.openpage;
    }

    public GUI(String name, String displayName, int slots, ArrayList<Item> items, String listento, String openpage, boolean Main) {
        this.name = name;
        this.displayName = displayName;
        this.slots = slots;
        this.items = items;
        this.openpage = openpage;
        this.listento = listento;
        this.Main = Main;
        this.inv = Bukkit.createInventory((InventoryHolder) null, (int) this.slots, (String) this.displayName);
        for (Item i : items) {
            ItemStack item = new ItemStack(i.getMat(), 1, (byte) i.getsubtype());
            ItemMeta im = item.getItemMeta();
            im.setDisplayName(i.getDisplayName());
            im.setLore((List) i.getLores());
            item.setItemMeta(im);
            this.inv.setItem(i.getSlot() - 1, item);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void setListento(String listento) {
        this.listento = listento;
    }

    public void setInv(Inventory inv) {
        this.inv = inv;
    }

    public void setMain(boolean main) {
        this.Main = main;
    }

    public void setOpenpage(String openpage) {
        this.openpage = openpage;
    }

    public Inventory getInv() {
        return this.inv;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getSlots() {
        return this.slots;
    }
}