package me.szwagru.pvp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class pvp extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {
    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        this.getCommand("pvp").setExecutor(this);
        this.getCommand("pvp").setTabCompleter(this::onTabComplete);

    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public ArrayList<String> listPvP = new ArrayList<>();
    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
            Player damaged = (Player)e.getEntity();
            Player damager = (Player)e.getDamager();
            if (this.listPvP.contains(damager.getName()) || this.listPvP.contains(damaged.getName())) {
                e.setCancelled(true);
                if(this.listPvP.contains(damaged.getName())){
                    for(String s : this.getConfig().getStringList("pvp-event-attacker-off")) {
                        s = s.replace(">>", "»");
                        s = s.replace("<<", "«");
                        s = s.replace("{o}", "•");
                        s = s.replace("{GRACZ}" , damaged.getName());
                        damager.sendMessage(Utilities.chat(s));
                    }
                }else if(this.listPvP.contains(damager.getName())){
                    for(String s : this.getConfig().getStringList("pvp-event-off")){
                        s = s.replace(">>", "»");
                        s = s.replace("<<", "«");
                        s = s.replace("{o}", "•");
                        damager.sendMessage(Utilities.chat(s));
                    }
                }

            }else if(e.getDamager() instanceof Projectile && e.getEntity() instanceof Player){
                Projectile p = (Projectile) e.getDamager();
                if(p.getShooter() instanceof Player){
                    e.setCancelled(true);
                    if(this.listPvP.contains(damaged.getName())){
                        for(String s : this.getConfig().getStringList("pvp-event-attacker-off")) {
                            s = s.replace(">>", "»");
                            s = s.replace("<<", "«");
                            s = s.replace("{o}", "•");
                            s = s.replace("{GRACZ}" , damaged.getName());
                            damager.sendMessage(Utilities.chat(s));
                        }
                    }else if(this.listPvP.contains(damager.getName())){
                        for(String s : this.getConfig().getStringList("pvp-event-off")){
                            s = s.replace(">>", "»");
                            s = s.replace("<<", "«");
                            s = s.replace("{o}", "•");
                            damager.sendMessage(Utilities.chat(s));
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (cmd.getName().equalsIgnoreCase("pvp")) {
                    if (args.length == 0) {
                        /*for (String s : this.getConfig().getStringList("pvp-message")) {
                            s = s.replace(">>", "»");
                            s = s.replace("<<", "«");
                            s = s.replace("{o}", "•");
                            s = s.replace("{GRACZ}", p.getName());
                            p.sendMessage(Utilities.chat(s));
                            Gui.pvp(p);
                        }*/
                        Gui.pvp(p);
                    }
                    if (args.length > 0) {
                        if (args[0].equalsIgnoreCase("on")) {
                            if (!(this.listPvP.contains(p.getName()))) {
                                for (String s : this.getConfig().getStringList("pvp-active")) {
                                    p.sendMessage(Utilities.chat(s));
                                    return false;
                                }
                            } else {
                                for (String s : this.getConfig().getStringList("pvp-on")) {
                                    this.listPvP.remove(p.getName());
                                    p.sendMessage(Utilities.chat(s));
                                }
                            }
                        } else if (args[0].equalsIgnoreCase("off")) {
                            if (this.listPvP.contains(p.getName())) {
                                for (String s : this.getConfig().getStringList("pvp-desactive")) {
                                    p.sendMessage(Utilities.chat(s));
                                    return false;
                                }
                            } else {
                                for (String s : this.getConfig().getStringList("pvp-off")) {
                                    this.listPvP.add(p.getName());
                                    p.sendMessage(Utilities.chat(s));
                                }
                            }
                        }
                    }
                }
            } else if (sender instanceof ConsoleCommandSender) {
                if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                    this.reloadConfig();
                    this.saveConfig();
                    sender.sendMessage(Utilities.chat("&cReload zrobiony!"));
                }
            }
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1){
            List<String> autoCompletes  = new ArrayList<>();
            autoCompletes.add("on");
            autoCompletes.add("off");
            return autoCompletes;
        }
        if(args.length >= 2){
            return new ArrayList<>();
        }
        return null;
    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().equals(Gui.inventory)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
                return;
            }
            if(e.getCurrentItem().hasItemMeta() &&
                    e.getCurrentItem().getItemMeta().hasDisplayName()
                    && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utilities.chat("&cPvP &aOFF"))) {
                if (!(this.listPvP.contains(p.getName()))) {
                    for (String s : this.getConfig().getStringList("pvp-active")) {
                        p.sendMessage(Utilities.chat(s));
                    }
                } else {
                    for (String s : this.getConfig().getStringList("pvp-on")) {
                        this.listPvP.remove(p.getName());
                        p.sendMessage(Utilities.chat(s));
                    }
                }
            }
                    /*if (!(this.listPvP.contains(p.getName()))) {
                        for (String s : this.getConfig().getStringList("pvp-active")) {
                            p.sendMessage(Utilities.chat(s));
                        }
                    } else {
                        for (String s : this.getConfig().getStringList("pvp-on")) {
                            this.listPvP.remove(p.getName());
                            p.sendMessage(Utilities.chat(s));
                        }
                    }*/
        }
    }
}