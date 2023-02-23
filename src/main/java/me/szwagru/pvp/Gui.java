package me.szwagru.pvp;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Collections;

public class Gui{
    public static Inventory inventory;
    private pvp plugin = new pvp();

    public Gui(){
        this.plugin = plugin;
    }
    public static void pvp(Player p){

        Inventory inventory = Bukkit.createInventory((InventoryHolder)p , 27 , (Utilities.chat("&2PvP")));
        ItemBuilder s1 = (new ItemBuilder(Material.IRON_SWORD, 1)).setTitle(Utilities.chat("&cPvP &aON")).addLores(Collections.singletonList(Utilities.chat("&7Włącza &cPvP")));//.addLores(plugin.getConfig().getStringList("pvp-on"));
        ItemBuilder s2 = (new ItemBuilder(Material.SHIELD, 1)).setTitle(Utilities.chat("&cPvP &aOFF"));//.addLores(plugin.getConfig().getStringList("pvp-off"));
        inventory.setItem(11, s2.build());
        inventory.setItem(15, s1.build());
        p.openInventory(inventory);
    }
}
